package com.leemanshow.app.domain.entity;

import lombok.Data;

@Data
public class QuestionEntity {
    /**
     * 问题
     */
    String title;
    /**
     * 答案
     */
    String answer;
    /**
     * 正确选项
     */
    String choice;
    /**
     * 入库时间
     */
    String createTime;
}
