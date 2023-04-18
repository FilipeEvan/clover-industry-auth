package br.com.jacto.cloverindustryauth.dto.product;

import br.com.jacto.cloverindustryauth.model.product.Category;
import br.com.jacto.cloverindustryauth.model.product.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequestDto {
    @NotBlank(message = "O nome não pode estar em branco")
    private String name;
    @NotNull(message = "O preço não pode estar em branco")
    private double price;
    @NotNull
    private Category category;
    @NotBlank(message = "A descrição não pode estar em branco")
    private String description;
    @NotNull
    private Status status;
}
