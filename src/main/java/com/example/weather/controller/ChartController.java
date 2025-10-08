package com.example.weather.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/chart")
public class ChartController {

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> getChart(@RequestParam String path) {
        File f = new File(path);
        if (!f.exists()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new FileSystemResource(f));
    }
}

