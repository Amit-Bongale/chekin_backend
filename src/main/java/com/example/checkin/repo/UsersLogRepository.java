package com.example.checkin.repo;


import com.example.checkin.models.UsersLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsersLogRepository extends JpaRepository<UsersLog , Long> {
    List<UsersLog> findByUsername(String username);

    List<UsersLog> findAllByLoginTimeBetween(LocalDateTime start, LocalDateTime end);

    List<UsersLog> findByLogoutTimeIsNull();
}
