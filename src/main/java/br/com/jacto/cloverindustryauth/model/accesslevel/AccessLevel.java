package br.com.jacto.cloverindustryauth.model.accesslevel;

import br.com.jacto.cloverindustryauth.dto.accesslevel.AccessLevelRequestDto;
import br.com.jacto.cloverindustryauth.dto.product.ProductCreateRequestDto;
import br.com.jacto.cloverindustryauth.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "accesslevels")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccessLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String access_level;

    public AccessLevel(AccessLevelRequestDto dto) {
        this.access_level = dto.getAccess_level();
    }
}
