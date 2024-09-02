package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private Integer F_id;
    private Integer F_enable;
    private String F_group_member_id;
    private String F_group_member_name;
    private String F_group_member_nickname;
    private String F_group_id;
    private String F_group_name;
    private Integer F_role;
    private Integer F_status;
    private LocalTime F_create_time;
    private LocalTime F_modify_time;
    private Integer F_group_type;
    private Integer F_score;
    private Integer F_member_type;
    private Integer F_complete_words;
    private Integer F_complete_tasks;
    private String F_mobile;
    private String F_member_info_status;
    private Integer F_order_enabled;
    private Integer F_active_status;
    private LocalTime F_active_time;
    private String F_price_id;
    private String F_student_id;
    private String F_class_id;
    private LocalTime F_birthday;
    private String F_major;
    private String F_college;
    private String F_sex;
}
