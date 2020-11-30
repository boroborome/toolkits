package com.happy3w.toolkits.manager;

import com.happy3w.toolkits.reflect.ReflectUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTypeProcessorManager<DT, PT, ST extends AbstractTypeProcessorManager<DT, PT, ST>> {
    protected Map<Class<? extends DT>, PT> processMap = new HashMap<>();

    public void registProcess(Class<? extends DT> dataType, PT processor) {
        processMap.put(dataType, processor);
    }

    public PT findProcessByType(Class<? extends DT> dataType) {
        return processMap.getOrDefault(dataType, null);
    }

    public PT findProcessByInheritType(Class<? extends DT> dataType) {
        for (Map.Entry<Class<? extends DT>, PT> entry : processMap.entrySet()) {
            if (entry.getKey().isAssignableFrom(dataType)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public ST newCopy() {
        AbstractTypeProcessorManager<DT, PT, ST> newManager = ReflectUtil.newInstance(this.getClass());
        newManager.processMap.putAll(processMap);
        return (ST) newManager;
    }
}
