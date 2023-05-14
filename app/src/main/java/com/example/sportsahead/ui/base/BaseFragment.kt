package com.example.sportsahead.ui.base

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sportsahead.di.components.FragmentComponent
import com.example.sportsahead.di.modules.FragmentModule
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var fragmentComponent: FragmentComponent

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initFragmentComponent()
    }

    private fun initFragmentComponent() {
        fragmentComponent = (activity as BaseActivity)
            .activityComponent
            .fragmentComponent(FragmentModule(this))
        fragmentComponent.inject(this)

    }
}