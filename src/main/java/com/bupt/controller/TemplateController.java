package com.bupt.controller;

import com.bupt.pojo.Result;
import com.bupt.pojo.Template;
import com.bupt.pojo.TemplateDTD;
import com.bupt.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class TemplateController {
    @Autowired
    TemplateService templateService;
    @PostMapping("/template/createTemplate")
    public Result addTemplate(@RequestBody Template template){
        return Result.success(templateService.createTemplate(template));
    }

    @GetMapping("/template/getTemplate")
    public Result getTemplate(@RequestParam("ownerId") Integer ownerId){
        return Result.success(templateService.getTemplateByOwnerId(ownerId));
    }

    @PostMapping("/template/addTemplateContent")
    public Result addTemplateContent(@RequestBody TemplateDTD templateDTD){
        return Result.success(templateService.addTemplateContent(templateDTD.getTemplateId(),templateDTD.getEvaluationDimension()));
    }
}
