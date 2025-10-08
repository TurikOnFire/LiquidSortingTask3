package com.example.weather.cache;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCache {
    private static class Entry {
        Object value;
        Instant expiresAt;
        Entry(Object v, Instant e) { value = v; expiresAt = e; }
    }

    private final Map<String, Entry> map = new ConcurrentHashMap<>();

    public void put(String key, Object value, long ttlSeconds) {
        map.put(key, new Entry(value, Instant.now().plusSeconds(ttlSeconds)));
    }

    public Object get(String key) {
        Entry e = map.get(key);
        if (e == null) return null;
        if (Instant.now().isAfter(e.expiresAt)) {
            map.remove(key);
            return null;
        }
        return e.value;
    }
}

