package br.com.jacto.cloverindustryauth.dto.user;

import br.com.jacto.cloverindustryauth.dto.accesslevel.AccessLevelResponseDto;
import br.com.jacto.cloverindustryauth.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponseDto {
    private UUID id;
    private String name;
    private String email;
    private LocalDateTime created_at;
    private List<AccessLevelResponseDto> access_levels;

    public UserDetailResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.created_at = user.getCreated_at();
        if (user.getAccess_levels() != null) {
            this.access_levels = user.getAccess_levels().stream().map(AccessLevelResponseDto::new).collect(Collectors.toList());
        }
    }
}

