package br.com.jacto.cloverindustryauth.dto.product;

import br.com.jacto.cloverindustryauth.model.product.Category;
import br.com.jacto.cloverindustryauth.model.product.Status;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequestDto {
    private String name;
    private double price;
    private Category category;
    private String description;
    private Status status;
}
