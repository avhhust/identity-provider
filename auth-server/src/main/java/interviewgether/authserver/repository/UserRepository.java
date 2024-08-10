package interviewgether.authserver.repository;

import interviewgether.authserver.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN " +
            "TRUE ELSE FALSE END FROM AuthUser u " +
            "WHERE u.email = ?1"
    )
    Boolean isEmailExists(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN " +
            "TRUE ELSE FALSE END FROM AuthUser u " +
            "WHERE u.username = ?1"
    )
    Boolean isUsernameExists(String username);
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByUsername(String username);

    @Query("SELECT u FROM AuthUser u " +
            "JOIN FETCH u.roles " +
            "WHERE u.username = :username"
    )
    Optional<AuthUser> findByUsernameWithRoles(String username);
}
