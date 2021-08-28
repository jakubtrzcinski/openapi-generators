package io.trzcinski.test.commons.dto;

import lombok.Data;
import org.springframework.lang.Nullable;
import java.util.List;

@Data
public class Order{
    @Nullable
    private Integer id;
    @Nullable
    private Integer petId;
    @Nullable
    private Integer quantity;
    @Nullable
    private java.time.LocalDateTime shipDate;
    @Nullable
    private String status;
    @Nullable
    private Boolean complete;
}