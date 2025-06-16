package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
@Document(collection = "TemplateCollection")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Template {
    @Id
    private String id;
    private Integer ownerId;
    private List<EvaluationDimension> templateInfos;
}
