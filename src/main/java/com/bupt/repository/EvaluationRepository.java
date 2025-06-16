package com.bupt.repository;

import com.bupt.pojo.Evaluation;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository extends MongoRepository<Evaluation,String> {
    public Optional<Evaluation> getEvaluationBySubmissionId(String submissionId);
    public Optional<Evaluation> getEvaluationByTaskIdAndStudentId(String taskId,Integer studentId);
    public List<Evaluation> getEvaluationByTaskIdAndStudentIdIn(String taskId, List<Integer> studentIds);
    public List<Evaluation> getEvaluationByTaskId(String taskId);
}
