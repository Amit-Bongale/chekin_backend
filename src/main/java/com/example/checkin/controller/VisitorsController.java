package com.example.checkin.controller;

import com.example.checkin.models.Visitors;
import com.example.checkin.service.VisitorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import javax.swing.text.html.Option;
import java.sql.Date;
import java.sql.Time;
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
    public List<Visitors> getallVisitors(){
        return visitorsService.getAllVisitors();
    }

    @GetMapping("/{id}")
    public Optional<Visitors> getVisitor(@PathVariable Long id){
        return visitorsService.getVisitorByID(id);
    }

    @PostMapping("/add")
    public Visitors addVisitor(@RequestBody Visitors visitor){
        return visitorsService.saveVisior(visitor);
    }

    @PostMapping("/update/{id}")
    public Visitors updateVisitor(@PathVariable Long id , @RequestBody Visitors visitor){
        visitor.setId(id);
        return visitorsService.saveVisior(visitor);
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

        Visitors checkoutVisitor = visitorsService.checkoutVisitor(visitor);

        return ResponseEntity.ok(checkoutVisitor);
    }

}
