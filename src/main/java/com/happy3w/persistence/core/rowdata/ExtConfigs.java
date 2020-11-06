package com.happy3w.persistence.core.rowdata;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ExtConfigs {
    private Map<Class<? extends IRdConfig>, IRdConfig> configs;

    public ExtConfigs() {
        configs = new HashMap<>();
    }

    public ExtConfigs(Map<Class<? extends IRdConfig>, IRdConfig> configs) {
        this.configs = configs;
    }

    public <T extends IRdConfig> T getConfig(Class<T> configType) {
        return (T) configs.get(configType);
    }

    public <T extends IRdConfig> void save(T config) {
        configs.put(config.getClass(), config);
    }

    public void merge(ExtConfigs otherExtConfigs) {
        configs.putAll(otherExtConfigs.configs);
    }
}
