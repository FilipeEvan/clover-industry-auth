package br.com.jacto.cloverindustryauth.dto.accesslevel;

import br.com.jacto.cloverindustryauth.model.accesslevel.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLevelResponseDto {

    private UUID id;
    private String access_level;

    public AccessLevelResponseDto(AccessLevel accessLevel) {
        this.id = accessLevel.getId();
        this.access_level = accessLevel.getAccess_level();
    }
}
