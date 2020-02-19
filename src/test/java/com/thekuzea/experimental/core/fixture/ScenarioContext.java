package com.thekuzea.experimental.core.fixture;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioContext {

    private final Map<DataKeys, Object> data = new HashMap<>();

    public void clean() {
        data.clear();
    }

    public void save(final DataKeys key, final Object value) {
        data.put(key, value);
    }

    public <T> T getDataByKey(final DataKeys key, final Class<T> clazz) {
        return clazz.cast(data.get(key));
    }
}
