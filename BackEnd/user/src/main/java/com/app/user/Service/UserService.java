package com.app.user.Service;

import com.app.user.DTO.UserDTO;
import com.app.user.Entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    void addUser(User user);
    List<UserDTO> searchUsers();
    User register(User user);
    Optional<User> findByEmail(String email);
    boolean authenticate(String email, String password);
    User convertToEntity(UserDTO userDTO);
    UserDTO convertToDTO(User user);

}
