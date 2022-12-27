package com.quanzip.filemvc.repository;

import com.quanzip.filemvc.entity.User;
import com.quanzip.filemvc.service.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT U FROM User U where lower(U.name) like %:name% or lower(U.userName) like %:name%")
    List<User> findAllByUserName(@Param(value = "name") String name);

    List<User> findAllByIdNotIn(List<Long> ids);
}
