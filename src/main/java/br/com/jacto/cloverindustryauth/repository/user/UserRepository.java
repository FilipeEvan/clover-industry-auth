package br.com.jacto.cloverindustryauth.repository.user;

import br.com.jacto.cloverindustryauth.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    UserDetails findByEmail(String email);
}
