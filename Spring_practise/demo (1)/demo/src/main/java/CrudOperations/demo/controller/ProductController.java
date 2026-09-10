package CrudOperations.demo.controller;

import CrudOperations.demo.dto.ProductDTO;
import CrudOperations.demo.entity.Product;
import CrudOperations.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private final ProductService productService;

    @GetMapping("/")
    public ResponseEntity<List<ProductDTO.ProductResponseDTO>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProduct());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO.ProductResponseDTO> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PostMapping("/")
    public ResponseEntity<ProductDTO.ProductResponseDTO> addProduct(@RequestBody ProductDTO.ProductRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO.ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO.ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
