package com.happy3w.persistence.core.rowdata;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

    public <T extends IRdConfig> ExtConfigs regist(T config) {
        configs.put(config.getClass(), config);
        return this;
    }

    public void merge(ExtConfigs otherExtConfigs) {
        configs.putAll(otherExtConfigs.configs);
    }

    public String createContentKey() {
        if (configs.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        ArrayList<Map.Entry<Class<? extends IRdConfig>, IRdConfig>> configList = new ArrayList<>(configs.entrySet());
        configList.sort(Comparator.comparing(e -> e.getKey().getTypeName()));
        for (Map.Entry<Class<? extends IRdConfig>, IRdConfig> entry : configList) {
            builder.append(entry.getKey().getTypeName())
                    .append(":");
            entry.getValue().buildContentKey(builder);
            builder.append(',');
        }
        return builder.toString();
    }

    public static ExtConfigs mergeArray(ExtConfigs... configs) {
        return mergeAll(Arrays.asList(configs));
    }

    public static ExtConfigs mergeAll(List<ExtConfigs> configs) {
        ExtConfigs resultConfigs = new ExtConfigs();
        for (int i = configs.size() - 1; i >= 0; i--) {
            ExtConfigs config = configs.get(i);
            if (config == null) {
                continue;
            }
            resultConfigs.merge(config);
        }
        return resultConfigs;
    }
}
