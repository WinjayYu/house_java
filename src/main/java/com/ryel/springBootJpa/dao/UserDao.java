package com.ryel.springBootJpa.dao;

import com.ryel.springBootJpa.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by burgl on 2016/8/16.
 */
@Repository
public interface UserDao extends JpaRepository<User, Integer> ,JpaSpecificationExecutor<User> {

    @Query("select u from User u where (u.email = ?1 or u.tel =?1)  and u.password = ?2")
    public User findByEmailOrMobileAndPassword(String username, String password);

    @Query("select a from User a where a.type = ?1")
    public Page<User> findByType(Integer type,Pageable pageable);

    @Query("select u from User u where u.email = ?1 or u.tel =?2")
    public User check(String email, String tel);

    @Query("select u from User u where u.type = 1")
    public List<User> registUser();
}
