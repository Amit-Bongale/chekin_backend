package com.example.checkin.service;

import com.example.checkin.models.Visitors;
import com.example.checkin.repo.VisitorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

    public Visitors checkoutVisitor(Visitors visitor){
        if(visitor.getCheckinTime() != null && visitor.getCheckoutTime() != null){
            LocalTime chekin = visitor.getCheckinTime().toLocalTime();
            LocalTime checkout = visitor.getCheckoutTime().toLocalTime();

            int duration = (int) Duration.between(chekin , checkout).toMinutes();
            visitor.setDuration(duration);

            visitor.setStatus(false);
        }

        return visitorsRepository.save(visitor);
    }

}

