package com.example.checkin.service;

import com.example.checkin.models.Visitors;
import com.example.checkin.repo.VisitorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class VisitorsService {

    @Autowired
    private VisitorsRepository visitorsRepository;

    public List<Visitors> getAllVisitors(){
        return visitorsRepository.findAll();
    }

    public Optional<Visitors> getVisitorByID(Long id){
        return visitorsRepository.findById(id);
    }

    public Visitors saveVisitor(Visitors visitor){
        return visitorsRepository.save(visitor);
    }

    public List<Visitors> getCheckInVisitors(){
        return  visitorsRepository.findByStatusTrue();
    }

    public List<Visitors> getVisitorsByDate(Date checkinDate){
        return visitorsRepository.findByCheckinDate(checkinDate);
    }

    public List<Visitors> getVisitorsBetweenDates(Date startdate , Date enddate){
        return  visitorsRepository.findAllByCheckinDateBetween(startdate , enddate);
    }

    public Visitors checkoutVisitor(Visitors visitor){
        return visitorsRepository.save(visitor);
    }

    public Map<String , Object> visitorsStats(LocalDate date){
        long totalVisitors = visitorsRepository.countByCheckinDate(date);
        long checkedInVisitorss = visitorsRepository.countByCheckinDateAndStatus(date , true);
        long checkedOutVisitors = visitorsRepository.countByCheckinDateAndStatus(date , false);
        Double avgDuration = visitorsRepository.findAverageDurationByDate(date);

        Map<String , Object> response = new HashMap<>();
        response.put("totalVisitors" , totalVisitors);
        response.put("checkedInVisitors" , checkedInVisitorss);
        response.put("CheckedOutVisitors" , checkedOutVisitors);
        response.put("avgDuration" , avgDuration != null ? avgDuration : 0);
        return response;
    }


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

