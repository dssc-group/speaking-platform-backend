package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDTD {
    private Integer ownerId;
    private String templateId;
    private EvaluationDimension evaluationDimension;
}
