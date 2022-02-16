package com.happy3w.toolkits.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;

public class ZoneIdCacheTest {

    @Test
    public void should_return_default_value_with_wrong_zone_name() {
        ZoneId zoneId = ZoneIdCache.getZoneId("xxx", ZoneIdCache.BEIJING_ZONE_ID);
        Assertions.assertEquals(ZoneIdCache.BEIJING_ZONE_ID, zoneId);
    }
}
