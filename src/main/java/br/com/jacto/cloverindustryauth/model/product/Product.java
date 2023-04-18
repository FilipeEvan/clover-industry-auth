package br.com.jacto.cloverindustryauth.model.product;

import br.com.jacto.cloverindustryauth.dto.product.ProductCreateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private double price;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String description;

    private LocalDateTime created_at;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Product(ProductCreateRequestDto dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.category = dto.getCategory();
        this.description = dto.getDescription();
        this.status = dto.getStatus();
    }
}
