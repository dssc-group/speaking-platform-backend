package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentApplication {
    private Integer applicationId;
    public enum ApplicationStatus {
        pending, approved, rejected
    };
    private int studentId;
    private int classId;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime applicationDate;
    private LocalDateTime responseDate;
    private int teacherId;
//    private String nickName;
//    private String userStudentId;
//    private String className;
//    private String userName;
}
