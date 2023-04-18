package br.com.jacto.cloverindustryauth.controller.product;

import br.com.jacto.cloverindustryauth.dto.product.ProductCreateRequestDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductDetailResponseDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductListResponseDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductUpdateRequestDto;
import br.com.jacto.cloverindustryauth.service.product.ProductService;
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
@RequestMapping("products")
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('COLLABORATOR')")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping
    @Transactional
    @Operation(summary = "Criar um novo produto",
            description = "Cria um novo produto com base nas informações fornecidas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductCreateRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Produto criado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDetailResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity postProduct(@RequestBody @Valid ProductCreateRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.postProduct(dto));
    }

    @GetMapping
    @Operation(summary = "Retornar todos os produtos",
            description = "Retorna uma lista paginada de todos os produtos cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Lista de produtos retornada com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductListResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<Page<ProductListResponseDto>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retornar um produto pelo ID",
            description = "Retorna um produto com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Produto retornado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDetailResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    public ResponseEntity getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualizar um produto pelo ID",
            description = "Atualiza um produto com base no ID fornecido e nas informações fornecidas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductUpdateRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Produto atualizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProductDetailResponseDto.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    public ResponseEntity putProductById(@PathVariable UUID id,
                                         @RequestBody @Valid ProductUpdateRequestDto dto) {
        return ResponseEntity.ok(productService.putProductById(id, dto));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Excluir um produto pelo ID",
            description = "Exclui um produto com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado")
            }
    )
    public ResponseEntity deleteProductById(@PathVariable UUID id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
