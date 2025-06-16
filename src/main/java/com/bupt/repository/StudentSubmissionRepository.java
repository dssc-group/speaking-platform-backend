package com.bupt.repository;

import com.bupt.pojo.StudentApplication;
import com.bupt.pojo.StudentSubmission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StudentSubmissionRepository extends MongoRepository<StudentSubmission,String> {
    Optional<StudentSubmission> findByTaskIdAndStudentId(String taskId,Integer studentId);
    List<StudentSubmission> findByStudentId(Integer studentId);
    List<StudentSubmission> findByTaskIdAndStudentIdIn(String taskId,List<Integer> studentIds);
    List<StudentSubmission> findStudentSubmissionByTaskId(String taskId);

}
