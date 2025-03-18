// UserService.java
package com.healthsystem.service;

import com.healthsystem.model.User;
import com.healthsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Integer id) {
        User user = userRepository.findById(id);
        if (user != null) {
            // 不返回密码
            user.setPassword(null);
        }
        return user;
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            // 不返回密码
            user.setPassword(null);
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        // 不返回密码
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    public boolean updateUser(User user) {
        // 仅更新允许的字段，不更新密码
        User existingUser = userRepository.findById(user.getId());
        if (existingUser == null) {
            return false;
        }

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());

        // 只有管理员可以更改角色
        if ("ADMIN".equals(user.getRole())) {
            existingUser.setRole(user.getRole());
        }

        return userRepository.update(existingUser) > 0;
    }

    public boolean deleteUser(Integer id) {
        return userRepository.deleteById(id) > 0;
    }
}