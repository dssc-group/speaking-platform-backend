package com.bupt.repository;

import com.bupt.pojo.Template;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TemplateRepository extends MongoRepository<Template,String> {
    Optional<Template> getTemplatesById(String Id);
    Optional<Template> getTemplatesByOwnerId(Integer ownerId);
}
