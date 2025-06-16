package com.bupt.repository;

import com.bupt.pojo.Task;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByOwnerId(Integer ownerId);
    Optional<Task> findById(String id);

}
