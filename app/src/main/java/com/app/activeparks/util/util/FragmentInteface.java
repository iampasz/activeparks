package com.app.activeparks.util.util;


import androidx.fragment.app.Fragment;

public interface FragmentInteface {

    void show(Fragment fragment);

    void navigation(int id);

    void message(String msg);
}
