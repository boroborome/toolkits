package com.happy3w.toolkits.manager;

import com.happy3w.toolkits.reflect.ReflectUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfigManager<DT, CT, ST extends AbstractConfigManager<DT, CT, ST>> {
    protected Map<Class, CT> configMap = new HashMap<>();

    @Getter
    @Setter
    protected IConfigDetector<CT> configDetector;

    public abstract CT findByType(Class<? extends DT> dataType);

    public void regist(Class<? extends DT> dataType, CT config) {
        configMap.put(dataType, config);
    }

    protected CT findByTypeStep(Class<?> dataType) {
        CT config = configMap.get(dataType);
        if (config == null && configDetector != null) {
            config = configDetector.detectConfig(dataType);
            if (config != null) {
                configMap.put(dataType, config);
            }
        }
        return config;
    }

    public ST newCopy() {
        AbstractConfigManager<DT, CT, ST> newManager = ReflectUtil.newInstance(this.getClass());
        newManager.configMap.putAll(configMap);
        return (ST) newManager;
    }
}
