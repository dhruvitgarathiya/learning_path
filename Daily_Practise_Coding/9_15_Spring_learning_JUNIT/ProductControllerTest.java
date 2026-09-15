package com.example.DailyPractiseSpringBoot;

import com.example.DailyPractiseSpringBoot.controller.ProductController;
import com.example.DailyPractiseSpringBoot.entity.Product;
import com.example.DailyPractiseSpringBoot.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getById_found_returns200() throws Exception {
        Product product = new Product(1L, "Laptop", 50000.0, 10);
        Mockito.when(service.getById(1L)).thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(50000.0));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        Mockito.when(service.getById(99L)).thenReturn(null);
        // OR: Mockito.when(service.getById(99L)).thenThrow(new ProductNotFoundException())
        // depends on your controller's null-handling logic

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

   }