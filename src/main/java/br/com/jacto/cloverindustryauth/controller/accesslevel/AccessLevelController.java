package br.com.jacto.cloverindustryauth.controller.accesslevel;

import br.com.jacto.cloverindustryauth.dto.accesslevel.AccessLevelRequestDto;
import br.com.jacto.cloverindustryauth.dto.accesslevel.AccessLevelResponseDto;
import br.com.jacto.cloverindustryauth.dto.user.UserCreateRequestDto;
import br.com.jacto.cloverindustryauth.dto.user.UserDetailResponseDto;
import br.com.jacto.cloverindustryauth.dto.user.UserListResponseDto;
import br.com.jacto.cloverindustryauth.dto.user.UserUpdateRequestDto;
import br.com.jacto.cloverindustryauth.service.accesslevel.AccessLevelService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("access-levels")
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("hasAuthority('ADMIN')")
public class AccessLevelController {

    @Autowired
    private AccessLevelService accessLevelService;

    @PostMapping
    @Transactional
    @Operation(summary = "Criar um novo nível de acesso",
            description = "Cria um novo nível de acesso com base nas informação fornecida.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccessLevelRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Nível de acesso criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccessLevelResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity postAccessLevel(@RequestBody @Valid AccessLevelRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accessLevelService.postAccessLevel(dto));
    }

    @GetMapping
    @Operation(summary = "Retornar todos os níveis de acesso",
            description = "Retorna uma lista paginada de todos os níveis de acesso cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Lista de níveis de acesso retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccessLevelResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<Page<AccessLevelResponseDto>> getAllAccessLevels(Pageable pageable) {
        return ResponseEntity.ok(accessLevelService.getAllAccessLevels(pageable));
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualizar um nível de acesso pelo ID",
            description = "Atualiza um nível de acesso com base no ID fornecido e na informação fornecida.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccessLevelRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Nível de acesso atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccessLevelResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Nível de acesso não encontrado")
            }
    )
    public ResponseEntity putAccessLevelById(@PathVariable UUID id,
                                         @RequestBody @Valid AccessLevelRequestDto dto) {
        return ResponseEntity.ok(accessLevelService.putAccessLevelById(id, dto));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Excluir um nível de acesso pelo ID",
            description = "Exclui um nível de acesso com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Nível de acesso excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Nível de acesso não encontrado")
            }
    )
    public ResponseEntity deleteAccessLevelById(@PathVariable UUID id) {
        accessLevelService.deleteAccessLevelById(id);
        return ResponseEntity.noContent().build();
    }
}
