package com.gathin.tvhome;

import java.util.Locale;

/** The shortcut follows the TV's named input, never an assumed vendor hardware-port number. */
final class InputShortcut {
    private InputShortcut() { }

    static boolean isFireTv(CharSequence custom, CharSequence provider) {
        CharSequence label = custom != null && custom.length() > 0 ? custom : provider;
        if (label == null) return false;
        String name = label.toString().toLowerCase(Locale.ROOT)
                .replace(" ", "").replace("-", "").replace("_", "");
        return name.contains("firetv");
    }
}
