package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

//using this we can do operations in mongoDB
// in <JournalEntry, ObjectId>
// JournalEntry means data in collection will be stored in this format
// ObjectId means ki jo id bnata h mongoDb vo ObjectId type ki hogi
public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {

}
