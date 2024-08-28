package interviewgether.authserver.repository;

import interviewgether.authserver.model.AuthUser;
import interviewgether.authserver.model.OneTimePasscode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OneTimePasscodeRepository extends JpaRepository<OneTimePasscode, Long> {

    Optional<OneTimePasscode> findByCode(String code);

    Optional<OneTimePasscode> findByUserEmail(String email);

    Optional<OneTimePasscode> findByUser(AuthUser user);

    @Modifying
    @Query(value = "DELETE FROM one_time_passcode WHERE code = :code", nativeQuery = true)
    void deleteByCode(@Param("code") String code);

}
