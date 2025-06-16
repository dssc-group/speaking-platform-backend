package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationDimension {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EvaluationTitles{
        private String evaluationTitle;//标题
        List<String> evaluationInfos;//评价详细信息
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvaluationType{ //评价种类
        private String evaluationContent; //视频、音频
        private List<EvaluationTitles> evaluationTitles;
    }
    private String evaluationMethods; //互评、师评、自评
    private List<EvaluationType> evaluationTypes;
}
