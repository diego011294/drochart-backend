package edu.rico.backend.usersapp.backendusersapp.services;

import java.util.List;
import java.util.Optional;

import edu.rico.backend.usersapp.backendusersapp.models.dto.UserDto;
import edu.rico.backend.usersapp.backendusersapp.models.entities.User;

public interface UserService {
    
    List<UserDto> findAll();

    Optional<UserDto> findById(Long id);

    UserDto save(User user);
    Optional<UserDto> update(User user, Long id);

    void remove(Long id);

    Optional<UserDto> getUserByUsername(String username);

    Optional<User> findUserWithMostPosts();

}
