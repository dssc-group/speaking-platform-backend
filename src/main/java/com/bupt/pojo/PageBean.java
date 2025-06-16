package com.bupt.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageBean<T> {
    private Long total;
    private List<T> rows;
}
