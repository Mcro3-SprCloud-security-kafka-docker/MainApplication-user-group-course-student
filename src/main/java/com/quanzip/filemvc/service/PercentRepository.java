package com.quanzip.filemvc.service;

import com.quanzip.filemvc.entity.Percent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PercentRepository extends JpaRepository<Percent, Long> {
    void deleteAllByCategory(int category);
}
