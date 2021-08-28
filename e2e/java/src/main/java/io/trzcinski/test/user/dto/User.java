package io.trzcinski.test.user.dto;

import lombok.Data;
import org.springframework.lang.Nullable;
import java.util.List;

@Data
public class User{
    @Nullable
    private Integer id;
    @Nullable
    private String username;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String email;
    @Nullable
    private String password;
    @Nullable
    private String phone;
    @Nullable
    private Integer userStatus;
}