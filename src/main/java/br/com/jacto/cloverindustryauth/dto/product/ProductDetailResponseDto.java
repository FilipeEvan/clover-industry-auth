package br.com.jacto.cloverindustryauth.dto.product;

import br.com.jacto.cloverindustryauth.model.product.Category;
import br.com.jacto.cloverindustryauth.model.product.Product;
import br.com.jacto.cloverindustryauth.model.product.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponseDto {
    private UUID id;
    private String name;
    private double price;
    private Category category;
    private String description;
    private LocalDateTime created_at;
    private Status status;

    public ProductDetailResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.category = product.getCategory();
        this.description = product.getDescription();
        this.created_at = product.getCreated_at();
        this.status = product.getStatus();
    }
}
