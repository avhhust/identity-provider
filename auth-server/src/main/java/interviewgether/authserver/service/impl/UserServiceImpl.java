package interviewgether.authserver.service.impl;

import interviewgether.authserver.dto.PasswordResetDTO;
import interviewgether.authserver.dto.UserTransformer;
import interviewgether.authserver.dto.UserRegisterDTO;
import interviewgether.authserver.exception.DAL.*;
import interviewgether.authserver.model.AuthUser;
import interviewgether.authserver.model.OneTimePasscode;
import interviewgether.authserver.model.Role;
import interviewgether.authserver.repository.RoleRepository;
import interviewgether.authserver.repository.UserRepository;
import interviewgether.authserver.service.OneTimePasscodeService;
import interviewgether.authserver.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.nio.CharBuffer;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final OneTimePasscodeService oneTimePasscodeService;

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
                .orElseThrow(() -> new EmailNotFoundException("User with this email doesn't exist"));
    }

    @Override
    public AuthUser readByUsername(String username) {
        Assert.notNull(username, "Username cannot be null");
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with this username doesn't exist"));
    }

    @Override
    public AuthUser readUserWithRolesByUsername(String username){
        Assert.notNull(username, "Username cannot be null");
        return userRepository
                .findByUsernameWithRoles(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with this username doesn't exist"));
    }

    @Override
    public void resetPassword(PasswordResetDTO passwordResetDTO) {
        // ToDo: consider to refactor and move otp verification to OneTimePasscodeServiceImpl
        // 1. Make sure code exists and it's for correct user
        OneTimePasscode otp = oneTimePasscodeService.readByCode(passwordResetDTO.getCode());
        String email = otp.getUser().getEmail();
        if(!email.equals(passwordResetDTO.getEmail())){
            throw new OneTimePasscodeNotValidException();
        }
        if(otp.getConfirmedAt() == null){
            // 2. Check if code was confirmed
            throw new OneTimePasscodeNotValidException();
        }
        AuthUser user = readByEmail(email);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(passwordResetDTO.getPassword())));
        update(user);
    }
}