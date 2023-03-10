package com.app.activeparks;

import android.util.Log;

public class Test {

    public static Test d(String msg){
        try {
            Log.d("TEST_APP", "msg: " + msg);
        }catch (Exception e){

        }
        return null;
    }
}
