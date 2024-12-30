package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<User> getUser(){
        return userService.getAll();
    }

    @PostMapping
    public boolean saveUser(@RequestBody User newUser){
        userService.saveEntry(newUser);
        return true;
    }

    @PutMapping("{userName}")
    public ResponseEntity<?> updateUser(@RequestBody User myUser , @PathVariable String userName){
        User userInDB = userService.findByUserName(userName);

        if(userInDB != null){
            userInDB.setUserName(myUser.getUserName());
            userInDB.setPassword(myUser.getPassword());

            userService.saveEntry(userInDB);

            return new ResponseEntity<>(userInDB , HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
