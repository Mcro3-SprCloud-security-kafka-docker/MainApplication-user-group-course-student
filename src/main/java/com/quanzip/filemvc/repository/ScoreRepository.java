package com.quanzip.filemvc.repository;

import com.quanzip.filemvc.entity.Course;
import com.quanzip.filemvc.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

}
