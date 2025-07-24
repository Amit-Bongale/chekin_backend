package com.example.checkin.service;

import com.example.checkin.models.Users;
import com.example.checkin.repo.UserRepository;
import io.jsonwebtoken.security.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users saveuser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Users updateUser(Long id , Users updateuser){
        Users existinguser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not Found"));

        existinguser.setUsername(updateuser.getUsername());
        existinguser.setRole(updateuser.getRole());
        existinguser.setMobile(updateuser.getMobile());

        if(!existinguser.getPassword().equals(existinguser.getPassword())){
            existinguser.setPassword(encoder.encode(existinguser.getPassword()));
        }

        return userRepository.save(existinguser);
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not Found");
        }

        userRepository.deleteById(id);
    }
}
