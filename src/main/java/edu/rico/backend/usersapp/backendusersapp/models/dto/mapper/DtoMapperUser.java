package edu.rico.backend.usersapp.backendusersapp.models.dto.mapper;

import edu.rico.backend.usersapp.backendusersapp.models.dto.UserDto;
import edu.rico.backend.usersapp.backendusersapp.models.entities.User;

public class DtoMapperUser {
    
    private User user;

    private DtoMapperUser(){}

    public static DtoMapperUser builder(){
        return new DtoMapperUser();
    }

    public DtoMapperUser setUser(User user) {
        this.user = user;
        return this;
    }

    public UserDto build(){
        if (user == null) {
            throw new RuntimeException("Debe pasar el entinty user");
        }
        return new UserDto(this.user.getId(), user.getUsername(), user.getEmail());
    }
    
}
