package interviewgether.authserver.service.impl;

import interviewgether.authserver.dto.UserTransformer;
import interviewgether.authserver.dto.UserRegisterDTO;
import interviewgether.authserver.exception.DAL.*;
import interviewgether.authserver.model.AuthUser;
import interviewgether.authserver.model.Role;
import interviewgether.authserver.repository.RoleRepository;
import interviewgether.authserver.repository.UserRepository;
import interviewgether.authserver.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.nio.CharBuffer;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void create(AuthUser authUser) throws UserAlreadyExistsException {
        Assert.notNull(authUser, "User cannot be null");
        if(userRepository.isEmailExists(authUser.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if(userRepository.isUsernameExists(authUser.getUsername())){
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        try{
            setDefaultRole(authUser);
            AuthUser persistedUser = userRepository.save(authUser);

        } catch (DataIntegrityViolationException e){
            // ToDo: Review later
            String errorMessage = e.getMostSpecificCause().getMessage();
            if(errorMessage.contains("username"))
                throw new UsernameAlreadyExistsException("Username already exists");
            else if (errorMessage.contains("email"))
                throw new EmailAlreadyExistsException("Email already exists");
            else
                throw new UserAlreadyExistsException("User already exists");
        }
    }

    @Override
    @Transactional
    public void create(UserRegisterDTO userRegisterDTO) {
        userRegisterDTO.setPassword(passwordEncoder.encode(CharBuffer.wrap(userRegisterDTO.getPassword())));
        create(UserTransformer.convertToUser(userRegisterDTO));
    }

    @Transactional
    public void setDefaultRole(AuthUser authUser){
        Role defaultRole = roleRepository.findByRoleName("USER").orElseGet(
                () -> roleRepository.save(new Role("USER"))
        );
        authUser.addRole(defaultRole);
    }

    @Override
    public AuthUser readById(long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " doesn't exist"));
    }

    @Override
    public AuthUser update(AuthUser updatedAuthUser) {
        Assert.notNull(updatedAuthUser, "User cannot be null");
        readById(updatedAuthUser.getUserId());
        return userRepository.save(updatedAuthUser);
    }

    @Override
    public void delete(long id) {
        readById(id);
        userRepository.deleteById(id);
    }

    @Override
    public AuthUser readByEmail(String email) {
        Assert.notNull(email, "Email cannot be null");
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("User with email: " + email + " doesn't exist"));
    }

    @Override
    public AuthUser readByUsername(String username) {
        Assert.notNull(username, "Username cannot be null");
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " doesn't exist"));
    }

    @Override
    public AuthUser readUserWithRolesByUsername(String username){
        Assert.notNull(username, "Username cannot be null");
        return userRepository
                .findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " doesn't exist"));
    }
}