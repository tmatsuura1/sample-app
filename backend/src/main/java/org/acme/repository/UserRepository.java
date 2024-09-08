package org.acme.repository;

import jakarta.ws.rs.NotFoundException;
import org.acme.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {

    public static UserEntity createUser(String name) {

        UserEntity user = new UserEntity();
        user.name = name; 
        user.persist();

        return user;
    }

    public static UserEntity getUserById(Long id) {
        UserEntity user = UserEntity.findById(id);
        if (user == null) {
            throw new NotFoundException("User not found with id " + id);
        }

        return user;
    }

    public static UserEntity updateUserName(Long id, String name) {
        UserEntity user = UserEntity.findById(id);
        if (user == null) {
            throw new NotFoundException("User not found with id " + id);
        }

        user.name = name;
        user.persist();

        return user;
    }

    public static void deleteUser(Long id) {
        UserEntity user = UserEntity.findById(id);
        if (user == null) {
            throw new NotFoundException("User not found with id " + id);
        }

        user.delete();
    }

    public static List<UserDTO> listUsers(Long id, String name,Integer page, Integer limit) {
    
        // クエリの基本部分を構築
        StringBuilder query = new StringBuilder("1 = 1");
        if (id != null) {
            query.append(" and id = ").append(id);
        }
        if (name != null && !name.isEmpty()) {
            query.append(" and name = '").append(name).append("'");
        }
    
        // データベースからユーザーリストを取得
        List<UserEntity> users = UserEntity.list(query.toString());
    
        if (users.isEmpty()) {
            return null;
        } else {
            // ページネーションが無効な場合（pageやlimitがnull）の場合、全件を返す
            if (page != null && limit != null) {
                int start = (page - 1) * limit;
                int end = Math.min(start + limit, users.size());
                users = users.subList(start, end);
            }
    
            // DTO に変換
            List<UserDTO> userDTOs = users.stream()
                .map(user -> new UserDTO(user.id, user.name))
                .collect(Collectors.toList());
    
            return userDTOs;
        }
    }
    
    public static class UserDTO {
        public Long id;
        public String name;
    
        public UserDTO(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }    
    
}
