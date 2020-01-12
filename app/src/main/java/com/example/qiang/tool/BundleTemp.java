package com.example.qiang.tool;

import android.os.Bundle;

public class BundleTemp {

    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";


    public static Bundle newInstanceBundle(long noteId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_NOTE_ID, noteId);
        return bundle;
    }

    public static Bundle newInstanceBundle(long noteId,long meetingId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_NOTE_ID, noteId);
        bundle.putLong("meetingId", meetingId);
        return bundle;
    }


    public static Bundle meetingBundle(long meetingId) {
        Bundle bundle = new Bundle();
        bundle.putLong("meetingId", meetingId);
        return bundle;
    }

    public static Bundle ThemeIdBundle(long themeId) {
        Bundle bundle = new Bundle();
        bundle.putLong("themeId", themeId);
        return bundle;
    }

    public static Bundle ThemeIdBundle(long themeId,String theme) {
        Bundle bundle = new Bundle();
        bundle.putLong("themeId", themeId);
        bundle.putString("theme", theme);
        return bundle;
    }
}
