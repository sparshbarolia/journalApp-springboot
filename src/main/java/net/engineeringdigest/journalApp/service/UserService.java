package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

//    iski jagah @Slf4j use krlia jo lombork provide krta h
//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<User> getAll() {
        return userRepository.findAll();
    }

    //The save() method in Spring Data MongoDB can perform an update if the object has an @Id field populated.
    //Otherwise, it will attempt to create a new document.
    public void saveEntry(User user) {
        userRepository.save(user);
    }

    //for /public/signup (POST)
    //jb input user me bs userName and password hoga tb hum search krenge in DB with userName
    //because of @Indexed(unique = true) in User.java
    //agr same userName ka user mil gya to entry save nhi krenge
    //for /user (PUT)
    //jb input user me id bhi hogi tb id ke base pr search krenge
    //or jo entry doge uske basis pr updation krdenge
    public boolean saveNewEntry(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            return true;
        }
        catch (Exception e){
            log.debug("Error occurred for {} :" , user.getUserName() , e);
            return false;
        }
    }

    public User saveNewAdminEntry(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER" , "ADMIN"));
        userRepository.save(user);
        return userRepository.findByUserName(user.getUserName());
    }

    public Optional<User> getEntryById(ObjectId inputId) {
        return userRepository.findById(inputId);
    }

    public void deleteById(ObjectId inputId) {
        userRepository.deleteById(inputId);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
}
