package com.example.checkin.controller;

import com.example.checkin.models.Visitors;
import com.example.checkin.service.VisitorsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/visitors")
@CrossOrigin(origins = "*")
public class VisitorsController {

    @Autowired
    private VisitorsService visitorsService;

    @GetMapping("/")
    public List<Visitors> getAllVisitors(){
        return visitorsService.getAllVisitors();
    }

    @GetMapping("/{id}")
    public Optional<Visitors> getVisitor(@PathVariable Long id){
        return visitorsService.getVisitorByID(id);
    }


    @PostMapping("/add")
    public Visitors addVisitor(@RequestBody Visitors visitor) {
        try {
            // set status as checked-in
            visitor.setStatus(true);

            // set current check-in date and time
            visitor.setCheckinTime(java.sql.Time.valueOf(java.time.LocalTime.now()));
            visitor.setCheckinDate(new java.sql.Date(System.currentTimeMillis()));

            return visitorsService.saveVisitor(visitor);
        } catch (Exception e) {
            throw new RuntimeException("Error saving visitor: " + e.getMessage(), e);
        }
    }

    @PostMapping("/update/{id}")
    public Visitors updateVisitor(@PathVariable Long id , @RequestBody Visitors updatedVisitor){
        Visitors existingVisitor = visitorsService.getVisitorByID(id)
                .orElseThrow(() -> new EntityNotFoundException("Visitor not found"));

        existingVisitor.setName(updatedVisitor.getName());
        existingVisitor.setMobile(updatedVisitor.getMobile());
        existingVisitor.setCheckinTime(updatedVisitor.getCheckinTime());
        existingVisitor.setDuration(updatedVisitor.getDuration());
        existingVisitor.setCheckoutTime(updatedVisitor.getCheckoutTime());
        existingVisitor.setCheckinDate(updatedVisitor.getCheckinDate());
        existingVisitor.setVisiting(updatedVisitor.getVisiting());
        existingVisitor.setPurpose(updatedVisitor.getPurpose());
        existingVisitor.setStatus(updatedVisitor.getStatus());

        return visitorsService.saveVisitor(existingVisitor);

    }

    @GetMapping("/checkedin")
    public List<Visitors> getCheckInVisitors(){
        return visitorsService.getCheckInVisitors();
    }

    @GetMapping("/checkedin/{date}")
    public List<Visitors> getVisitorsByDate(@PathVariable Date date){
        return visitorsService.getVisitorsByDate(date);
    }

    @PostMapping("/checkout/{id}")
    public ResponseEntity<?> chekoutVisitor(@PathVariable Long id){

        Optional <Visitors> optionalVisitor = visitorsService.getVisitorByID(id);

        if(optionalVisitor.isEmpty()){
            return  ResponseEntity.notFound().build();
        }

        Visitors visitor = optionalVisitor.get();

        if (!visitor.getStatus()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Visitor already checked out");
        }

        Time now = Time.valueOf(LocalTime.now());
        visitor.setCheckoutTime(now);


        if(visitor.getCheckinTime() != null && visitor.getCheckoutTime() != null){
            LocalTime checkin = visitor.getCheckinTime().toLocalTime();
            LocalTime checkout = visitor.getCheckoutTime().toLocalTime();

            int duration = (int) Duration.between(checkin , checkout).toMinutes();
            visitor.setDuration(duration);

            visitor.setStatus(false);
        }


        Visitors checkoutVisitor = visitorsService.checkoutVisitor(visitor);

        return ResponseEntity.ok(checkoutVisitor);
    }

}
