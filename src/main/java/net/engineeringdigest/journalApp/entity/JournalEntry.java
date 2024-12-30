package net.engineeringdigest.journalApp.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

//@Component    //with this also it is working(don't know how)(badme err aane lga tha pr startin me work kr rha tha)
//@Document mtlb ye class mongoDb se integrated h
//or jo bhi collection ka naam doge us se map hojaegi ye class
//agr collection bnayi nhi h to new collection bn jaegi is naam ki
@Document(collection = "journal_entries")
//Annotations for lambok
//@Getter
//@Setter
// OR
@Data
@NoArgsConstructor  //iske bina error arha tha
public class JournalEntry {

    //mtlb ye hogi id mongoDb me
    @Id
    private ObjectId id;   //in mongoDB, id is generated of type ObjectId
    @NonNull
    private String title;

    private String content;

    private LocalDateTime date;


//    public ObjectId getId() {
//        return id;
//    }
//
//    public void setId(ObjectId id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public LocalDateTime getDate() {
//        return date;
//    }
//
//    public void setDate(LocalDateTime date) {
//        this.date = date;
//    }
}
