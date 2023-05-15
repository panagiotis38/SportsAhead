package com.example.sportsahead.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import com.example.sportsahead.ui.base.BaseFragment
import com.example.sportsahead.ui.dashboard.compose.DashboardScreen
import com.example.sportsahead.ui.theme.SportsAheadTheme

class DashboardFragment : BaseFragment() {


    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initViewModel()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SportsAheadTheme {
                    DashboardScreen(viewModel = viewModel)
                }
            }
        }.also {
            viewModel.fetchUpcomingEvents()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)[DashboardViewModel::class.java]
    }


    companion object {
        fun newInstance() = DashboardFragment()
    }
}