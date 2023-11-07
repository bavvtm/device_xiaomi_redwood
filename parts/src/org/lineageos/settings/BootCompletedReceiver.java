/*
 * Copyright (C) 2018 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.os.IBinder;
import android.view.Display;
import android.view.Display.HdrCapabilities;
import android.util.Log;

import org.lineageos.settings.dirac.DiracUtils;
import org.lineageos.settings.doze.PocketService;
import org.lineageos.settings.thermal.ThermalUtils;

public class BootCompletedReceiver extends BroadcastReceiver {
    private static final String TAG = "Xiaomi Parts";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d(TAG, "Received intent: " + intent.getAction());
        
        switch (intent.getAction()) {
            case Intent.ACTION_LOCKED_BOOT_COMPLETED:
                onLockedBootCompleted(context);
                break;
            case Intent.ACTION_BOOT_COMPLETED:
                onBootCompleted(context);
                break;
        }
    }

    private static void onLockedBootCompleted(Context context) {
        // Data is not accessible (user has just locked).
        PocketService.startService(context);

        // Override HDR types to enable Dolby Vision
        final DisplayManager dm = context.getSystemService(DisplayManager.class);
        dm.overrideHdrTypes(Display.DEFAULT_DISPLAY, new int[]{
                HdrCapabilities.HDR_TYPE_DOLBY_VISION, HdrCapabilities.HDR_TYPE_HDR10,
                HdrCapabilities.HDR_TYPE_HLG, HdrCapabilities.HDR_TYPE_HDR10_PLUS});
    }

    private static void onBootCompleted(Context context) {
        // Data is now accessible (user has just unlocked).
        DiracUtils.onBootCompleted(context);
        ThermalUtils.startService(context);
    }

}
