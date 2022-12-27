package com.quanzip.filemvc.repository;

import com.quanzip.filemvc.entity.Group;
import com.quanzip.filemvc.entity.GroupUser;
import com.quanzip.filemvc.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GroupUserRepositoryImpl {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<GroupUser> getGroupUserByGroupId(Long groupId){
        String sql = "select * from groupp_user p where group_id = :groupId";
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        List<GroupUser> result = namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(GroupUser.class));
        return result;
    }

    public void insertGroupUser(Group group, UserDTO userDTO) {
        String sql = "insert into groupp_user (group_id, user_id) values (:groupId, :userId)";
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", group.getId());
        params.put("userId", userDTO.getId());
        int size = namedParameterJdbcTemplate.update(sql, params);
        System.out.println("Update " + size + " user of group " + group.getName());
    }

    public void delete(Long groupId, Long userId) {
        String sql = "delete from groupp_user where group_id = :groupId and user_id = :userId";
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", groupId);
        params.put("userId", userId);
        int size = namedParameterJdbcTemplate.update(sql, params);
        System.out.println("deleted " + size + " group-user");

    }
}
