package com.example.checkin.controller;

import com.example.checkin.models.Users;
import com.example.checkin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

   @Autowired
   private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public List<Users> getUser(){
        return userService.getallUserss();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Optional<Users> getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public Users addUsers(@RequestBody Users user){
        user.setStatus(false);
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


    @PostMapping("/update/login/{username}")
    public ResponseEntity<String> updatelogin(@PathVariable String username){
         userService.updateLogin(username);
        return  ResponseEntity.ok("status updated");
    }

    @PostMapping("/update/logout/{username}")
    public ResponseEntity<String> updatelogoutstatus(@PathVariable String username){
        userService.updateLogout(username);
        return  ResponseEntity.ok("status updated");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/count/active")
    public long ActiveUsersCount(){
        return userService.ActiveUsersCount();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/active")
    public List<Users> ActiveUserst(){
        return userService.ActiveUsres();
    }


}
