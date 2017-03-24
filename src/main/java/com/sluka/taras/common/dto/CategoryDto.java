package com.sluka.taras.common.dto;

import com.sluka.taras.common.model.Category;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class CategoryDto extends BaseDto {
    private Long id;
    private Long parent;
    private String name;
    private String description;
    private List<Category> children = new ArrayList<>();
    private List<Long> childCategory = new ArrayList<>();

    @Override
    public String toString() {
        return "Category{" +
                ", id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", value='" + description + '\'' +
//                ", productArrayList=" + productArrayList.toArray().toString() + '\'' +
                ", parent=" + parent + '\'' +
                ", children=" + children.toArray().toString() +
                '}';

    }
}
