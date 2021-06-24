package com.challenge.kippo

import android.app.Application
import androidx.fragment.app.Fragment
import com.challenge.kippo.ui.main.GameCardAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

//@HiltAndroidApp
class KippoChallengeApplication : Application() {
  /*  @Module
    @InstallIn(SingletonComponent::class)
    object GameCardAdapterModule{
        @Provides
        fun provideGameAdapter(context : Fragment) = GameCardAdapter(context)
    }
*/
}