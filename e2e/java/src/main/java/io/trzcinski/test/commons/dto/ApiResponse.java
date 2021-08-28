package io.trzcinski.test.commons.dto;

import lombok.Data;
import org.springframework.lang.Nullable;
import java.util.List;

@Data
public class ApiResponse{
    @Nullable
    private Integer code;
    @Nullable
    private String type;
    @Nullable
    private String message;
}