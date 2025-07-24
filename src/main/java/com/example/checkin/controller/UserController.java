package com.example.checkin.controller;

import com.example.checkin.models.Users;
import com.example.checkin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

   @Autowired
   private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public Users addUsers(@RequestBody Users user){
        return userService.saveuser(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{id}")
    public ResponseEntity<Users> updateUsers(@PathVariable Long id , @RequestBody Users updateuser){
        Users user = userService.updateUser(id, updateuser);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok("User Deleted Successfully");
    }

}
