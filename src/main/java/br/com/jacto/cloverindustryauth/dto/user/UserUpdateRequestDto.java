package br.com.jacto.cloverindustryauth.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {
    private String name;
    private String email;
    private String password;
    @NotEmpty(message = "O nível de acesso não pode estar vazio")
    public List<UUID> access_levels;
}
