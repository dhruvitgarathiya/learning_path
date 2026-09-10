package CrudOperations.demo.service.impl;

import CrudOperations.demo.GlobalExceptionHandler.ResourceNotFoundException;
import CrudOperations.demo.dto.ProductDTO;
import CrudOperations.demo.entity.Product;
import CrudOperations.demo.repositroy.ProductRepository;
import CrudOperations.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    private ProductRepository productRepository;

    private ProductDTO.ProductResponseDTO mapToResponse(Product product){
        return new ProductDTO.ProductResponseDTO(product.getId(), product.getName(), product.getPrice());
    }

    public List<ProductDTO.ProductResponseDTO> getAllProduct() {

        return productRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());

    }

    @Override
    public ProductDTO.ProductResponseDTO getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return mapToResponse(product);
    }

    @Override
    public ProductDTO.ProductResponseDTO addProduct(ProductDTO.ProductRequestDTO dto) {
        Product p = new Product();
        p.setName(dto.name());
        p.setPrice(dto.price());
       Product Saved = productRepository.save(p);
        return mapToResponse(Saved);
    }

    @Override
    public ProductDTO.ProductResponseDTO updateProduct(Long id, ProductDTO.ProductRequestDTO dto) {
        Product p = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Resource to be updated not found"));

        p.setPrice(dto.price());
        p.setName(dto.name());

        Product p1 = productRepository.save(p);
        return mapToResponse(p1);

    }

    @Override
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)){
            throw new ResourceNotFoundException("product does not exist");
        }
        productRepository.deleteById(id);
    }
}
