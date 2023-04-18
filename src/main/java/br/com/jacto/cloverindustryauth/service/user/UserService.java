package br.com.jacto.cloverindustryauth.service.user;

import br.com.jacto.cloverindustryauth.ValidationException;
import br.com.jacto.cloverindustryauth.dto.user.UserCreateRequestDto;
import br.com.jacto.cloverindustryauth.dto.user.UserDetailResponseDto;
import br.com.jacto.cloverindustryauth.dto.user.UserListResponseDto;
import br.com.jacto.cloverindustryauth.dto.user.UserUpdateRequestDto;
import br.com.jacto.cloverindustryauth.model.accesslevel.AccessLevel;
import br.com.jacto.cloverindustryauth.model.user.User;
import br.com.jacto.cloverindustryauth.repository.accesslevel.AccessLevelRepository;
import br.com.jacto.cloverindustryauth.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessLevelRepository accessLevelRepository;

    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Transactional
    public UserDetailResponseDto postUser(UserCreateRequestDto dto) {
        User user = new User();
        System.out.println(dto);
        // Cria as informações do produto
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder().encode(dto.getPassword()));
        user.setCreated_at(LocalDateTime.now(ZoneId.of("UTC"))); // Salva a data de criação do usuário

        // Define níveis de acesso
        if (dto.getAccess_levels() != null) {
            List<AccessLevel> accessLevels = accessLevelRepository.findAllById(dto.getAccess_levels());
            user.setAccess_levels(accessLevels);
        }

        // Salva as informações do usuário no banco de dados
        userRepository.save(user);

        return new UserDetailResponseDto(user);
    }

    public Page<UserListResponseDto> getAllUsers(@PageableDefault(page = 0, size = 10, sort = {"name"}) Pageable pageable) {
        var page = userRepository.findAll(pageable).map(UserListResponseDto::new);
        return page;
    }

    public UserDetailResponseDto getUserById(UUID id) {
        // Busca o usuário correspondente pelo ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Usuário não encontrado"));

        return new UserDetailResponseDto(user);
    }

    @Transactional
    public UserDetailResponseDto putUserById(UUID id, UserUpdateRequestDto dto) {
        // Busca o usuário correspondente pelo ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Usuário não encontrado"));

        // Atualiza as informações do usuário
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }

        // Verifica se os níveis de acessos foram atualizados
        List<UUID> uuids = dto.getAccess_levels();
        if (uuids != null) {
            // Apaga as relações entre o usuário e seus níveis de acesso antigos
            user.getAccess_levels().clear();
            // Adiciona as novas relações entre o usuário e seus níveis de acesso atualizados
            for (UUID uuid : uuids) {
                AccessLevel accessLevel = accessLevelRepository.findById(uuid)
                        .orElseThrow(() -> new ValidationException("Cultura não encontrada"));
                user.getAccess_levels().add(accessLevel);
            }
        }

        // Salva as informações atualizadas do usuário no banco de dados
        user = userRepository.save(user);

        return new UserDetailResponseDto(user);
    }

    @Transactional
    public void deleteUserById(UUID id) {
        // Busca o usuário correspondente pelo ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Usuário não encontrado"));
        // Apaga as relações entre o usuário e seus níveis de acesso
        user.getAccess_levels().clear();
        // Exclui as informações do usuário do banco de dados
        userRepository.delete(user);
    }
}
