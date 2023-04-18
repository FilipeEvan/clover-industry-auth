package br.com.jacto.cloverindustryauth.infra.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTTokenDto {
    private String token;
}
