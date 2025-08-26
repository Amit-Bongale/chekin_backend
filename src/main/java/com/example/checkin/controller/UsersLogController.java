package com.example.checkin.controller;

import com.example.checkin.models.UsersLog;
import com.example.checkin.repo.UsersLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import java.time.LocalDate;
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


    @PreAuthorize("hasAnyRole('ADMIN' , 'SUPER_ADMIN')")
    @GetMapping(value = "/{startdate}/{enddate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UsersLog>> getVisitorsBetweenDates(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startdate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enddate) {

        LocalDateTime start = startdate.atStartOfDay();
        LocalDateTime end = enddate.atTime(LocalTime.MAX);

        List<UsersLog> result = usersLogRepository.findAllByLoginTimeBetweenAndRoleNot(start, end , "super_admin");
        return ResponseEntity.ok(result);
    }

}
