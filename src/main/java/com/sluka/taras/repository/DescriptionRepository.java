package com.sluka.taras.repository;

import com.sluka.taras.common.model.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, Long> {
}
