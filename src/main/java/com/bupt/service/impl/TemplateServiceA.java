package com.bupt.service.impl;

import com.bupt.pojo.EvaluationDimension;
import com.bupt.pojo.Template;
import com.bupt.repository.TemplateRepository;
import com.bupt.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplateServiceA implements TemplateService {
    @Autowired
    private TemplateRepository templateRepository;
    @Override
    public Template createTemplate(Template template) {

        return templateRepository.getTemplatesByOwnerId(template.getOwnerId())
                .orElseGet(()-> templateRepository.save(template));
    }

    @Override
    public Template getTemplateByOwnerId(Integer ownerId) { //
        return templateRepository.getTemplatesByOwnerId(ownerId)
                .orElse(null);
    }

    @Override
    public Template addTemplateContent(String templateId,EvaluationDimension evaluationDimension) {
        Template template = templateRepository.getTemplatesById(templateId).get();
        EvaluationDimension changedEvaluationDimension = template.getTemplateInfos().stream()
                .filter(templateInfo -> templateInfo.getEvaluationMethods().equals(evaluationDimension.getEvaluationMethods()))
                .findFirst().get();
        for(EvaluationDimension.EvaluationType evaluationType:evaluationDimension.getEvaluationTypes()){
            EvaluationDimension.EvaluationType changedEvaluationType = changedEvaluationDimension.getEvaluationTypes().stream()
                    .filter(evaluationType1 -> evaluationType1.getEvaluationContent().equals(evaluationType.getEvaluationContent()))
                    .findFirst().get();
            changedEvaluationType.getEvaluationTitles().addAll(evaluationType.getEvaluationTitles());
        }

        return templateRepository.save(template);
    }
}
