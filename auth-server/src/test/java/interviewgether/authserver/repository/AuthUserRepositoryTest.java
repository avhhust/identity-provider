package interviewgether.authserver.repository;

import interviewgether.authserver.model.AuthUser;
import interviewgether.authserver.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuthUserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        //given
        String email = "test@email.com";
        AuthUser authUser = new AuthUser();
        authUser.setEmail(email);
        authUser.setUsername("username");
        authUser.setPassword("password");
        userRepository.save(authUser);
        //when
        boolean result = userRepository.isEmailExists(email);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenEmailDoesntExist() {
        //given
        String email = "test@email.com";
        //when
        boolean result = userRepository.isEmailExists(email);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueWhenUsernameExists() {
        //given
        String username = "username";
        AuthUser authUser = new AuthUser();
        authUser.setUsername(username);
        authUser.setEmail("test@email.com");
        authUser.setPassword("password");
        userRepository.save(authUser);
        //when
        boolean result = userRepository.isUsernameExists(username);
        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenUsernameDoesntExist() {
        //given
        String username = "username";
        //when
        boolean result = userRepository.isUsernameExists(username);
        //then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnUserByUsername(){
        String username = "username";
        AuthUser authUser = new AuthUser();
        authUser.setUsername(username);
        authUser.setEmail("test@email.com");
        authUser.setPassword("password");
        userRepository.save(authUser);

        AuthUser founded = userRepository.findByUsername(username).orElse(new AuthUser());

        assertThat(founded).isEqualTo(authUser);
    }

    @Test
    void shouldReturnUserWithFetchedRolesByUsername() {
        String username = "username";
        AuthUser givenAuthUser = new AuthUser();
        givenAuthUser.setUsername(username);
        givenAuthUser.setEmail("email@test.com");
        givenAuthUser.setPassword("password");
        Role roleUser = roleRepository.save(new Role("USER"));
        givenAuthUser.addRole(roleUser);
        userRepository.save(givenAuthUser);

        AuthUser retrieved = userRepository.findByUsernameWithRoles(username).orElse(new AuthUser());

        assertThat(retrieved).isEqualTo(givenAuthUser);
        assertThat(retrieved.getRoles()).contains(roleUser);
    }
}
