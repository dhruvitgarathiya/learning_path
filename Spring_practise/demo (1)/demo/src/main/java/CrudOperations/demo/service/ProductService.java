package CrudOperations.demo.service;

import CrudOperations.demo.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductService {
    public  List<ProductDTO.ProductResponseDTO> getAllProduct() ;
    public ProductDTO.ProductResponseDTO getProduct(Long id);

    public ProductDTO.ProductResponseDTO addProduct(ProductDTO.ProductRequestDTO dto);

    ProductDTO.ProductResponseDTO updateProduct(Long id, ProductDTO.ProductRequestDTO dto);

    void deleteProduct(Long id);
}
