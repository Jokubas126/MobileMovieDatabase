package com.example.mmdb.navigation

import android.os.Parcelable
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mmdb.R
import com.example.mmdb.config.DrawerConfig
import com.example.mmdb.navigation.configproviders.NavigationWrapperFragmentConfigProvider
import com.example.mmdb.ui.NavigationWrapperFragment
import com.example.mmdb.ui.NavigationWrapperFragmentArgs
import com.example.mmdb.ui.drawer.DrawerAction
import com.example.mmdb.ui.drawer.DrawerBehaviorInterface
import com.jokubas.mmdb.util.extensions.popSafe
import java.util.*

class NavigationController(
    activity: NavigationActivity,
    private val drawerInteractor: DrawerBehaviorInterface,
    private val drawerConfig: DrawerConfig
) : NavigationDirections {

    private fun resolveFragment(action: Parcelable): Fragment? =
        actionRoutes.find { actionFragmentProviderPair ->
            actionFragmentProviderPair.key == action::class.java
        }?.let { providerPair ->
            providerPair.value as FragmentProvider<Parcelable>
        }?.let { fragmentProvider ->
            fragmentProvider(action)
        }

    private fun resolveScreenDecoration(action: Parcelable): ScreenDecoration? =
        actionRoutes.find { actionFragmentProviderPair ->
            actionFragmentProviderPair.key == action::class.java
        }?.screenDecoration

    private val parentFragmentManager: FragmentManager = activity.supportFragmentManager

    private val currentNavigationHolder: NavigationHolder?
        get() = if (navigationHolderStack.size > 0) navigationHolderStack.peek() else null

    private val isTopFragment: Boolean
        get() = currentNavigationHolder == null

    private val currentChildFragmentManager: FragmentManager?
        get() = currentNavigationHolder?.fragmentManager

    private val currentFragmentManager: FragmentManager
        get() = currentChildFragmentManager ?: parentFragmentManager

    private val childCount: Int
        get() = currentChildFragmentManager?.backStackEntryCount ?: 0

    private val hasNoChild: Boolean
        get() = childCount == 0

    private val isLastChild: Boolean
        get() = childCount == 1

    private val dialogFragmentStack: Stack<DialogFragment> = Stack()

    private val navigationHolderStack: Stack<NavigationHolder> = Stack()

    override fun goTo(action: Parcelable, animation: Animation?, shouldAddWrapper: Boolean) {
        when (action) {
            is DrawerAction -> {
                if (action == DrawerAction.Open) {
                    drawerInteractor.openDrawer()
                } else drawerInteractor.closeDrawer()
            }
            else -> {
                resolveFragment(action)?.let { fragment ->
                    resolveScreenDecoration(action)?.let { decoration ->
                        if (shouldAddWrapper)
                            resetStacks()
                        showFragment(action, fragment, decoration, animation, shouldAddWrapper)
                        drawerConfig.isDrawerEnabled.set(decoration is ScreenDecoration.WithDrawer)
                    }
                }
            }
        }

    }

    override fun goBack() {
        when {
            dialogFragmentStack.isNotEmpty() -> {
                dialogFragmentStack.popSafe()?.dismiss()
            }
            hasNoChild -> {
                currentNavigationHolder?.onToolbarChanged?.invoke(true)
                parentFragmentManager.popBackStack()
                resetStacks()
            }
            else -> {
                currentNavigationHolder?.onToolbarChanged?.invoke(isLastChild)
                currentFragmentManager.popBackStack()
            }
        }
    }

    private fun showFragment(
        action: Parcelable,
        fragment: Fragment,
        decoration: ScreenDecoration,
        animation: Animation?,
        shouldAddWrapper: Boolean
    ) {
        when {
            fragment is DialogFragment -> {
                dialogFragmentStack.push(fragment)
                fragment.show(currentFragmentManager, fragment.javaClass.simpleName)
            }
            decoration is ScreenDecoration.WithDrawer -> {
                resetStacks()
                showFragmentInFullscreen(
                    fragment = if (shouldAddWrapper) createWrapperFragment(action) else fragment,
                    animation = animation
                )
            }
            else -> showFragmentInFullscreen(
                fragment = if (shouldAddWrapper) createWrapperFragment(action) else fragment,
                animation = animation
            )
        }
    }

    override fun putInWrapper(action: Parcelable) {
        resolveFragment(action)?.let { fragment ->
            currentFragmentManager
                .beginTransaction()
                .apply {
                    val transactionId: String = UUID.randomUUID().toString()
                    this.replace(R.id.wrapperContainer, fragment, transactionId)
                }
                .commitAllowingStateLoss()
        }
    }

    private fun showFragmentInFullscreen(fragment: Fragment, animation: Animation?) {
        val transactionId: String = UUID.randomUUID().toString()
        val containerId: Int = R.id.rootContainer

        parentFragmentManager
            .beginTransaction()
            .apply {
                animation?.let { anim ->
                    setCustomAnimations(anim.enter, anim.exit, anim.popEnter, anim.popExit)
                }
                this.add(containerId, fragment, transactionId)

                animation?.let { this.addToBackStack(fragment.id.toString()) }
            }
            .commitAllowingStateLoss()
    }

    private fun createWrapperFragment(action: Parcelable): Fragment =
        NavigationWrapperFragment().apply {
            arguments = NavigationWrapperFragmentArgs.create(
                action = action,
                configProvider = NavigationWrapperFragmentConfigProvider::class.java
            )
        }

    fun attachChildFragmentManager(
        fragmentManager: FragmentManager,
        onToolbarChanged: ((isTopFragment: Boolean) -> Unit)? = null
    ) {
        navigationHolderStack.push(
            NavigationHolder(
                fragmentManager,
                onToolbarChanged
            )
        )
    }

    fun detachFromNavigationController() {
        navigationHolderStack.popSafe()
    }

    override fun isOnForeground(): Boolean = isTopFragment

    private fun resetStacks() {
        navigationHolderStack.clear()
        dialogFragmentStack.clear()
    }

    private data class NavigationHolder(
        val fragmentManager: FragmentManager,
        val onToolbarChanged: ((isTopFragment: Boolean) -> Unit)?
    )

    enum class Animation(
        @AnimatorRes @AnimRes val enter: Int,
        @AnimatorRes @AnimRes val exit: Int,
        @AnimatorRes @AnimRes val popEnter: Int,
        @AnimatorRes @AnimRes val popExit: Int
    ) {
        FromRight(
            R.anim.in_from_right,
            R.anim.out_to_left,
            R.anim.in_from_left,
            R.anim.out_to_right
        ),
        FromBottom(
            R.anim.in_from_bottom,
            R.anim.out_to_top,
            R.anim.in_from_top,
            R.anim.out_to_bottom
        ),
        FadeIn(
            R.anim.fade_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.fade_out
        )
    }
}

