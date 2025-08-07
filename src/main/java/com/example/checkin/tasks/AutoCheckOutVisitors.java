package com.example.checkin.tasks;

import com.example.checkin.models.Visitors;
import com.example.checkin.repo.VisitorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class AutoCheckOutVisitors {

    @Autowired
    private VisitorsRepository visitorsRepository;

    // Runs every day at 11:59 PM
    @Scheduled(cron = "0 59 23 * * *")
    public void autoCheckoutVisitors(){
        List<Visitors> StillCheckedIn = visitorsRepository.findByCheckinDateAndStatus( java.sql.Date.valueOf(LocalDate.now()) , true);

        Time now = Time.valueOf(LocalTime.now());

        for(Visitors v: StillCheckedIn){
            v.setCheckoutTime(now);
            v.setStatus(false);
            v.setDuration(null);
        }

        visitorsRepository.saveAll(StillCheckedIn);
        System.out.println("Auto Checkout Completed for " + StillCheckedIn.size() + "Visitors.");
    }
}
