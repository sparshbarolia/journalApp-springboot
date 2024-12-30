package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
public class User {
    @Id
    private ObjectId id;
    //@Indexed ke liye application properties me ek line add krni pdti h
    //iski help se searching along userName becomes faster
    @Indexed(unique = true)
    @NonNull
    private String userName;
    @NonNull
    private String password;
    //is se ab link bn gya between user and JournalEntry
    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();
}
