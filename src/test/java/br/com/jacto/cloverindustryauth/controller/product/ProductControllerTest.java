package br.com.jacto.cloverindustryauth.controller.product;

import br.com.jacto.cloverindustryauth.ValidationException;
import br.com.jacto.cloverindustryauth.dto.product.ProductCreateRequestDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductDetailResponseDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductListResponseDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductUpdateRequestDto;
import br.com.jacto.cloverindustryauth.model.product.Category;
import br.com.jacto.cloverindustryauth.model.product.Product;
import br.com.jacto.cloverindustryauth.model.product.Status;
import br.com.jacto.cloverindustryauth.service.product.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações estão inválidas")
    @WithMockUser(authorities = {"ADMIN", "COLLABORATOR"})
    @Transactional
    void postProductScenario1() throws Exception {
        var response = mvc.perform(post("/products"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações estão válidas")
    @WithMockUser(authorities = {"ADMIN", "COLLABORATOR"})
    @Transactional
    void postProductScenario2() throws Exception {
        ProductCreateRequestDto requestDto = new ProductCreateRequestDto();
        requestDto.setName("Product Test");
        requestDto.setPrice(9.99);
        requestDto.setCategory(Category.MANUAL);
        requestDto.setDescription("Test product for unit test");
        requestDto.setStatus(Status.FORA_DE_LINHA);

        Product product = new Product(requestDto);
        product.setId(UUID.randomUUID());
        product.setCreated_at(LocalDateTime.now());

        ProductDetailResponseDto responseDto = new ProductDetailResponseDto(product);

        when(productService.postProduct(any(ProductCreateRequestDto.class)))
                .thenReturn(responseDto);

        // Realiza a requisição POST para criar um novo produto
        mvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(product.getId().toString()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.category").value(product.getCategory().toString()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.status").value(product.getStatus().toString()));
    }

    @Test
    @DisplayName("Deveria retornar código http 200 e lista de produtos")
    @WithMockUser(authorities = {"ADMIN", "COLLABORATOR"})
    void getAllProductsScenario1() throws Exception {
        Product product1 = new Product();
        product1.setId(UUID.randomUUID());
        product1.setName("Product 1");
        product1.setPrice(10.99);
        product1.setCategory(Category.MANUAL);
        product1.setDescription("Description 1");
        product1.setStatus(Status.FORA_DE_LINHA);
        product1.setCreated_at(LocalDateTime.of(2023, 1, 30, 12, 0, 0));

        Product product2 = new Product();
        product2.setId(UUID.randomUUID());
        product2.setName("Product 2");
        product2.setPrice(20.99);
        product2.setCategory(Category.COMBUSTÍVEL);
        product2.setDescription("Description 2");
        product2.setStatus(Status.DISPONÍVEL);
        product2.setCreated_at(LocalDateTime.of(2023, 1, 31, 12, 0, 0));

        Page<ProductListResponseDto> responseDtoPage = new PageImpl<>(
                Arrays.asList(
                        new ProductListResponseDto(product1),
                        new ProductListResponseDto(product2)
                )
        );

        when(productService.getAllProducts(any(Pageable.class)))
                .thenReturn(responseDtoPage);

        // Act and Assert
        mvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(product1.getId().toString()))
                .andExpect(jsonPath("$.content[0].name").value(product1.getName()))
                .andExpect(jsonPath("$.content[0].created_at").value("2023-01-30T12:00:00"))
                .andExpect(jsonPath("$.content[1].id").value(product2.getId().toString()))
                .andExpect(jsonPath("$.content[1].name").value(product2.getName()))
                .andExpect(jsonPath("$.content[1].created_at").value("2023-01-31T12:00:00"));
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e informações do produto quando o ID é válido")
    @WithMockUser(authorities = {"ADMIN", "COLLABORATOR"})
    void getProductByIdScenario1() throws Exception {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Product Test");
        product.setPrice(9.99);
        product.setCategory(Category.MANUAL);
        product.setDescription("Test product for unit test");
        product.setStatus(Status.FORA_DE_LINHA);
        product.setCreated_at(LocalDateTime.now());

        when(productService.getProductById(productId))
                .thenReturn(new ProductDetailResponseDto(product));

        mvc.perform(get("/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.category").value(product.getCategory().toString()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.status").value(product.getStatus().toString()));
    }

    @Test
    @DisplayName("Deveria devolver código http 404 quando o id não existe")
    @WithMockUser(authorities = {"ADMIN", "COLLABORATOR"})
    void getProductByIdScenario2() throws Exception {
        UUID productId = UUID.randomUUID();

        when(productService.getProductById(productId))
                .thenThrow(new ValidationException("Produto não encontrado"));

        mvc.perform(get("/products/" + productId))
                    .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deveria atualizar um produto existente e retornar código http 200")
    @WithMockUser(authorities = {"ADMIN", "COLLABORATOR"})
    @Transactional
    void putProductByIdScenario1() throws Exception {
        // Cria um produto existente para ser atualizado
        Product existingProduct = new Product();
        existingProduct.setId(UUID.randomUUID());
        existingProduct.setName("Old product");
        existingProduct.setPrice(9.99);
        existingProduct.setCategory(Category.MANUAL);
        existingProduct.setDescription("Product update test");
        existingProduct.setStatus(Status.FORA_DE_LINHA);
        existingProduct.setCreated_at(LocalDateTime.of(2023, 1, 30, 12, 0, 0));

        // Define o ID do produto existente a ser atualizado
        UUID productId = existingProduct.getId();

        // Cria uma requisição DTO de atualização de produto
        ProductUpdateRequestDto requestDto = new ProductUpdateRequestDto();
        requestDto.setName("New product");
        requestDto.setPrice(19.99);
        requestDto.setCategory(Category.MANUAL);
        requestDto.setDescription("Product updated successfully");
        requestDto.setStatus(Status.FORA_DE_LINHA);

        // Cria um produto atualizado para ser retornado pelo serviço
        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName(requestDto.getName());
        updatedProduct.setPrice(requestDto.getPrice());
        updatedProduct.setCategory(requestDto.getCategory());
        updatedProduct.setDescription(requestDto.getDescription());
        updatedProduct.setStatus(requestDto.getStatus());
        updatedProduct.setCreated_at(existingProduct.getCreated_at());

        // Define a resposta DTO que será retornada pelo serviço
        ProductDetailResponseDto responseDto = new ProductDetailResponseDto(updatedProduct);

        // Configura o mock do serviço para retornar o produto atualizado
        when(productService.putProductById(eq(productId), any(ProductUpdateRequestDto.class)))
                .thenReturn(responseDto);

        // Realiza a requisição PUT para atualizar o produto
        mvc.perform(put("/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedProduct.getId().toString()))
                .andExpect(jsonPath("$.name").value(updatedProduct.getName()))
                .andExpect(jsonPath("$.price").value(updatedProduct.getPrice()))
                .andExpect(jsonPath("$.category").value(updatedProduct.getCategory().toString()))
                .andExpect(jsonPath("$.description").value(updatedProduct.getDescription()))
                .andExpect(jsonPath("$.created_at").value("2023-01-30T12:00:00"))
                .andExpect(jsonPath("$.status").value(updatedProduct.getStatus().toString()));
    }

    @Test
    @DisplayName("Deveria deletar um produto e retornar código http 204")
    @WithMockUser(authorities = {"ADMIN", "COLLABORATOR"})
    @Transactional
    void deleteProductByIdScenario1() throws Exception {
        // Cria um produto para deletar
        ProductCreateRequestDto requestDto = new ProductCreateRequestDto();
        requestDto.setName("Product Test");
        requestDto.setPrice(9.99);
        requestDto.setCategory(Category.MANUAL);
        requestDto.setDescription("Test product for unit test");
        requestDto.setStatus(Status.FORA_DE_LINHA);

        Product product = new Product(requestDto);
        product.setId(UUID.randomUUID());
        product.setCreated_at(LocalDateTime.now());

        ProductDetailResponseDto responseDto = new ProductDetailResponseDto(product);

        when(productService.postProduct(any(ProductCreateRequestDto.class))).thenReturn(responseDto);

        ProductDetailResponseDto createdProduct = objectMapper.readValue(
                mvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(),
                ProductDetailResponseDto.class
        );

        // Deleta o produto
        mvc.perform(delete("/products/" + createdProduct.getId()))
                .andExpect(status().isNoContent());
    }

}