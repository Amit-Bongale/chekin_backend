package com.example.checkin.repo;

import com.example.checkin.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <Users , Long>{
    Optional<Users> findByUsername(String username);

    List<Users> findByStatusTrue();

    long countByStatusTrue();

    List<Users> findByRole(String staff);
    List<Users> findByRoleNot(String superAdmin);
}
