package io.trzcinski.test.pet.dto;

import io.trzcinski.test.commons.dto.Category;
import io.trzcinski.test.commons.dto.Tag;
import lombok.Data;
import org.springframework.lang.Nullable;
import java.util.List;

@Data
public class Pet{
    @Nullable
    private Integer id;
    @Nullable
    private Category category;
    private String name;
    private List<String> photoUrls;
    @Nullable
    private List<Tag> tags;
    @Nullable
    private String status;
}