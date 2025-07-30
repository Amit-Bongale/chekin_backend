package com.example.checkin.service;
import com.example.checkin.models.Users;
import com.example.checkin.repo.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public Users saveuser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<Users> getallUserss(){
        return userRepository.findAll();
    }

    public Users updateUser(Long id , Users updateuser){
        Users existinguser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not Found"));

        existinguser.setUsername(updateuser.getUsername());
        existinguser.setRole(updateuser.getRole());
        existinguser.setMobile(updateuser.getMobile());

        if(!updateuser.getPassword().equals(existinguser.getPassword())){
            existinguser.setPassword(encoder.encode(updateuser.getPassword()));
        }

        return userRepository.save(existinguser);
    }

    public void deleteUser(Long id){
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not Found");
        }

        userRepository.deleteById(id);
    }

    public Users updateLogin(String username){
        return userRepository.findByUsername(username)
                .map(user -> {
                    user.setStatus(true);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public Users updateLogout(String username){
        return userRepository.findByUsername(username)
                .map(user -> {
                    user.setStatus(false);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public List<Users> ActiveUsres(){
        return userRepository.findByStatusTrue();
    }

    public long ActiveUsersCount(){
        return  userRepository.countByStatusTrue();
    }


}
