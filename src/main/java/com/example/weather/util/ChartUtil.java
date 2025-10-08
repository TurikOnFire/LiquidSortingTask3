package com.example.weather.util;

import com.example.weather.model.ForecastResponse;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries;

import java.io.File;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ChartUtil {

    public static String createHourlyTemperatureChart(ForecastResponse forecast, String city, String outDir) {
        if (forecast == null || forecast.hourly == null || forecast.hourly.time == null) return null;
        String[] times = forecast.hourly.time;
        double[] temps = forecast.hourly.temperature_2m;

        int len = Math.min(times.length, 24);
        List<String> labels = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        DateTimeFormatter outFmt = DateTimeFormatter.ofPattern("HH:mm");
        for (int i = 0; i < len; i++) {
            try {
                OffsetDateTime odt = OffsetDateTime.parse(times[i]);
                labels.add(outFmt.format(odt));
            } catch (Exception e) {
                labels.add(times[i]);
            }
            values.add(temps[i]);
        }

        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(400)
                .title("Hourly temperature for " + city)
                .xAxisTitle("Hour")
                .yAxisTitle("Temp °C")
                .build();

        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setAxisTitlesVisible(true);
        chart.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);

        chart.addSeries("temp", labels, values);

        try {
            File d = new File(outDir);
            if (!d.exists()) d.mkdirs();
            String fileName = String.format("%s/%s-%d.png", outDir, city.replaceAll("\\s+", "_"), System.currentTimeMillis());
            BitmapEncoder.saveBitmap(chart, fileName, BitmapEncoder.BitmapFormat.PNG);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

