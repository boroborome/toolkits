package com.happy3w.toolkits.manager;

public class ConfigManager<CT> extends AbstractConfigManager<CT, ConfigManager<CT>> {

    public static <CT> ConfigManager<CT> inherit() {
        return new ConfigManager<CT>()
                .finder((IConfigFinder<CT>) InheritFinder.INSTANCE);
    }
}
