package br.com.jacto.cloverindustryauth.service.accesslevel;

import br.com.jacto.cloverindustryauth.ValidationException;
import br.com.jacto.cloverindustryauth.dto.accesslevel.AccessLevelRequestDto;
import br.com.jacto.cloverindustryauth.dto.accesslevel.AccessLevelResponseDto;
import br.com.jacto.cloverindustryauth.model.accesslevel.AccessLevel;
import br.com.jacto.cloverindustryauth.model.user.User;
import br.com.jacto.cloverindustryauth.repository.accesslevel.AccessLevelRepository;
import br.com.jacto.cloverindustryauth.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccessLevelService {

    @Autowired
    private AccessLevelRepository accessLevelRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public AccessLevelResponseDto postAccessLevel(AccessLevelRequestDto dto) {
        AccessLevel accessLevel = new AccessLevel(dto);
        // Salva as informações do nível de acesso no banco de dados
        accessLevelRepository.save(accessLevel);

        return new AccessLevelResponseDto(accessLevel);
    }

    public Page<AccessLevelResponseDto> getAllAccessLevels(@PageableDefault(page = 0, size = 10, sort = {"access_level"}) Pageable pageable) {
        var page = accessLevelRepository.findAll(pageable).map(AccessLevelResponseDto::new);
        return page;
    }

    @Transactional
    public AccessLevelResponseDto putAccessLevelById(UUID id, AccessLevelRequestDto dto) {
        // Busca o nível de acesso correspondente pelo ID
        AccessLevel accessLevel = accessLevelRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Nível de acesso não encontrado"));

        // Atualiza as informações do nível de acesso
        if (dto.getAccess_level() != null) {
            accessLevel.setAccess_level(dto.getAccess_level());
        }

        return new AccessLevelResponseDto(accessLevel);
    }

    @Transactional
    public void deleteAccessLevelById(UUID id) {
        // Busca o nível de acesso correspondente pelo ID
        AccessLevel accessLevel = accessLevelRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Nível de acesso não encontrado"));

        // Verifica se o nível de acesso está sendo usado por algum usuário
        int count = accessLevelRepository.countUsersByAccessLevelId(accessLevel.getId());
        if (count > 0) {
            throw new ValidationException("O nível de acesso não pode ser excluído porque está sendo usado por " + count + " usuário(s)");
        }

//        // Exclui as informações do nível de acesso do banco de dados
        accessLevelRepository.delete(accessLevel);
    }
}
