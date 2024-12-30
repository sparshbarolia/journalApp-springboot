package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void saveEntry(User user) {
        userRepository.save(user);
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
