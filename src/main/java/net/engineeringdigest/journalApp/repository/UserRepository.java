package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

//using this we can do operations in mongoDB
// in <JournalEntry, ObjectId>
// JournalEntry means data in collection will be stored in this format
// ObjectId means ki jo id bnata h mongoDb vo ObjectId type ki hogi
public interface UserRepository extends MongoRepository<User, ObjectId> {

//    writing findByUserName(String userName); in your UserRepository interface is sufficient for Spring Boot to automatically implement the method, as long as you meet the following requirements:
//    1.Naming Convention: The method name findByUserName follows Spring Data's naming conventions. Spring interprets findByUserName as:
//
//    findBy: Indicates a query method.
//    UserName: Refers to a field named userName in the entity class User.

//    2.MongoRepository: The repository must extend MongoRepository

//    it will not work the same if you write findByUserName(String userName) in the UserService class. The reason is that Spring Boot’s automatic query method generation only works within repository interfaces (e.g., UserRepository) that extend Spring Data repositories like MongoRepository or CrudRepository.
    User findByUserName(String userName);
}
