package com.happy3w.toolkits.utils;

import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneId;

import static org.junit.Assert.*;

public class ZoneIdCacheTest {

    @Test
    public void should_return_default_value_with_wrong_zone_name() {
        ZoneId zoneId = ZoneIdCache.getZoneId("xxx", ZoneIdCache.BEIJING_ZONE_ID);
        Assert.assertEquals(ZoneIdCache.BEIJING_ZONE_ID, zoneId);
    }
}
