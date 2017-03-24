package com.sluka.taras.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
public class Category extends BaseEntity {
    @ManyToOne(/*cascade = {CascadeType.MERGE},*/ fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Category parent;

    @OneToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    private String name;
    private String description;

    public void addToChildList(Category category) {
        children.add(category);
    }

    @Override
    public String toString() {
        return "Category{" +
                ", id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", value='" + description + '\'' +
                ", parent=" + parent + '\'' +
                '}';

    }

}
