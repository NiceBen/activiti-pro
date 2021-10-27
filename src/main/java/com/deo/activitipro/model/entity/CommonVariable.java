package com.deo.activitipro.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 实体类
 *
 * @date 2021-10-27
 * @since 1.0.0
 */
@Data
@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
public class CommonVariable {

    private List<String> signList;

    private Integer day;

    private Integer amount;

    private Integer val;

    private Integer pay;

    private Integer order;

    private Integer sendout;

    private Integer pass;
}
