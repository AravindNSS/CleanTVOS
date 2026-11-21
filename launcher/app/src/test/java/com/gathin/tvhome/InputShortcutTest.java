package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.*;

public class InputShortcutTest {
    @Test public void recognisesTheNamedDeviceAcrossCommonLabels() {
        assertTrue(InputShortcut.isFireTv(null, "FireTV"));
        assertTrue(InputShortcut.isFireTv(null, "Amazon Fire TV Stick"));
        assertTrue(InputShortcut.isFireTv(null, "FIRE-TV · HDMI 2"));
    }

    @Test public void customLabelTakesPrecedenceOverProvider() {
        assertFalse(InputShortcut.isFireTv("Console", "Fire TV"));
        assertTrue(InputShortcut.isFireTv("Fire TV", "HDMI 2"));
        assertTrue(InputShortcut.isFireTv("", "Fire TV"));
    }

    @Test public void doesNotAssumeHdmiTwoIsAlwaysFireTv() {
        assertFalse(InputShortcut.isFireTv(null, "HDMI 2"));
        assertFalse(InputShortcut.isFireTv(null, "Game console"));
        assertFalse(InputShortcut.isFireTv(null, ""));
        assertFalse(InputShortcut.isFireTv(null, null));
    }
}
