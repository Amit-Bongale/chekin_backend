package com.example.checkin.repo;


import com.example.checkin.models.UsersLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UsersLogRepository extends JpaRepository<UsersLog , Long> {
    List<UsersLog> findByUsername(String username);

    @Query("SELECT l FROM UsersLog l WHERE l.loginTime BETWEEN :start AND :end")
    List<UsersLog> findAllByLoginTimeBetween( LocalDateTime start, LocalDateTime end);

    List<UsersLog> findByLogoutTimeIsNull();
}
