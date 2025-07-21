package com.example.checkin.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
@Entity
@Table(name = "visitors")
public class Visitors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long mobile;
    private Time checkinTime;
    private Integer duration;
    private Time checkoutTime;
    private Date checkinDate;
    private String visiting;
    private String purpose;
    private Boolean status;

    @Version
    private Integer version;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public Time getCheckinTime() {
        return checkinTime;
    }

    public void setCheckinTime(Time checkinTime) {
        this.checkinTime = checkinTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Time getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(Time checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public Date getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(Date checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getVisiting() {
        return visiting;
    }

    public void setVisiting(String visiting) {
        this.visiting = visiting;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
