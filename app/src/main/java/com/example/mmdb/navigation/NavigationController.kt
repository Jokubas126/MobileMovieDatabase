package com.example.mmdb.navigation

import android.os.Parcelable
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mmdb.R
import com.example.mmdb.config.DrawerConfig
import com.jokubas.mmdb.util.navigationtools.WrappedFragmentAction
import com.example.mmdb.navigation.configproviders.NavigationWrapperFragmentConfigProvider
import com.example.mmdb.ui.NavigationWrapperFragment
import com.example.mmdb.ui.NavigationWrapperFragmentArgs
import com.example.mmdb.ui.drawer.DrawerAction
import com.example.mmdb.ui.drawer.DrawerBehaviorInterface
import com.jokubas.mmdb.util.extensions.popSafe
import java.util.*

fun Fragment.requireNavController(): NavigationController = (requireActivity() as NavigationActivity).navigationController

class NavigationController(
    activity: NavigationActivity,
    private val drawerInteractor: DrawerBehaviorInterface,
    private val drawerConfig: DrawerConfig
) : BaseNavigationController(), NavigationDirections {

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
        get() = currentFragmentManager.backStackEntryCount

    private val hasNoChild: Boolean
        get() = childCount == 0

    private val isLastChild: Boolean
        get() = childCount == 1

    private val mainBackStackCount: Int
        get() = parentFragmentManager.backStackEntryCount

    private val dialogFragmentStack: Stack<DialogFragment> = Stack()

    private val navigationHolderStack: Stack<NavigationHolder> = Stack()

    override fun goTo(action: Parcelable, animation: Animation, useWrapper: Boolean) {
        when {
            action is DrawerAction ->
                if (action == DrawerAction.Open)
                    drawerInteractor.openDrawer()
                else drawerInteractor.closeDrawer()
            action is WrappedFragmentAction && !animation.isInner -> {
                resetStacks()
                putInWrapper(action, animation)
                drawerConfig.setDrawerEnabled(!animation.isInner)
            }
            else -> {
                resolveFragment(action)?.let { fragment ->
                    if (useWrapper)
                        resetStacks()
                    showFragment(action, fragment, animation, useWrapper)
                    drawerConfig.setDrawerEnabled(!animation.isInner)
                }
            }
        }
    }

    override fun goBack(alt: (() -> Unit)?) {
        when {
            dialogFragmentStack.isNotEmpty() -> {
                dialogFragmentStack.popSafe()?.dismiss()
            }
            hasNoChild && !isTopFragment -> {
                currentNavigationHolder?.onToolbarChanged?.invoke(true)
                drawerConfig.setDrawerEnabled(mainBackStackCount == 0 or 1)
                parentFragmentManager.popBackStack()
            }
            isLastChild && isTopFragment -> {
                currentNavigationHolder?.onToolbarChanged?.invoke(true)
                drawerConfig.setDrawerEnabled(mainBackStackCount == 0 or 1)
                currentFragmentManager.popBackStack()
            }
            else -> {
                alt?.invoke()
            }
        }
    }

    private fun showFragment(
        action: Parcelable,
        fragment: Fragment,
        animation: Animation?,
        useWrapper: Boolean
    ) {
        when {
            fragment is DialogFragment -> {
                dialogFragmentStack.push(fragment)
                fragment.show(currentFragmentManager, fragment.javaClass.simpleName)
            }
            else -> {
                showFragmentInFullscreen(
                    fragment = if (useWrapper) createWrapperFragment(action) else fragment,
                    animation = animation
                )
            }

        }
    }

    private fun showFragmentInFullscreen(fragment: Fragment, animation: Animation?) {
        parentFragmentManager
            .beginTransaction()
            .apply {
                animation?.let { anim ->
                    setCustomAnimations(anim.enter, anim.exit, anim.popEnter, anim.popExit)
                }
                add(R.id.rootContainer, fragment)

                animation?.let { addToBackStack(fragment.id.toString()) }
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

    override fun putInWrapper(action: Parcelable, animation: Animation?) {
        resolveFragment(action)?.let { fragment ->
            currentFragmentManager
                .beginTransaction()
                .apply {
                    animation?.let { anim ->
                        setCustomAnimations(anim.enter, anim.exit, anim.popEnter, anim.popExit)
                    }
                    replace(R.id.wrapperContainer, fragment)
                }
                .commitAllowingStateLoss()
        }
    }

    fun attachChildFragmentManager(
        fragmentManager: FragmentManager,
        onToolbarChanged: ((isTopFragment: Boolean) -> Unit)? = null
    ) {
        navigationHolderStack.push(
            NavigationHolder(
                fragmentManager = fragmentManager,
                onToolbarChanged = onToolbarChanged
            )
        )
    }

    fun detachFromNavigationController() {
        navigationHolderStack.popSafe()
    }

    private fun resetStacks() {
        dialogFragmentStack.clear()
        navigationHolderStack.clear()
    }

    private data class NavigationHolder(
        val fragmentManager: FragmentManager,
        val onToolbarChanged: ((isTopFragment: Boolean) -> Unit)?
    )

    enum class Animation(
        @AnimatorRes @AnimRes val enter: Int,
        @AnimatorRes @AnimRes val exit: Int,
        @AnimatorRes @AnimRes val popEnter: Int,
        @AnimatorRes @AnimRes val popExit: Int,
        val isInner: Boolean = true
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
            R.anim.fade_out,
            false
        )
    }
}

