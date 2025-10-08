package com.example.weather.model;

public class ForecastResponse {
    public double latitude;
    public double longitude;
    public Hourly hourly;

    public static class Hourly {
        public String[] time;
        public double[] temperature_2m;
    }
}

