package com.sluka.taras.common.model;

import lombok.Data;
import org.thymeleaf.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
@Data
public class Product extends BaseEntity {
    @ManyToOne
    private Category category;

    private String name;
    private Long price;
    private String avaName;
    @OneToMany(cascade = {CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<Description> descriptionsArrayList = new ArrayList<>();

    @Column(length = 1000)
    private String searchString;

    @PreUpdate
    @PrePersist
    void updateSearchString() {
        final String fullSearchString = StringUtils.join(Arrays.asList(
                name,
                getCategoryName(),
                descriptionsArrayListToString()
                ),
                " ");
        if (fullSearchString.length() <= 999)
            this.searchString = fullSearchString;
        else
            this.searchString = fullSearchString.substring(0, 999);
    }

    private String getCategoryName() {
        if (category != null)
            return category.getName();
        return "";
    }

    private String descriptionsArrayListToString() {
        String str = "";
        if (descriptionsArrayList.size() == 0)
            return str;
        for (Description description : descriptionsArrayList)
            str += description.getValue() + " ";
        System.out.println(str.toString());
        return String.valueOf(str);
    }
}
