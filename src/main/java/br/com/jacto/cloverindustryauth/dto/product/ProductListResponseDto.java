package br.com.jacto.cloverindustryauth.dto.product;

import br.com.jacto.cloverindustryauth.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListResponseDto {
    private UUID id;
    private String name;
    private LocalDateTime created_at;

    public ProductListResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.created_at = product.getCreated_at();
    }

    public Product toProduct() {
        Product product = new Product();
        product.setId(this.id);
        product.setName(this.name);
        product.setCreated_at(this.created_at);
        return product;
    }
}
