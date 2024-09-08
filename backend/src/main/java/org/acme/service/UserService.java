package org.acme.service;

import jakarta.transaction.Transactional;
import org.acme.entity.UserEntity;
import org.acme.repository.UserRepository;
import java.util.List;

public class UserService {

    @Transactional
    public static UserEntity createUser(String name) {
        return UserRepository.createUser(name);
    }


    @Transactional
    public static UserEntity getUserById(Long id) {
        return UserRepository.getUserById(id);
    }

    @Transactional
    public static UserEntity updateUserName(Long id, String name) {
        return UserRepository.updateUserName(id, name);
    }

    @Transactional
    public static void deleteUser(Long id) {
        UserRepository.deleteUser(id);
    }

    @Transactional
    public static List<UserRepository.UserDTO> listUsers(Long id, String name,Integer page, Integer limit) {
        return UserRepository.listUsers(id, name, page, limit);
    }   
}
