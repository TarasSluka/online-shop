package com.sluka.taras.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<Cart> cardList = new ArrayList<>();

    @Column(unique = true)
    @NotNull
    private String userName;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String phoneNumber;

    @Column(unique = true)
    @NotNull
    private String email;

    private String avaName;
    @NotNull
    @Size(min = 6)
    private String password;

    private Boolean enabled;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Sex sex;

}
