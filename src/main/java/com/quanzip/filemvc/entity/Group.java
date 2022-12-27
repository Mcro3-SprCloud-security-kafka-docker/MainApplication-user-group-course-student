package com.quanzip.filemvc.entity;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.Generated;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "groupp")
@EnableJpaAuditing()
//@org.hibernate.annotations.NamedQuery(name = "findAllGroups", query = "select g from GroupUser g")
public class Group extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "groupp_user",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
