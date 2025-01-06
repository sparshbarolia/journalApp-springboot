package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

//ye bnaya h spring security ke liye bs
//config -> SpringSecurity me use kia h ise

//How It Works in Spring Security
//  1.Authentication Process :
//  .When a user tries to log in, Spring Security calls the loadUserByUsername method with the provided username.
//  .This method retrieves the user details and converts them into a UserDetails object.

//  2.Password Verification
//  .Spring Security automatically compares the password provided in the login request with the one stored in the database (handled internally).

//  3.Role-based Authorization
//  .The roles provided in the UserDetails object are used by Spring Security to determine the user's access rights to various resources.

@Component
public class UserDetailsServiceImplementaion implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        if(user != null){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())  // Map your entity's `userName` to Spring Security's username
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))  //string type ki array me covert krdia List<String>  ko
                    .build();
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
