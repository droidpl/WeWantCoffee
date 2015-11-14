package com.github.droidpl.android.syncommand.model;

/**
 * Created by rob on 14/11/15.
 */
public class SoundItem {

    public int iconResId;
    public String name;
    public int soundResId;

    public SoundItem(int iconResId, String name, int soundResId) {
        this.iconResId = iconResId;
        this.name = name;
        this.soundResId = soundResId;
    }
}
