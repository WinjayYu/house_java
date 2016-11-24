package com.ryel.springBootJpa.dao;

import com.ryel.springBootJpa.entity.User;
import com.ryel.springBootJpa.entity.UserFllow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: koabs
 * 9/1/16.
 */
@Repository
public interface UserFllowDao extends JpaRepository<UserFllow, Integer> {

    @Query("select uf.fllowUser.id from UserFllow uf where uf.user.id = ?1")
    public List<Integer> findWatch(Integer userId);

    @Query("select uf.fllowUser.id from UserFllow uf where uf.user.id = ?1 and uf.fllowUser.id = ?2")
    public UserFllow check(Integer userId, Integer fllowUserId);

    @Modifying()
    @Query("delete from UserFllow uf where uf.user.id = ?1 and uf.fllowUser.id = ?2")
    public void unWatch(Integer userId, Integer fllowUserId);


    @Query("select uf.fllowUser from UserFllow uf where uf.user.id = ?1")
    public List<User> watchlist(Integer userId);

    @Query("select uf.user from UserFllow uf where uf.fllowUser.id = ?1")
    public List<User> fllowlist(Integer userId);
}
