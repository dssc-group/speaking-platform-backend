package com.bupt.repository;

import com.bupt.pojo.ReviewTask;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<ReviewTask,String> {
    public List<ReviewTask> findByStudentId(Integer studentId);
    public List<ReviewTask> findByReviewerId(Integer reviewerId);
    public List<ReviewTask> getReviewTasksByIdIn(List<String> reviewTaskIds);
    public Optional<ReviewTask> findByTaskIdAndAndStudentIdAndAndReviewType(String taskId, Integer studentId,String reviewType);
    public List<ReviewTask> findByTaskIdAndStudentIdIn(String taskId,List<Integer> studentIds);
}
