package br.com.jacto.cloverindustryauth.controller.auth;

import br.com.jacto.cloverindustryauth.dto.auth.AuthRequestDto;
import br.com.jacto.cloverindustryauth.infra.security.JWTTokenDto;
import br.com.jacto.cloverindustryauth.infra.security.TokenService;
import br.com.jacto.cloverindustryauth.model.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Transactional
    @Operation(summary = "Realizar o login de um usuário",
            description = "Realiza a autenticação de um usuário e retorna um token JWT válido.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Token JWT gerado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JWTTokenDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Usuário ou senha inválidos"),
                    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
            }
    )
    public ResponseEntity logIn(@RequestBody @Valid AuthRequestDto dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new JWTTokenDto(tokenJWT));
    }

}
