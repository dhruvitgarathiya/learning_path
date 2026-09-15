package com.example.DailyPractiseSpringBoot.controller;

import com.example.DailyPractiseSpringBoot.entity.Product;
import com.example.DailyPractiseSpringBoot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) { };

    @GetMapping
    public List<Product> getAll() {  };

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) { };

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {  };

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {  };
}