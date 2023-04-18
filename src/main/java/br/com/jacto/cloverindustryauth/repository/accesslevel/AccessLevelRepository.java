package br.com.jacto.cloverindustryauth.repository.accesslevel;

import br.com.jacto.cloverindustryauth.model.accesslevel.AccessLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccessLevelRepository extends JpaRepository<AccessLevel, UUID> {


    @Query(value = """
           select count(*)
           from users_accesslevels ua 
           join 
           accesslevels a 
           on 
           ua.accesslevel_id = a.id 
           where 
           a.id = :accessLevelId
           """, nativeQuery = true)
    int countUsersByAccessLevelId(UUID accessLevelId);
}
