package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationApproval {
    private Integer applicationId;
    private String approvalStatus;
    private LocalDateTime responseDate;
}
