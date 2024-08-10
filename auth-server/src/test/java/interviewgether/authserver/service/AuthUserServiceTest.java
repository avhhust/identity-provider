package interviewgether.authserver.service;
import interviewgether.authserver.dto.UserRegisterDTO;
import interviewgether.authserver.exception.DAL.EmailAlreadyExistsException;
import interviewgether.authserver.exception.DAL.EmailNotFoundException;
import interviewgether.authserver.exception.DAL.UsernameAlreadyExistsException;
import interviewgether.authserver.exception.DAL.UsernameNotFoundException;
import interviewgether.authserver.model.AuthUser;
import interviewgether.authserver.model.Role;
import interviewgether.authserver.repository.RoleRepository;
import interviewgether.authserver.repository.UserRepository;
import interviewgether.authserver.service.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.CharBuffer;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


// ToDo: Refactor
@ExtendWith(MockitoExtension.class)
public class AuthUserServiceTest {

    // ToDo: add test for updated create(UserRegisterDTO) method
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void shouldCreateUserWhenValidUserProvided() {
        AuthUser authUser = new AuthUser();
        when(userRepository.save(authUser)).thenReturn(authUser);
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.of(new Role("USER")));
        //when
        userService.create(authUser);
        //then
        ArgumentCaptor<AuthUser> userArgumentCaptor =
                ArgumentCaptor.forClass(AuthUser.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        AuthUser captured = userArgumentCaptor.getValue();
        assertThat(captured).isEqualTo(authUser);
    }

    @Test
    void shouldAssignDefaultRoleWhenCreatingUser() {
        AuthUser authUser = new AuthUser();

        UserServiceImpl spyService = spy(userService);
        when(userRepository.save(authUser)).thenReturn(authUser);
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.of(new Role("USER")));

        spyService.create(authUser);

