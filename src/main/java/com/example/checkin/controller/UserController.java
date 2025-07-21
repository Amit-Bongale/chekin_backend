package com.example.checkin.controller;

import com.example.checkin.models.Users;
import com.example.checkin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

   @Autowired
   private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public Users addusers(@RequestBody Users user){
        return userService.saveuser(user);
    }
}
