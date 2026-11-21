package com.gathin.tvhome;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BannerArtTest {
    @Test public void samplesLargeArtworkBeforeAllocatingPixels() {
        assertEquals(8, BannerArt.sampleSize(3840, 320));
        assertEquals(4, BannerArt.sampleSize(1920, 320));
        assertEquals(2, BannerArt.sampleSize(640, 320));
    }

    @Test public void retainsSmallArtworkWithoutUpscaling() {
        assertEquals(1, BannerArt.sampleSize(320, 320));
        assertEquals(1, BannerArt.sampleSize(160, 320));
        assertEquals(1, BannerArt.sampleSize(0, 320));
        assertEquals(1, BannerArt.sampleSize(640, 0));
    }
}