        verify(spyService).setDefaultRole(authUser);
    }

    @Test
    void shouldCreateDefaultRoleIfOneDoesntExistsAlready() {
        AuthUser authUser = new AuthUser();
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(new Role("USER"));

        userService.setDefaultRole(authUser);

        ArgumentCaptor<Role> roleArgCaptor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository, times(1)).save(roleArgCaptor.capture());
        Role captured = roleArgCaptor.getValue();
        assertThat(captured.getRoleName()).isEqualTo("USER");
    }

    @Test
    void shouldSetRoleWithNameUSERToUser() {
        AuthUser authUser = new AuthUser();
        Role roleUser = new Role("USER");
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.of(roleUser));

        userService.setDefaultRole(authUser);

        assertThat(authUser.getRoles()).contains(roleUser);

    }

    @Test
    void shouldCreateUserWithProvidedUserRegisterDTO() {
        UserRegisterDTO userRegisterDTO =
                new UserRegisterDTO("username", "email", "password");
        when(userRepository.save(any(AuthUser.class))).thenAnswer(ans -> ans.getArguments()[0]);
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.of(new Role("USER")));

        userService.create(userRegisterDTO);

        ArgumentCaptor<AuthUser> userArgumentCaptor =
                ArgumentCaptor.forClass(AuthUser.class);
        verify(userRepository, times(1))
                .save(userArgumentCaptor.capture());
        AuthUser captured = userArgumentCaptor.getValue();

        assertThat(captured.getUsername()).isEqualTo(userRegisterDTO.getUsername());
        assertThat(captured.getEmail()).isEqualTo(userRegisterDTO.getEmail());
        assertThat(captured.getPassword()).isEqualTo(userRegisterDTO.getPassword());
    }

    @Test
    void shouldEncodePasswordWhenCreatingUserFromUserRegisterDTO() {
        String password = "password";
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setPassword(password);
        when(userRepository.save(any(AuthUser.class))).thenAnswer(ans -> ans.getArgument(0));
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.of(new Role("USER")));

        userService.create(userRegisterDTO);

        verify(passwordEncoder, times(1)).encode(CharBuffer.wrap(password));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingUserWithNull() {
        AuthUser nullAuthUser = null;
        assertThatThrownBy(() -> userService.create(nullAuthUser))
                .hasMessageContaining("User cannot be null");
    }

    @Test
    void shouldThrowEmailAlreadyExistsExceptionWhenEmailExists(){
        AuthUser authUser = new AuthUser();
        authUser.setEmail("email@test.com");
        when(userRepository.isEmailExists(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(authUser))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void shouldThrowUsernameAlreadyExistsExceptionWhenUsernameExists(){
        AuthUser authUser = new AuthUser();
        authUser.setUsername("username");
        when(userRepository.isUsernameExists(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(authUser))
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    void shouldThrowExceptionWhenDataIntegrityViolationOccursDueToUsername(){
        when(userRepository.save(any(AuthUser.class)))
                .thenThrow(new DataIntegrityViolationException("username"));
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.of(new Role("USER")));

        assertThatThrownBy(() -> userService.create(new AuthUser()))
                .isInstanceOf(UsernameAlreadyExistsException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    void shouldThrowExceptionWhenDataIntegrityViolationOccursDueToEmail(){
        when(userRepository.save(any(AuthUser.class)))
                .thenThrow(new DataIntegrityViolationException("email"));
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.of(new Role("USER")));

        assertThatThrownBy(() -> userService.create(new AuthUser()))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    void shouldReadUserByExistingId() {
        AuthUser authUser = new AuthUser();
        long id = 1L;
        authUser.setUserId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(authUser));

        AuthUser retrieved = userService.readById(id);

        ArgumentCaptor<Long> argCaptor =
                ArgumentCaptor.forClass(long.class);

        verify(userRepository).findById(argCaptor.capture());
        long capturedId = argCaptor.getValue();

        assertThat(capturedId).isEqualTo(id);
        assertThat(retrieved.getUserId()).isEqualTo(id);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenIdDoesntExist() {
        long id = 1L;
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.readById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User with id " + id + " doesn't exist");
    }

    @Test
    void shouldUpdateUserWhenValidUserProvided() {
        AuthUser authUser = new AuthUser();
        authUser.setPassword("password");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(authUser));
        when(userRepository.save(authUser)).thenReturn(authUser);

        userService.update(authUser);

        ArgumentCaptor<AuthUser> userArgCaptor =
                ArgumentCaptor.forClass(AuthUser.class);
        verify(userRepository).save(userArgCaptor.capture());
        AuthUser captured = userArgCaptor.getValue();

        assertThat(captured).isEqualTo(authUser);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingUserWithNull() {
        AuthUser nullAuthUser = null;
        assertThatThrownBy(() -> userService.update(nullAuthUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User cannot be null");
        verify(userRepository, never()).save(any(AuthUser.class));
    }

    @Test
    void shouldDeleteUserByExistingId() {
        long id = 1L;
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new AuthUser()));

        userService.delete(id);

        ArgumentCaptor<Long> argCaptor =
                ArgumentCaptor.forClass(long.class);
        verify(userRepository).deleteById(argCaptor.capture());
        long capturedId = argCaptor.getValue();

        assertThat(capturedId).isEqualTo(id);
    }

    @Test
    void shouldReturnUserByValidUsername() {
        String username = "username";
        AuthUser authUser = new AuthUser();
        authUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(authUser));

        AuthUser retrieved = userService.readByUsername(username);

        ArgumentCaptor<String> argCaptor =
                ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByUsername(argCaptor.capture());
        String captured = argCaptor.getValue();

        assertThat(captured).isEqualTo(username);
        assertThat(retrieved).isEqualTo(authUser);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenReadingByInvalidUsername() {
        assertThatThrownBy(() -> userService.readByUsername(null))
                .hasMessageContaining("Username cannot be null");
    }

    @Test
    void shouldReturnUserWithRolesWhenProvidedValidUsername(){
        String username = "username";
        AuthUser authUser = new AuthUser();
        Role roleUser = new Role("USER");
        authUser.addRole(roleUser);
        when(userRepository.findByUsernameWithRoles(username)).thenReturn(Optional.of(authUser));

        AuthUser retrieved = userService.readUserWithRolesByUsername(username);

        ArgumentCaptor<String> argCaptor =
                ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByUsernameWithRoles(argCaptor.capture());

        assertThat(argCaptor.getValue()).isEqualTo(username);
        assertThat(retrieved).isEqualTo(authUser);
        assertThat(retrieved.getRoles()).contains(roleUser);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenReadingWithRolesByInvalidUsername() {
        assertThatThrownBy(() -> userService.readUserWithRolesByUsername(null))
                .hasMessageContaining("Username cannot be null");
    }

    @Test
    void shouldReturnUserByValidEmail() {
        String email = "test@email.com";
        AuthUser authUser = new AuthUser();
        authUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(authUser));

        AuthUser retrieved = userService.readByEmail(email);

        ArgumentCaptor<String> argCaptor =
                ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByEmail( argCaptor.capture());
        String captured = argCaptor.getValue();

        assertThat(captured).isEqualTo(email);
        assertThat(retrieved).isEqualTo(authUser);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenReadingByInvalidEmail() {
        assertThatThrownBy(() -> userService.readByEmail(null))
                .hasMessageContaining("Email cannot be null");
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUsernameDoesntExist() {
        String username = "username";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.readByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with username: " + username + " doesn't exist");
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenEmailDoesntExist() {
        String email = "test@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.readByEmail(email))
                .isInstanceOf(EmailNotFoundException.class)
                .hasMessageContaining("User with email: " + email + " doesn't exist");
    }
}