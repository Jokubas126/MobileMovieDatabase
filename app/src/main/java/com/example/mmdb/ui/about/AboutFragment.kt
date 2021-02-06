package com.example.mmdb.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mmdb.R
import com.example.mmdb.navigation.ConfigFragmentArgs
import com.example.mmdb.navigation.actions.AboutFragmentAction

object AboutFragmentArgs : ConfigFragmentArgs<AboutFragmentAction, AboutFragmentConfig>()

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }
}