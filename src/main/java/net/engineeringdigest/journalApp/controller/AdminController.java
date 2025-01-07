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
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(){
        List<User> all = userService.getAll();

        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all , HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> cerateAdminUser(@RequestBody User myUser){
        User newUser = userService.saveNewAdminEntry(myUser);
        if(newUser != null){
            return new ResponseEntity<>(newUser , HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
