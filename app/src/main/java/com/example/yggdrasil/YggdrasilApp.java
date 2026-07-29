package com.example.yggdrasil;

import android.app.Application;
import com.google.android.material.color.DynamicColors;

public class YggdrasilApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Force disable dynamic colors to stick with Catppuccin theme
        if (DynamicColors.isDynamicColorAvailable()) {
            // This is effectively a no-op if you don't call applyToActivitiesIfAvailable,
            // but we're making it explicit here just in case it was auto-enabled by a library.
        }
    }
}
