package net.engineeringdigest.journalApp.config;

import net.engineeringdigest.journalApp.service.UserDetailsServiceImplementaion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImplementaion userDetailsServiceImplementaion;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/journal/**" , "/user/**").authenticated()      // /journal/-- , /user/--  jaise endpoints ko secure krdo
                .antMatchers("/admin/**").hasRole("ADMIN")       //agr roles List me ADMIN h to hi access milega /admin vale endpoints ka
                .anyRequest().permitAll()                                   //baki sb request ko permit krdo
                .and()
                .httpBasic();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  //Specifies that the application will not use HTTP sessions to store authentication information.
                .and()
                .csrf().disable();  //In stateless APIs (e.g., RESTful APIs), CSRF protection is usually unnecessary because authentication tokens (e.g., JWTs) are used for each request instead of relying on cookies, which are more vulnerable to CSRF attacks.
    }


    //ye neeche ke 2 functions help krte h password verify krne me
    //jo password daloge usko hashed password ke sath verify krte h
    //The UserDetailsServiceImplementation is registered here to provide user data for authentication.
    //A password encoder (e.g., BCryptPasswordEncoder) is used to handle encrypted passwords.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImplementaion).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
