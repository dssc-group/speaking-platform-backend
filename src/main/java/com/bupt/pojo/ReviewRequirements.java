package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ReviewRequirementsCollection")
public class ReviewRequirements {
    public static class ReviewRequirement{
        String reviewTitle;
        List<String> reviewDimensions;
    }
    @Id
    String reviewType;
    List<ReviewRequirements> reviewRequirements;
}
