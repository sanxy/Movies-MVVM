package com.sanxynet.movie_mvvm.common.navigation

import android.app.Activity
import android.content.Intent
import com.sanxynet.movie_mvvm.ui.home.HomeActivity

fun Activity.navigateToHome() {
    this.startActivity(Intent(this, HomeActivity::class.java))
    this.finish()
}