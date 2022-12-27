package com.quanzip.filemvc.repository;

import com.quanzip.filemvc.entity.Course;
import com.quanzip.filemvc.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByNameLikeOrNameIgnoreCase(String nameLike, String nameIgnoreCase);
}
