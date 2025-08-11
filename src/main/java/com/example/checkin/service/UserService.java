package com.example.checkin.service;
import com.example.checkin.models.Users;
import com.example.checkin.models.UsersLog;
import com.example.checkin.repo.UserRepository;
import com.example.checkin.repo.UsersLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersLogRepository usersLogRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users saveuser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<Users> getallUserss(){
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Optional<Users> getUserByName(String username){
       return userRepository.findByUsername(username);
    }

    public Optional<Users> getUser(Long id){
        return userRepository.findById(id);
    }

    public Users updateUser(Long id , Users updateuser){
        Users existinguser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not Found"));

        existinguser.setUsername(updateuser.getUsername());
        existinguser.setRole(updateuser.getRole());
        existinguser.setMobile(updateuser.getMobile());

        if (updateuser.getPassword() != null && !updateuser.getPassword().isBlank()) {
            if(!updateuser.getPassword().equals(existinguser.getPassword())){
                existinguser.setPassword(encoder.encode(updateuser.getPassword()));
            }
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
        Users users = userRepository.findByUsername(username)
                .map(user -> {
                    user.setStatus(true);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        UsersLog userlog = new UsersLog();
        userlog.setUsername(users.getUsername());
        userlog.setRole(users.getRole());
        userlog.setLoginTime(LocalDateTime.now());
        usersLogRepository.save(userlog);

        return  users;

    }

    public Users updateLogout(String username){
        Users users = userRepository.findByUsername(username)
                .map(user -> {
                    user.setStatus(false);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        List<UsersLog> logs = usersLogRepository.findByUsername(username);
        Optional<UsersLog> lastlog = logs.stream().filter(log -> log.getLogoutTime() == null )
                .sorted(Comparator.comparing(UsersLog::getLoginTime).reversed()).findFirst();

        lastlog.ifPresent(log -> {
            log.setLogoutTime(LocalDateTime.now());
            usersLogRepository.save(log);
        });

        return users;
    }

    public List<Users> ActiveUsres(){
        return userRepository.findByStatusTrue();
    }

    public long ActiveUsersCount(){
        return  userRepository.countByStatusTrue();
    }


    public List<Users> findUserByRole(String staff) {
        return userRepository.findByRole(staff);
    }

    public List<Users> findUserByRoleNot(String superAdmin) {
        return userRepository.findByRoleNot(superAdmin);
    }
}
