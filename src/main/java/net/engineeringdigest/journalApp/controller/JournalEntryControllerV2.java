package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//the @RestController annotation is used to define a RESTful web service controller in a Spring Boot application.
// It tells Spring that this class is a controller that handles HTTP requests and automatically converts the
// returned values to JSON or other appropriate formats to be used as responses.
@RestController    //@Component included hota h @RestController me
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(){

        //is line ka mtlb h ki user verify hogya ki usne sahi userName and password dala h
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //ye us verified user ka userName ladega
        String userName = authentication.getName();

        User currUser = userService.findByUserName(userName);

        List<JournalEntry> allEntries = currUser.getJournalEntries();

        if(allEntries != null && !allEntries.isEmpty()){
            //iska mtlb sb entries ke sath .OK HttpStatus bhej do
            return new ResponseEntity<>(allEntries , HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //@RequestBody is like saying , "Hey Spring,please take the
    //Data from the request and turn it into a java object that
    //I can use in my code
    //jo input hum postman me denge vo requestBosy use krlega idhr
    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){
        try {
            //is line ka mtlb h ki user verify hogya ki usne sahi userName and password dala h
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //ye us verified user ka userName ladega
            String userName = authentication.getName();

            journalEntryService.saveEntry(myEntry , userName);
            //iska mtlb myEntry ke sath .CREATED HttpStatus bhej do
            return new ResponseEntity<>(myEntry , HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //"id/{myId}" defines the URL path for the request. The curly braces ({}) indicate a path variable.
    // This means that any value provided in the place of {myId} in the URL will be captured and passed
    // as a method parameter.
    //Example request URL: /journal/id/123
    //Here, 123 would be captured as the value for myId.
    //The @PathVariable annotation tells Spring to extract the value of myId from the URL path.
    //In the example URL /journal/id/123, myId would be 123, and it will be automatically converted
    // from a String to a Long.
    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
        //hum chahte h ki ek user apni hi journalEnteries dekh ske
        //isliye chk kro ki jo myId user ne di h vo uski journalEntries me lie krti bhi h ya nhi

        //get the logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User currUser = userService.findByUserName(userName);

        //user.getJournalEntries() retrieves the list of all journal entries for the user.
        //stream() converts this list into a Stream, enabling functional-style operations.(filter,map,reduce bs stream pr hi lgta h)
        //filter(x -> x.getId().equals(myId)) filters the stream, retaining only the journal entries where the id matches the provided myId.
        //collect(Collectors.toList()) converts the filtered stream back into a List.
        List<JournalEntry> collect = currUser.getJournalEntries().stream().filter( x -> x.getId().equals(myId)).collect(Collectors.toList());

        if(!collect.isEmpty()){
            //optional use kro,agr value nhi hui to isPresent se pta chl jaega asani se
            //code simple bnata h ye bs
            Optional<JournalEntry> myEntry = journalEntryService.getEntryById(myId);

            //If a value is present, isPresent() returns true. If no value is present,
            //the object is considered empty and isPresent() returns false.
            if(myEntry.isPresent()){
                //.get Returns the non-null value described by this Optional
                return new ResponseEntity<>(myEntry.get(), HttpStatus.OK);
            }
        }


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    //? likhne pr , JournalEntry likhne ki jaroorat nhi pdegi
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId){

        //get the logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        boolean removed = journalEntryService.deleteById(myId , userName);

        if(removed) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateJournalEntryById(
            @PathVariable ObjectId myId ,
            @RequestBody JournalEntry myEntry
    ){

        //get the logged in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        //check if entry belongs to the user or not
        User currUser = userService.findByUserName(userName);
        List<JournalEntry> collect = currUser.getJournalEntries().stream().filter( x -> x.getId().equals(myId)).collect(Collectors.toList());

        if(!collect.isEmpty()){
            JournalEntry oldEntry = journalEntryService.getEntryById(myId).orElse(null);
            if(oldEntry != null){
                oldEntry.setTitle(myEntry.getTitle() != null && !myEntry.getTitle().equals("") ? myEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(myEntry.getContent() != null && !myEntry.getContent().equals("") ? myEntry.getContent() : oldEntry.getContent());

                journalEntryService.saveEntry(oldEntry);

                return new ResponseEntity<>(oldEntry,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
