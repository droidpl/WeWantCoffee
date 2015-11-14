package com.github.droidpl.android.syncommand;

import android.support.annotation.NonNull;

import com.google.android.gms.nearby.messages.Message;

import java.util.Arrays;

/**
 * Created by feantury on 14/11/15.
 */
public class Action {
    public String mAction;
    public String[] mArguments;

    public static Action parse(Message message){
        String[] items = new String(message.getContent()).split(":");
        String[] arguments = null;
        if(items.length > 1) {
            arguments = Arrays.copyOfRange(items, 1, items.length);
        }
        return new Action(items[0], arguments);
    }

    public Action(@NonNull String action, String... arguments){
        mAction = action;
        mArguments = arguments;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(mAction);
        if(mArguments != null) {
            for (String value : mArguments) {
                builder.append(":");
                builder.append(value);
            }
        }
        return builder.toString();
    }
}
