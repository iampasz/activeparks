package com.app.activeparks.util.extention

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.activeparks.MainActivity

fun FragmentManager.removeFragment(fragment:Fragment) =
    this.beginTransaction()
        .remove(fragment)
        .commit()

fun mainAddFragment(mainActivity: MainActivity, fragment:Fragment) =
    mainActivity.addFragment(fragment)




