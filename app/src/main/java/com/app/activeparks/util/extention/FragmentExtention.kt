package com.app.activeparks.util.extention

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.activeparks.MainActivity

//fun FragmentManager.replaceFragment(container:Int, fragment:Fragment) =
//    this.beginTransaction()
//        .replace(container, fragment)
//        .commit()

fun FragmentManager.removeFragment(fragment:Fragment) =
    this.beginTransaction()
        .remove(fragment)
        .commit()

fun mainAddFragment(mainActivity: MainActivity, fragment:Fragment) =
    mainActivity.addFragment(fragment)

fun mainReplaceFragment(mainActivity: MainActivity, fragment:Fragment) =
    mainActivity.openFragment(fragment)




