package com.example.weather.model;

public class GeoResponse {
    public GeoResult[] results;

    public static class GeoResult {
        public String name;
        public String country;
        public double latitude;
        public double longitude;
    }
}

