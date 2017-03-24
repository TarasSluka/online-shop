package com.sluka.taras.repository;

import com.sluka.taras.common.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {

    User findByEmail(String email);

    User findByUserName(String userName);

}
