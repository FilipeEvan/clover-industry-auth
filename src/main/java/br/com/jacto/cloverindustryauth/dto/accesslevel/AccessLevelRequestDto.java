package br.com.jacto.cloverindustryauth.dto.accesslevel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLevelRequestDto {

    @NotBlank(message = "O nível de acesso não pode estar em branco")
    private String access_level;

}
