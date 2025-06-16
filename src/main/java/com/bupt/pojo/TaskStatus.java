package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatus {
    private String taskId;
    private String taskStatus;
}
