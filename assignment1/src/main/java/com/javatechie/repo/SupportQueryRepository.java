package com.javatechie.repo;

import com.javatechie.entity.SupportQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportQueryRepository extends JpaRepository<SupportQuery, Long> {
}
