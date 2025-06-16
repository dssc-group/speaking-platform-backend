package com.bupt.service;

import com.bupt.pojo.EvaluationDimension;
import com.bupt.pojo.Template;

public interface TemplateService {
    public Template createTemplate(Template template);

    public Template getTemplateByOwnerId(Integer ownerId);

    public Template addTemplateContent(String templateId,EvaluationDimension evaluationDimension);

}
