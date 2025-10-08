package com.example.weather.service;

import com.example.weather.model.ForecastResponse;
import com.example.weather.model.GeoResponse;
import com.example.weather.util.ChartUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private static final String GEO_URL = "https://geocoding-api.open-meteo.com/v1/search?name={city}";
    private static final String WEATHER_URL =
            "https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lon}&hourly=temperature_2m&timezone=auto";

    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, CacheEntry> cache = new HashMap<>();

    public Map<String, Object> getWeather(String city) {
        CacheEntry entry = cache.get(city.toLowerCase());
        if (entry != null && !entry.isExpired()) {
            return entry.data;
        }

        GeoResponse geo = restTemplate.getForObject(GEO_URL, GeoResponse.class, city);
        if (geo == null || geo.results == null || geo.results.length == 0) {
            throw new RuntimeException("City not found: " + city);
        }

        double lat = geo.results[0].latitude;
        double lon = geo.results[0].longitude;
        String resolvedName = geo.results[0].name;

        ForecastResponse forecast = restTemplate.getForObject(WEATHER_URL, ForecastResponse.class, lat, lon);
        if (forecast == null || forecast.hourly == null || forecast.hourly.temperature_2m == null) {
            throw new RuntimeException("Invalid weather response for: " + city);
        }

        String chartPath = ChartUtil.createHourlyTemperatureChart(
                forecast, resolvedName, Paths.get("charts").toAbsolutePath().toString()
        );

        Map<String, Object> resp = new HashMap<>();
        resp.put("city", resolvedName);
        resp.put("latitude", lat);
        resp.put("longitude", lon);
        resp.put("temperatures", forecast.hourly.temperature_2m);
        resp.put("time", forecast.hourly.time);
        resp.put("chartPath", chartPath);

        cache.put(city.toLowerCase(), new CacheEntry(resp, 15 * 60_000));

        return resp;
    }

    private static class CacheEntry {
        final Map<String, Object> data;
        final long expireAt;

        CacheEntry(Map<String, Object> data, long ttlMillis) {
            this.data = data;
            this.expireAt = System.currentTimeMillis() + ttlMillis;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expireAt;
        }
    }
}
