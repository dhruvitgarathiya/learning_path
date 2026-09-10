package CrudOperations.demo.dto;

public class ProductDTO {

 public record ProductResponseDTO(
         Long id,
         String name,
         String price
 ){}


    public record ProductRequestDTO(
            String name,
            String price
    ){}



}
