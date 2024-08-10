package interviewgether.authserver.dto;


import interviewgether.authserver.model.AuthUser;

public class UserTransformer {
    public static AuthUser convertToUser(UserRegisterDTO userRegisterDTO){
        AuthUser authUser = new AuthUser();
        authUser.setEmail(userRegisterDTO.getEmail());
        authUser.setUsername(userRegisterDTO.getUsername());
        authUser.setPassword(userRegisterDTO.getPassword());
        return authUser;
    }

    public static AuthUser convertToUser(UserLoginDTO userLoginDTO){
        AuthUser authUser = new AuthUser();
        authUser.setUsername(userLoginDTO.getUsername());
        authUser.setPassword(userLoginDTO.getPassword());
        return authUser;
    }

}