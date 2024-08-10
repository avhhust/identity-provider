package interviewgether.authserver.service;

import interviewgether.authserver.dto.UserRegisterDTO;
import interviewgether.authserver.model.AuthUser;

public interface UserService {
    void create(AuthUser authUser);
    void create(UserRegisterDTO userRegisterDTO);
    AuthUser readById(long id);
    AuthUser update(AuthUser updatedAuthUser);
    void delete(long id);
    AuthUser readByEmail(String email);
    AuthUser readByUsername(String username);
    AuthUser readUserWithRolesByUsername(String username);
}
