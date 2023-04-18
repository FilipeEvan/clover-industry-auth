package br.com.jacto.cloverindustryauth.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {
    @NotBlank(message = "O e-mail não pode estar em branco")
    private String email;
    @NotBlank(message = "A senha não pode estar em branco")
    private String password;
}
