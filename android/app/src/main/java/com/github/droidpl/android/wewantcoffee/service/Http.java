package com.github.droidpl.android.wewantcoffee.service;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by feantury on 14/11/15.
 */
public class Http {
    private static OkHttpClient sClient;
    public static OkHttpClient client(){
        if(sClient == null){
            sClient = new OkHttpClient();
        }
        return sClient;
    }
}
