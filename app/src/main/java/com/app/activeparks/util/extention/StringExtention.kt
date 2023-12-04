package com.app.activeparks.util.extention

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun simpleTraining() =
    "848e3121-4a2b-413d-8a8f-ebdd4ecf2840"

fun routeTraining() =
    "bd09f36f-835c-49e4-88b8-4f835c1602ac"

fun onlineTraining() =
    "e58e5c86-5ca7-412f-94f0-88effd1a45a8"


fun FragmentManager.replaceFragment(container:Int, fragment:Fragment) =
    this.beginTransaction()
        .replace(container, fragment)
        .commit()

fun FragmentManager.removeFragment(fragment:Fragment) =
    this.beginTransaction()
        .remove(fragment)
        .commit()


