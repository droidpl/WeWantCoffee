package com.github.droidpl.android.syncommand;

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
        return new Action(items[0], Arrays.copyOfRange(items, 1, items.length));
    }

    public Action(String action, String[] arguments){
        mAction = action;
        mArguments = arguments;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(mAction);
        for(String value: mArguments){
            builder.append(":");
            builder.append(value);
        }
        return builder.toString();
    }
}
