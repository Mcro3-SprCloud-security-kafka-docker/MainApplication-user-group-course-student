package com.quanzip.filemvc.repository;

import com.quanzip.filemvc.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Modifying
    @Query(value = "delete from Role r where r.user.id = :uId")
    void deleteByUserId(@Param(value = "uId") Long userId);

    Page<Role> findAllByRoleLike(String role, Pageable pageable);

    Page<Role> findAllByRoleLikeOrRoleIgnoreCase(String role, String role1, Pageable pageable);

    List<Role> findAllByUser_Id(Long userId);

    Integer countAllByRoleAndUser_Id(String role, Long userId);
}
