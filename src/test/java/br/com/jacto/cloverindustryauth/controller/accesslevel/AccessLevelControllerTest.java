package br.com.jacto.cloverindustryauth.controller.accesslevel;

import br.com.jacto.cloverindustryauth.dto.accesslevel.AccessLevelRequestDto;
import br.com.jacto.cloverindustryauth.dto.accesslevel.AccessLevelResponseDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductCreateRequestDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductDetailResponseDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductListResponseDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductUpdateRequestDto;
import br.com.jacto.cloverindustryauth.model.accesslevel.AccessLevel;
import br.com.jacto.cloverindustryauth.model.product.Category;
import br.com.jacto.cloverindustryauth.model.product.Product;
import br.com.jacto.cloverindustryauth.model.product.Status;
import br.com.jacto.cloverindustryauth.service.accesslevel.AccessLevelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class AccessLevelControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccessLevelService accessLevelService;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações estão inválidas")
    @WithMockUser(authorities = "ADMIN")
    @Transactional
    void postAccessLevelScenario1() throws Exception {
        var response = mvc.perform(post("/access-levels"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações estão válidas")
    @WithMockUser(authorities = "ADMIN")
    @Transactional
    void postAccessLevelScenario2() throws Exception {
        AccessLevelRequestDto requestDto = new AccessLevelRequestDto();
        requestDto.setAccess_level("ACCESS_LEVEL_TEST");

        AccessLevel accessLevel = new AccessLevel(requestDto);
        accessLevel.setId(UUID.randomUUID());

        AccessLevelResponseDto responseDto = new AccessLevelResponseDto(accessLevel);

        when(accessLevelService.postAccessLevel(any(AccessLevelRequestDto.class)))
                .thenReturn(responseDto);

        // Realiza a requisição POST para criar um novo nível de acesso
        mvc.perform(post("/access-levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(accessLevel.getId().toString()))
                .andExpect(jsonPath("$.access_level").value(accessLevel.getAccess_level()));
    }

    @Test
    @DisplayName("Deveria retornar código http 200 e lista de níveis de acesso")
    @WithMockUser(authorities = "ADMIN")
    void getAllAccessLevelsScenario1() throws Exception {
        AccessLevel accessLevel1 = new AccessLevel();
        accessLevel1.setId(UUID.randomUUID());
        accessLevel1.setAccess_level("ACCESS_LEVEL_1");

        AccessLevel accessLevel2 = new AccessLevel();
        accessLevel2.setId(UUID.randomUUID());
        accessLevel2.setAccess_level("ACCESS_LEVEL_2");

        Page<AccessLevelResponseDto> expectedResponse = new PageImpl<>(
                Arrays.asList(
                        new AccessLevelResponseDto(accessLevel1),
                        new AccessLevelResponseDto(accessLevel2)
                )
        );

        when(accessLevelService.getAllAccessLevels(any(Pageable.class)))
                .thenReturn(expectedResponse);

        mvc.perform(get("/access-levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "access_level")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(accessLevel1.getId().toString()))
                .andExpect(jsonPath("$.content[0].access_level").value(accessLevel1.getAccess_level()))
                .andExpect(jsonPath("$.content[1].id").value(accessLevel2.getId().toString()))
                .andExpect(jsonPath("$.content[1].access_level").value(accessLevel2.getAccess_level()));
    }

    @Test
    @DisplayName("Deveria atualizar um nível de acesso existente e retornar código http 200")
    @WithMockUser(authorities = "ADMIN")
    @Transactional
    void putAccessLevelByIdScenario1() throws Exception {
        // Cria um produto existente para ser atualizado
        AccessLevel existingAccessLevel = new AccessLevel();
        existingAccessLevel.setId(UUID.randomUUID());
        existingAccessLevel.setAccess_level("OLD_ACCESS_LEVEL");

        // Define o ID do nível de acesso existente a ser atualizado
        UUID accessLevelId = existingAccessLevel.getId();

        // Cria uma requisição DTO de atualização de nível de acesso
        AccessLevelRequestDto requestDto = new AccessLevelRequestDto();
        requestDto.setAccess_level("NEW_ACCESS_LEVEL");

        // Cria um nível de acesso atualizado para ser retornado pelo serviço
        AccessLevel updatedAccessLevel = new AccessLevel();
        updatedAccessLevel.setId(accessLevelId);
        updatedAccessLevel.setAccess_level(requestDto.getAccess_level());

        // Define a resposta DTO que será retornada pelo serviço
        AccessLevelResponseDto responseDto = new AccessLevelResponseDto(updatedAccessLevel);

        // Configura o mock do serviço para retornar o nível de acesso atualizado
        when(accessLevelService.putAccessLevelById(eq(accessLevelId), any(AccessLevelRequestDto.class)))
                .thenReturn(responseDto);

        // Realiza a requisição PUT para atualizar o nível de acesso
        mvc.perform(put("/access-levels/" + accessLevelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedAccessLevel.getId().toString()))
                .andExpect(jsonPath("$.access_level").value(updatedAccessLevel.getAccess_level()));
    }

    @Test
    @DisplayName("Deveria deletar um nível de acesso e retornar código http 204")
    @WithMockUser(authorities = {"ADMIN"})
    @Transactional
    void deleteAccessLevelByIdScenario1() throws Exception {
        AccessLevelRequestDto requestDto = new AccessLevelRequestDto();
        requestDto.setAccess_level("ACCESS_LEVEL_TEST");

        AccessLevel accessLevel = new AccessLevel(requestDto);
        accessLevel.setId(UUID.randomUUID());

        AccessLevelResponseDto responseDto = new AccessLevelResponseDto(accessLevel);

        when(accessLevelService.postAccessLevel(any(AccessLevelRequestDto.class))).thenReturn(responseDto);

        AccessLevelResponseDto createdAccessLevel = objectMapper.readValue(
                mvc.perform(post("/access-levels")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(),
                AccessLevelResponseDto.class
        );

        // Deleta o produto
        mvc.perform(delete("/access-levels/" + createdAccessLevel.getId()))
                .andExpect(status().isNoContent());
    }
}