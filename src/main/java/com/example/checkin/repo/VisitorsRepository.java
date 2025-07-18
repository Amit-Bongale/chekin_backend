package com.example.checkin.repo;

import com.example.checkin.models.Visitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface VisitorsRepository extends JpaRepository<Visitors ,Long> {

    // list all chcecked in users
    List<Visitors> findByStatusTrue();

    List<Visitors> findByCheckinDate(Date checkinDate);

}
