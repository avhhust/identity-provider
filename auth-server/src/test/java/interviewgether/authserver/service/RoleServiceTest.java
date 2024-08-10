package interviewgether.authserver.service;

import interviewgether.authserver.model.Role;
import interviewgether.authserver.repository.RoleRepository;
import interviewgether.authserver.service.impl.RoleServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    RoleRepository roleRepository;
    @Mock
    UserService userService;
    @InjectMocks
    RoleServiceImpl roleService;

    @Test
    void shouldCreateRoleWithValidParameter() {
        Role role = new Role();

        roleService.create(role);

        ArgumentCaptor<Role> roleArgCaptor =
                ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(roleArgCaptor.capture());
        Role captured = roleArgCaptor.getValue();
        assertThat(captured).isEqualTo(role);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingRoleWithNull() {
        //when
        assertThatThrownBy(() -> roleService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Role cannot be null");
        //then
        verify(roleRepository, never()).save(any());
    }

    @Test
    void shouldReadRoleByExistingId() {
        //given
        long id = 1L;
        String name = "test_name";
        Role role = new Role();
        role.setRoleName(name);

        when(roleRepository.findById(id)).thenReturn(Optional.of(role));
        //when
        Role actualRole = roleService.readById(id);
        //then
        ArgumentCaptor<Long> argCaptor =
                ArgumentCaptor.forClass(long.class);
        verify(roleRepository).findById(argCaptor.capture());
        long capturedId = argCaptor.getValue();
        assertThat(capturedId).isEqualTo(id);
        assertThat(actualRole.getRoleName()).isEqualTo(name);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionForNonExistingId(){
        //given
        long nonExistingId = 999L;
        when(roleRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        //when
        assertThatThrownBy(() -> roleService.readById(nonExistingId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Role with Id " + nonExistingId + " not found");

        verify(roleRepository).findById(nonExistingId);
    }

    @Test
    void shouldUpdateRole() {
        //given
        long id = 1L;
        Role existingRole = new Role();
        existingRole.setId(id);
        existingRole.setRoleName("name");

        Role updatedRole = new Role();
        updatedRole.setId(id);
        updatedRole.setRoleName("updated_name");

        when(roleRepository.findById(id)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(updatedRole)).thenReturn(updatedRole);
        //when
        Role actualRole = roleService.update(updatedRole);
        //then
        assertThat(actualRole).isEqualTo(updatedRole);
        verify(roleRepository).save(updatedRole);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingWithNullParameter(){
        //given
        Role nullRole = null;
        //when
        assertThatThrownBy(() -> roleService.update(nullRole))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Role cannot be null");

        verify(roleRepository, never()).save(any());
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenUpdatingNonExistingRole(){
        Role role = new Role();
        role.setId(1L);
        //when
        assertThatThrownBy(() -> roleService.update(role))
                .isInstanceOf(EntityNotFoundException.class);

        verify(roleRepository, never()).save(any());
    }

    @Test
    void shouldDeleteRole() {
        //given
        long id = 1L;
        Role role = new Role();
        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        //when
        roleService.delete(id);

        //then
        verify(roleRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenDeletingNonExistingRole() {
        long nonExistingId = 1L;
        assertThatThrownBy(() -> roleService.delete(nonExistingId))
                .isInstanceOf(EntityNotFoundException.class);
        verify(roleRepository, never()).deleteById(any());
    }

    @Test
    void shouldGetAllRoles() {
        //when
        roleService.getAll();
        //then
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void shouldFindRoleByItsName() {
        String roleName = "ADMIN";
        when(roleRepository.findByRoleName("ADMIN")).thenReturn(Optional.of(new Role("ADMIN")));

        Role role = roleService.findByRoleName(roleName);

        ArgumentCaptor<String> nameArgCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(roleRepository, times(1))
                .findByRoleName(nameArgCaptor.capture());

        assertThat(nameArgCaptor.getValue()).isEqualTo(roleName);
        assertThat(role.getRoleName()).isEqualTo(roleName);
    }

    @Test
    void shouldThrowExceptionIfRoleWithSuchNameDoesntExist() {
        when(roleRepository.findByRoleName(anyString())).thenReturn(Optional.empty());
        String nonExistingName = "ROLE";

        assertThatThrownBy(() -> roleService.findByRoleName(nonExistingName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Role " + nonExistingName + " doesn't exists");
    }
}