package com.sanxynet.movie_mvvm.ui.splash

import android.os.Bundle
import com.sanxynet.movie_mvvm.common.navigation.navigateToHome
import com.sanxynet.movie_mvvm.base.BaseActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateToHome()
    }

}