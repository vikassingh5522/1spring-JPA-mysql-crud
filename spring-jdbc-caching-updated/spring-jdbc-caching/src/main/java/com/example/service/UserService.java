 package com.example.service;

import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Cacheable(value = "usersCache", key = "#userId", unless = "#result == null")
    public User getUserById(String userId) {
        Objects.requireNonNull(userId, "userId cannot be null");
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT id, name FROM users WHERE id = ?",
                    new Object[]{userId},
                    (rs, rowNum) -> new User(rs.getString("id"), rs.getString("name"))
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Transactional
    @CachePut(value = "usersCache", key = "#userId")
    public User updateUser(String userId, String name) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        jdbcTemplate.update("UPDATE users SET name = ? WHERE id = ?", name, userId);
        return new User(userId, name);
    }

    @Transactional
    @CacheEvict(value = "usersCache", key = "#userId")
    public void deleteUser(String userId) {
        Objects.requireNonNull(userId, "userId cannot be null");
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", userId);
    }

    @Transactional
    @CacheEvict(value = "usersCache", allEntries = true)
    public void createUser(String userId, String name) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        jdbcTemplate.update("INSERT INTO users (id, name) VALUES (?, ?)", userId, name);
    }

    @Cacheable(value = "usersCache", key = "'allUsers'")
    public List<User> getAllUsers() {
        return jdbcTemplate.query(
                "SELECT id, name FROM users",
                (rs, rowNum) -> new User(rs.getString("id"), rs.getString("name"))
        );
    }
}