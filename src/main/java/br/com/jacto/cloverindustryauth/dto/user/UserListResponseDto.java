package br.com.jacto.cloverindustryauth.dto.user;

import br.com.jacto.cloverindustryauth.model.product.Product;
import br.com.jacto.cloverindustryauth.model.product.Status;
import br.com.jacto.cloverindustryauth.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponseDto {
    private UUID id;
    private String name;
    private LocalDateTime created_at;

    public UserListResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.created_at = user.getCreated_at();
    }
}
