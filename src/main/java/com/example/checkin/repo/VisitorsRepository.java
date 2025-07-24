package com.example.checkin.repo;

import com.example.checkin.models.Visitors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VisitorsRepository extends JpaRepository<Visitors ,Long> {

    // list all chcecked in users
    List<Visitors> findByStatusTrue();

    List<Visitors> findByCheckinDate(Date checkinDate);

    List<Visitors> findAllByCheckinDateBetween(Date startDate, Date endDate);

    long countByCheckinDate(LocalDate date);
    long countByCheckinDateAndStatus(LocalDate date , boolean status);

    @Query("SELECT AVG(v.duration) FROM Visitors v WHERE v.checkinDate = :date")
    Double findAverageDurationByDate(@Param("date") LocalDate date);
}
