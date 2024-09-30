package com.app.user.Service;

import com.app.user.DTO.UserDTO;
import com.app.user.Entity.User;
import com.app.user.Exception.EmailAlreadyExistsException;
import com.app.user.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<UserDTO> searchUsers() {
        List<User> users = userRepository.findAll();
        // Converte cada User em UserDTO usando o método de conversão e retorna uma lista de UserDTO
        return users.stream()
                .map(this::convertToDTO)  // Converte cada User para UserDTO
                .collect(Collectors.toList());     // Coleta o resultado em uma lista
    }


    public User register(User user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail()); // Buscar o usuário pelo email
        if (userOptional.isPresent()) {
            throw new EmailAlreadyExistsException( "The email is already in use: " + user.getEmail() );
        }
        // Criptografar a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean authenticate(String email, String password) {
         Optional<User> userOptional = userRepository.findByEmail(email); // Buscar o usuário pelo email
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return passwordEncoder.matches(password, user.getPassword()); // Verificar se a senha bate
        }
        return false; // Usuário não encontrado
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    public User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        return user;
    }


}
