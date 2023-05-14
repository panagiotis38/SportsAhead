package com.example.sportsahead.di.modules

import androidx.fragment.app.Fragment
import com.example.sportsahead.di.annotations.PerFragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private var fragment: Fragment) {

    @Provides
    @PerFragment
    fun provideFragment(): Fragment {
        return fragment
    }
}