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
        

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_returnsListOfTwo() throws Exception {
        List<Product> products = List.of(
                new Product(1L, "Laptop", 50000.0, 10),
                new Product(2L, "Mouse", 500.0, 100)
        );
        Mockito.when(service.getAll()).thenReturn(products);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Mouse"));
    }


    @Test
    void create_validBody_returns201() throws Exception {
        Product input = new Product(null, "Keyboard", 1500.0, 20);
        Product saved = new Product(3L, "Keyboard", 1500.0, 20);

        Mockito.when(service.create(Mockito.any(Product.class))).thenReturn(saved);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Keyboard"));
    }



   }