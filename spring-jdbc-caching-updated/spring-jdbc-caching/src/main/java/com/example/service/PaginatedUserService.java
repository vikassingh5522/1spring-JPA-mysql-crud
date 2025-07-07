package com.example.service;

import com.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaginatedUserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Cacheable(value = "usersCache", key = "'paginatedUsers_page' + #page + '_size' + #size")
    public List<User> getPaginatedUsers(int page, int size) {
        int offset = (page - 1) * size;
        return jdbcTemplate.query(
                "SELECT id, name FROM users LIMIT ? OFFSET ?",
                new Object[]{size, offset},
                (rs, rowNum) -> new User(rs.getString("id"), rs.getString("name"))
        );
    }

    @Cacheable(value = "usersCache", key = "'sortedUsers_sortBy' + #sortBy + '_sortDir' + #sortDir")
    public List<User> getSortedUsers(String sortBy, String sortDir) {
        String column = "id".equals(sortBy) ? "id" : "name";
        String direction = "desc".equals(sortDir) ? "DESC" : "ASC";
        String query = String.format("SELECT id, name FROM users ORDER BY %s %s", column, direction);
        return jdbcTemplate.query(
                query,
                (rs, rowNum) -> new User(rs.getString("id"), rs.getString("name"))
        );
    }

    public int getTotalUsers() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    }
}