package io.trzcinski.test.user;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;

import io.trzcinski.test.user.dto.User;

interface UserClient {

    @PostMapping("/user/createWithList")
    void createUsersWithListInput(
        @RequestBody() List<User> payload
    );

    @GetMapping("/user/{username}")
    User getUserByName(
        @RequestParam("username") String username
    );

    @PutMapping("/user/{username}")
    void updateUser(
        @RequestBody() User payload,
        @RequestParam("username") String username
    );

    @DeleteMapping("/user/{username}")
    void deleteUser(
        @RequestParam("username") String username
    );

    @GetMapping("/user/login")
    String loginUser(
        @RequestParam("username") String username,
        @RequestParam("password") String password
    );

    @GetMapping("/user/logout")
    void logoutUser(
    );

    @PostMapping("/user/createWithArray")
    void createUsersWithArrayInput(
        @RequestBody() List<User> payload
    );

    @PostMapping("/user")
    void createUser(
        @RequestBody() User payload
    );
}