package com.example.checkin.controller;
import com.example.checkin.jwt.JwtUtil;
import com.example.checkin.models.Users;
import com.example.checkin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

   @Autowired
   private JwtUtil jwtUtil;

    @PreAuthorize("hasAnyRole('ADMIN' , 'SUPER_ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<Users>> getUsersByRole(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        String role = jwtUtil.extractRole(token);

        List<Users> users;

        switch (role){
            case "ROLE_ADMIN" : users = userService.findUserByRole("STAFF"); break;
            case "ROLE_SUPER_ADMIN" : users = userService.getallUserss(); break;
            default: return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyRole('ADMIN' , 'SUPER_ADMIN')")
    @GetMapping("/{id}")
    public Optional<Users> getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN' , 'SUPER_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addUsers(@RequestBody Users user , HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String loggedInUsername = jwtUtil.extractusername(token);

        Users loggedInUser = userService.getUserByName(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        String loggedInRole = loggedInUser.getRole();
        String newUserRole = user.getRole();


        Optional<Users> existingUser = userService.getUserByName(user.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists. Please choose a different one.");
        }


        if (loggedInRole.equals("admin")) {
            if (!newUserRole.equals("staff")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Admins can only add STAFF users");
            }
        }

        if (loggedInRole.equals("super_admin")) {
            if (newUserRole.equals("super_admin")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Super Admins cannot add another Super Admin");
            }
        }

        user.setStatus(false);
        Users savedUser = userService.saveuser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PreAuthorize("hasAnyRole('ADMIN' , 'SUPER_ADMIN')")
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateUsers(@PathVariable Long id , @RequestBody Users updateuser , HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String loggedInUsername = jwtUtil.extractusername(token);

        Users loggedInUser = userService.getUserByName(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found"));

        Users targetUser = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        String loggedInRole = loggedInUser.getRole();
        String targetOldRole = targetUser.getRole();
        String targetNewRole = updateuser.getRole();

        if (loggedInRole.equals("admin")) {
            if (!targetOldRole.equals("staff")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Admins can only update STAFF users");
            }
            if (!targetNewRole.equals("staff")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Admins are not allowed to change roles to ADMIN or SUPER ADMIN");
            }
        }

        if (loggedInRole.equals("super_admin")) {
            if (targetOldRole.equals("super_admin")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Super Admins cannot update other Super Admins");
            }

            //super admin can not add other super admins
            if (targetNewRole.equalsIgnoreCase("super_admin")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Super Admins cannot promote others to Super Admin");
            }
        }

        Users user = userService.updateUser(id, updateuser);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN' , 'SUPER_ADMIN')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id , HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        String loggedInUsername = jwtUtil.extractusername(token);

        Users loggedInUser = userService.getUserByName(loggedInUsername)
                .orElseThrow(() -> new RuntimeException("Target not found"));

        Users targetUser = userService.getUserById(id)
                        .orElseThrow(() -> new RuntimeException("User not Found"));

        String loggedInRole = loggedInUser.getRole();
        String targetRole = targetUser.getRole();

        if (loggedInRole.equals("admin")) {
            if(!targetRole.equals("staff")){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admins can only delete a Staff");
            }
        } else if (loggedInRole.equals("super_admin")) {
            if (targetRole.equals("super_admin")){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Can not Delete other Super User");
            }
        }

        userService.deleteUser(id);
        return ResponseEntity.ok("User Deleted Successfully");
    }

    @PreAuthorize("hasAnyRole('STAFF' , 'ADMIN' , 'SUPER_ADMIN')")
    @PostMapping("/update/login/{username}")
    public ResponseEntity<String> updatelogin(@PathVariable String username){
         userService.updateLogin(username);
        return  ResponseEntity.ok("status updated");
    }

    @PreAuthorize("hasAnyRole('STAFF' , 'ADMIN' , 'SUPER_ADMIN')")
    @PostMapping("/update/logout/{username}")
    public ResponseEntity<String> updatelogoutstatus(@PathVariable String username){
        userService.updateLogout(username);
        return  ResponseEntity.ok("status updated");
    }

    @PreAuthorize("hasAnyRole('ADMIN' , 'SUPER_ADMIN')")
    @GetMapping("/count/active")
    public long ActiveUsersCount(){
        return userService.ActiveUsersCount();
    }

    @PreAuthorize("hasAnyRole('ADMIN' , 'SUPER_ADMIN')")
    @GetMapping("/active")
    public List<Users> ActiveUserst(){
        return userService.ActiveUsres();
    }

}