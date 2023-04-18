package br.com.jacto.cloverindustryauth.controller.user;

import br.com.jacto.cloverindustryauth.ValidationException;
import br.com.jacto.cloverindustryauth.dto.product.ProductCreateRequestDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductDetailResponseDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductListResponseDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductUpdateRequestDto;
import br.com.jacto.cloverindustryauth.dto.user.UserCreateRequestDto;
import br.com.jacto.cloverindustryauth.dto.user.UserDetailResponseDto;
import br.com.jacto.cloverindustryauth.dto.user.UserListResponseDto;
import br.com.jacto.cloverindustryauth.dto.user.UserUpdateRequestDto;
import br.com.jacto.cloverindustryauth.model.product.Category;
import br.com.jacto.cloverindustryauth.model.product.Product;
import br.com.jacto.cloverindustryauth.model.product.Status;
import br.com.jacto.cloverindustryauth.model.user.User;
import br.com.jacto.cloverindustryauth.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Deveria devolver código http 400 quando informações estão inválidas")
    @WithMockUser(authorities = "ADMIN")
    @Transactional
    void getPostUserScenario1() throws Exception {
        var response = mvc.perform(MockMvcRequestBuilders.post("/users"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver código http 201 quando informações estão válidas")
    @WithMockUser(authorities = "ADMIN")
    @Transactional
    void postUserScenario2() throws Exception {
        UserCreateRequestDto requestDto = new UserCreateRequestDto();
        requestDto.setName("Test");
        requestDto.setEmail("test@test.com");
        requestDto.setPassword("123456");
        requestDto.setAccess_levels(Arrays.asList(UUID.randomUUID()));

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        user.setCreated_at(LocalDateTime.now());
        user.setAccess_levels(Collections.emptyList());

        UserDetailResponseDto responseDto = new UserDetailResponseDto(user);

        when(userService.postUser(ArgumentMatchers.any(UserCreateRequestDto.class)))
                .thenReturn(responseDto);

        // Realiza a requisição POST para criar um novo usuário
        mvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.access_levels").exists())
                .andExpect(jsonPath("$.created_at").exists());
    }

    @Test
    @DisplayName("Deveria retornar código http 200 e lista de usuários")
    @WithMockUser(authorities = "ADMIN")
    void getAllUsersScenario1() throws Exception {
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setName("Test 1");
        user1.setEmail("test1@test.com");
        user1.setPassword("13579");
        user1.setAccess_levels(Collections.emptyList());
        user1.setCreated_at(LocalDateTime.of(2023, 1, 30, 12, 0, 0));

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setName("Test 2");
        user2.setEmail("test2@test.com");
        user2.setPassword("02468");
        user2.setAccess_levels(Collections.emptyList());
        user2.setCreated_at(LocalDateTime.of(2023, 1, 31, 12, 0, 0));

        Page<UserListResponseDto> responseDtoPage = new PageImpl<>(
                Arrays.asList(
                        new UserListResponseDto(user1),
                        new UserListResponseDto(user2)
                )
        );

        when(userService.getAllUsers(any(Pageable.class)))
                .thenReturn(responseDtoPage);

        // Act and Assert
        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(user1.getId().toString()))
                .andExpect(jsonPath("$.content[0].name").value(user1.getName()))
                .andExpect(jsonPath("$.content[0].created_at").value("2023-01-30T12:00:00"))
                .andExpect(jsonPath("$.content[1].id").value(user2.getId().toString()))
                .andExpect(jsonPath("$.content[1].name").value(user2.getName()))
                .andExpect(jsonPath("$.content[1].created_at").value("2023-01-31T12:00:00"));
    }

    @Test
    @DisplayName("Deveria devolver código http 200 e informações do produto quando o ID é válido")
    @WithMockUser(authorities = "ADMIN")
    void getUserByIdScenario1() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Test 2");
        user.setEmail("test2@test.com");
        user.setPassword("123456");
        user.setAccess_levels(Collections.emptyList());
        user.setCreated_at(LocalDateTime.now());

        when(userService.getUserById(userId))
                .thenReturn(new UserDetailResponseDto(user));

        mvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.access_levels").exists());
    }

    @Test
    @DisplayName("Deveria devolver código http 404 quando o id não existe")
    @WithMockUser(authorities = "ADMIN")
    void getUserByIdScenario2() throws Exception {
        UUID userId = UUID.randomUUID();

        when(userService.getUserById(userId))
                .thenThrow(new ValidationException("Usuário não encontrado"));

        mvc.perform(get("/users/" + userId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deveria atualizar um usuário existente e retornar código http 200")
    @WithMockUser(authorities = "ADMIN")
    @Transactional
    void putUserByIdScenario1() throws Exception {
        // Cria um usuário existente para ser atualizado
        User existingUser = new User();
        existingUser.setId(UUID.randomUUID());
        existingUser.setName("Old User");
        existingUser.setEmail("old_test@test.com");
        existingUser.setPassword("123456");
        existingUser.setCreated_at(LocalDateTime.of(2023, 1, 30, 12, 0, 0));
        existingUser.setAccess_levels(Collections.emptyList());


        // Define o ID do usuário existente a ser atualizado
        UUID userId = existingUser.getId();

        // Cria uma requisição DTO de atualização de usuário
        UserUpdateRequestDto requestDto = new UserUpdateRequestDto();
        requestDto.setName("New User");
        requestDto.setEmail("new_user@test.com");
        requestDto.setPassword("abcdef");
        requestDto.setAccess_levels(Arrays.asList(UUID.randomUUID()));

        // Cria um usuário atualizado para ser retornado pelo serviço
        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName(requestDto.getName());
        updatedUser.setEmail(requestDto.getEmail());
        updatedUser.setPassword(requestDto.getPassword());
        updatedUser.setCreated_at(existingUser.getCreated_at());
        updatedUser.setAccess_levels(existingUser.getAccess_levels());

        // Define a resposta DTO que será retornada pelo serviço
        UserDetailResponseDto responseDto = new UserDetailResponseDto(updatedUser);

        // Configura o mock do serviço para retornar o usuário atualizado
        when(userService.putUserById(eq(userId), ArgumentMatchers.any(UserUpdateRequestDto.class)))
                .thenReturn(responseDto);

        // Realiza a requisição PUT para atualizar o usuário
        mvc.perform(put("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUser.getId().toString()))
                .andExpect(jsonPath("$.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.access_levels").exists())
                .andExpect(jsonPath("$.created_at").value("2023-01-30T12:00:00"));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @Transactional
    void deleteUserByIdScenario1() throws Exception {
        // Cria um usuário para deletar
        UserCreateRequestDto requestDto = new UserCreateRequestDto();
        requestDto.setName("Test");
        requestDto.setEmail("test@test.com");
        requestDto.setPassword("1234567");
        requestDto.setAccess_levels(Arrays.asList(UUID.randomUUID()));

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        user.setCreated_at(LocalDateTime.now());
        user.setAccess_levels(Collections.emptyList());

        UserDetailResponseDto responseDto = new UserDetailResponseDto(user);

        when(userService.postUser(any(UserCreateRequestDto.class))).thenReturn(responseDto);

        UserDetailResponseDto createdUser = objectMapper.readValue(
                mvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString(),
                UserDetailResponseDto.class
        );

        // Deleta o produto
        mvc.perform(delete("/users/" + createdUser.getId()))
                .andExpect(status().isNoContent());
    }
}