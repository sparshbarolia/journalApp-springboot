package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//the @RestController annotation is used to define a RESTful web service controller in a Spring Boot application.
// It tells Spring that this class is a controller that handles HTTP requests and automatically converts the
// returned values to JSON or other appropriate formats to be used as responses.
@RestController    //@Component included hota h @RestController me
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User myUser){

        //is line ka mtlb h ki user verify hogya ki usne sahi userName and password dala h
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //ye us verified user ka userName ladega
        String userName = authentication.getName();

        User userInDB = userService.findByUserName(userName);

        if(userInDB != null){
            userInDB.setUserName(myUser.getUserName());
            userInDB.setPassword(myUser.getPassword());

            userService.saveNewEntry(userInDB);

            return new ResponseEntity<>(userInDB , HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestBody User myUser){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        userRepository.deleteByUserName(userName);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
