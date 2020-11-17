package com.pengyeah.switchbutton.utils;

import android.graphics.Bitmap;

public class BitmapUtils {

    private BitmapUtils() {

    }

    public static Bitmap replacePixelColor(Bitmap origBm, int color) {
        Bitmap copyBm = origBm.copy(Bitmap.Config.ARGB_8888, true);
        final int w = copyBm.getWidth();
        final int h = copyBm.getHeight();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int c = copyBm.getPixel(i, j);
                if (c != 0) {
                    final int alpha = (c >> 24) & 0xff;
                    if (alpha > 0 && alpha != 0xff) {
                        color = (alpha << 24) | (color & 0xffffff);
                    }
                    copyBm.setPixel(i, j, (alpha << 24) | color);
                }
            }
        }
        origBm = copyBm;
        return origBm;
    }
}
