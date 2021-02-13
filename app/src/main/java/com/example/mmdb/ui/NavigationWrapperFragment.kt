package com.example.mmdb.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mmdb.databinding.FragmentMainBinding
import com.example.mmdb.navigation.BaseNavigationFragment
import com.example.mmdb.navigation.ConfigFragmentArgs
import com.example.mmdb.navigation.action
import kotlinx.android.synthetic.main.fragment_main.*

object NavigationWrapperFragmentConfig
object NavigationWrapperFragmentArgs :
    ConfigFragmentArgs<Parcelable, NavigationWrapperFragmentConfig>()

class NavigationWrapperFragment : BaseNavigationFragment() {

    private val action: Parcelable by action()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMainBinding.inflate(inflater, container, false).apply {
            viewModel = toolbarViewModel
            lifecycleOwner = this@NavigationWrapperFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarViewModel.attachToNavigationController(
            fragmentManager = childFragmentManager
        )

        navController.putInWrapper(action = action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navController.detachFromNavigationController()
    }
}