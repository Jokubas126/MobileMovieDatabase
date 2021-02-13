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
    private val drawerInteractor: DrawerBehaviorInterface
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

    private val Animation?.toRootFrame: Boolean
        get() = this?.showInFullscreen ?: false

    enum class Animation(
        @AnimatorRes @AnimRes val enter: Int,
        @AnimatorRes @AnimRes val exit: Int,
        @AnimatorRes @AnimRes val popEnter: Int,
        @AnimatorRes @AnimRes val popExit: Int,
        val showInFullscreen: Boolean = false
    ) {
        FromRight(
            R.anim.in_from_right,
            R.anim.out_to_left,
            R.anim.in_from_left,
            R.anim.out_to_right,
            true
        ),
        FromBottom(
            R.anim.in_from_bottom,
            R.anim.out_to_top,
            R.anim.in_from_top,
            R.anim.out_to_bottom,
            true
        ),
        FadeIn(
            R.anim.fade_in,
            R.anim.fade_out,
            R.anim.fade_in,
            R.anim.fade_out,
            true
        )
    }

    private var isTerminalFragment: Boolean = false

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
                        if (isTerminalFragment) {
                            goBack()
                        }
                        showFragment(action, fragment, decoration, animation, shouldAddWrapper)
                        isTerminalFragment = decoration == ScreenDecoration.Wrapped
                    }
                }
            }
        }

    }

    override fun goBack(toRoot: Boolean) {
        when {
            dialogFragmentStack.isNotEmpty() -> {
                dialogFragmentStack.popSafe()?.dismiss()
            }
            toRoot -> {
                currentNavigationHolder?.onToolbarChanged?.invoke(true)
                parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                isTerminalFragment = false
            }
            isTerminalFragment -> {
                currentNavigationHolder?.onToolbarChanged?.invoke(isLastChild)
                parentFragmentManager.popBackStack()
                isTerminalFragment = false
            }
            hasNoChild -> {
                currentNavigationHolder?.onToolbarChanged?.invoke(true)
                parentFragmentManager.popBackStack()
                navigationHolderStack.popSafe()
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
            !animation.toRootFrame && decoration is ScreenDecoration.Wrapped -> {
                if (isTopFragment) {
                    resetStacks()
                }
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
            val transactionId: String = UUID.randomUUID().toString()
            val containerId: Int = R.id.wrapperContainer

            currentFragmentManager
                .beginTransaction()
                .apply {
                    this.replace(containerId, fragment, transactionId)
                }
                .commitAllowingStateLoss()

            if (isTopFragment)
                resetStacks()
        }
    }

    private fun showFragmentInFullscreen(fragment: Fragment, animation: Animation?) {
        val transactionId: String = UUID.randomUUID().toString()
        val isToRootFrame = animation.toRootFrame

        val containerId: Int = if (isToRootFrame) {
            R.id.rootContainer
        } else R.id.wrapperContainer

        val fragmentManager = if (isToRootFrame) {
            parentFragmentManager
        } else currentFragmentManager

        fragmentManager
            .beginTransaction()
            .apply {
                animation?.let { anim ->
                    setCustomAnimations(anim.enter, anim.exit, anim.popEnter, anim.popExit)
                }
                if (isToRootFrame) {
                    this.add(containerId, fragment, transactionId)
                } else {
                    this.replace(containerId, fragment, transactionId)
                }
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
}

