package com.example.checkin.tasks;

import com.example.checkin.models.UsersLog;
import com.example.checkin.repo.UsersLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AutoLogoutTask {

    @Autowired
    private UsersLogRepository usersLogRepository;

    @Scheduled(cron = "0 59 23 * * *")
    public void autoLogoutUsers(){
        List<UsersLog> activeLogs = usersLogRepository.findByLogoutTimeIsNull();
        LocalDateTime logoutTime = LocalDateTime.now();

        for(UsersLog log : activeLogs){
            log.setLogoutTime(logoutTime);
        }

        usersLogRepository.saveAll(activeLogs);
        System.out.println("Auto logout users");
    }
}
