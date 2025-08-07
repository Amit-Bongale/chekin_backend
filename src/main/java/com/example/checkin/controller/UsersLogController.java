package com.example.checkin.controller;

import com.example.checkin.models.UsersLog;
import com.example.checkin.repo.UsersLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/logs")
public class UsersLogController {

    @Autowired
    private UsersLogRepository usersLogRepository;

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ADMIN' , 'SUPER_ADMIN')")
    public ResponseEntity<List<UsersLog>> getAllLogs(){
        return ResponseEntity.ok(usersLogRepository.findAll());
    }

    @GetMapping("/between/{startdate}/{enddate}")
    public List<UsersLog> getVisitorsBetweenDates(@PathVariable Date startdate , @PathVariable Date enddate){
        List<UsersLog> logs = List.of();

        if(startdate != null && enddate != null){
            LocalDateTime start = startdate.toLocalDate().atStartOfDay();
            LocalDateTime end = enddate.toLocalDate().atTime(LocalTime.MAX);
            logs = usersLogRepository.findAllByLoginTimeBetween(start , end);
        }

        return logs;
    }


}
