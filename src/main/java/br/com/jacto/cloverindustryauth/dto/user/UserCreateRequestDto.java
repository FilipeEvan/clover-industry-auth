package br.com.jacto.cloverindustryauth.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDto {
    @NotBlank(message = "O nome não pode estar em branco")
    private String name;
    @NotBlank(message = "O e-mail não pode estar em branco")
    private String email;
    @NotBlank(message = "A senha não pode estar em branco")
    private String password;
    @NotEmpty(message = "O nível de acesso não pode estar vazio")
    public List<UUID> access_levels;
}
