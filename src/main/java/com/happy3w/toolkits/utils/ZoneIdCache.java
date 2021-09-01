package com.happy3w.toolkits.utils;

import com.happy3w.java.ext.StringUtils;
import lombok.AllArgsConstructor;

import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZoneIdCache {
    private  ZoneIdCache() {

    }

    private static Map<String, ZoneIdResult> zoneIdMap = new ConcurrentHashMap<>();

    public static final ZoneId BEIJING_ZONE_ID = getZoneId("+08:00");

    public static final ZoneId LOCAL_TIME_HIDDEN_TIMEZONE = BEIJING_ZONE_ID;

    public static ZoneId getZoneId(String zoneIdStr, ZoneId defaultZoneId) {
        try {
            return getZoneId(zoneIdStr);
        } catch (ZoneRulesException exp) {
            return defaultZoneId;
        }
    }

    public static ZoneId getZoneId(String zoneIdStr) {
        if (StringUtils.isEmpty(zoneIdStr)) {
            return null;
        }

        ZoneIdResult result = zoneIdMap.computeIfAbsent(zoneIdStr,
                key -> new ZoneIdResult(ZoneId.of(key).normalized()));
        return result.zoneId;
    }

    @AllArgsConstructor
    private static class ZoneIdResult {
        ZoneId zoneId;
    }
}
