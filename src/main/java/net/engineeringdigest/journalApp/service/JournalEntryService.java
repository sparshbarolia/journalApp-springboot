package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    @Transactional
    public void saveEntry(JournalEntry journalEntry ,String userName){
        try {
            //user find kia
            User currUser = userService.findByUserName(userName);

            //journal entry bnai and save krai
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);

            //user ki journalEntries (List) me add krdi currently created journal entry
            currUser.getJournalEntries().add(saved);

            //user save kradia with updated journalEnrty
            //agr pehle se userId h,usi userId ke liye agr ek or user save kraenge
            //to purani userEntry ki jagah new userEntry update hojaegi
            userService.saveEntry(currUser);
        }
        catch (Exception e){
            System.out.println(e);
            //ye error isliye throw kia h qki @Transactional ko pta chlna chahiye ki kuchh code fata h
            //vrna vo rollback nhi krega changes ko
            //vrna try me dikkat aane ke baad catch me ajaenge or kuchh error throw nhi hoga
            //to rollback nhi hoga
            throw new RuntimeException("An error occurred while saving the entry.",e);
        }
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public Optional<JournalEntry> getEntryById(ObjectId inputId){
        return journalEntryRepository.findById(inputId);
    }

    @Transactional
    public boolean deleteById(ObjectId inputId , String userName){
        boolean removed = false;
        try {
            User currUser = userService.findByUserName(userName);

            removed = currUser.getJournalEntries().removeIf(x -> x.getId().equals(inputId)); // for loop shortcut

            if(removed){
                //same id ka user save kr rhe h jo pehle se db me h
                //to purane user ki jagah updated user save hojaega
                userService.saveEntry(currUser);

                journalEntryRepository.deleteById(inputId);
            }
        }
        catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occured while deleting the entry." , e);
        }
        return removed;
    }
}
