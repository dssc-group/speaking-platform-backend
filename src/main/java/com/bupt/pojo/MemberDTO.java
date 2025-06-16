package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private Integer page;
    private Integer pageSize;
    private List<Integer> stuIds;
    private String stuIndex;
    private String name;
    private String mail;
}
