package br.com.jacto.cloverindustryauth.controller.user;

import br.com.jacto.cloverindustryauth.dto.auth.AuthRequestDto;
import br.com.jacto.cloverindustryauth.dto.user.UserCreateRequestDto;
import br.com.jacto.cloverindustryauth.dto.user.UserDetailResponseDto;
import br.com.jacto.cloverindustryauth.dto.user.UserListResponseDto;
import br.com.jacto.cloverindustryauth.dto.user.UserUpdateRequestDto;
import br.com.jacto.cloverindustryauth.infra.security.JWTTokenDto;
import br.com.jacto.cloverindustryauth.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("users")
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Transactional
    @Operation(summary = "Criar um novo usuário",
            description = "Cria um novo usuário com base nas informações fornecidas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserCreateRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Usuário criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDetailResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity postUser(@RequestBody @Valid UserCreateRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.postUser(dto));
    }

    @GetMapping
    @Operation(summary = "Retornar todos os usuários",
            description = "Retorna uma lista paginada de todos os usuários cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Lista de usuários retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserListResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<Page<UserListResponseDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retornar um usuário pelo ID",
            description = "Retorna um usuário com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Usuário retornado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDetailResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    public ResponseEntity getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualizar um usuário pelo ID",
            description = "Atualiza um usuário com base no ID fornecido e nas informações fornecidas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserUpdateRequestDto.class)
            )
    ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Usuário atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDetailResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    public ResponseEntity putUserById(@PathVariable UUID id,
                                         @RequestBody @Valid UserUpdateRequestDto dto) {
        return ResponseEntity.ok(userService.putUserById(id, dto));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Excluir um usuário pelo ID",
            description = "Exclui um usuário com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    public ResponseEntity deleteUserById(@PathVariable UUID id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
