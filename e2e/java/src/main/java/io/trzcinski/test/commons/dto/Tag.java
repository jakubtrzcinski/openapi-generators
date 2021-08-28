package io.trzcinski.test.commons.dto;

import lombok.Data;
import org.springframework.lang.Nullable;
import java.util.List;

@Data
public class Tag{
    @Nullable
    private Integer id;
    @Nullable
    private String name;
}