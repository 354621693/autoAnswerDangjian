package com.leemanshow.app.es.bo;

import com.leemanshow.app.es.ElasticSearchConstant;
import lombok.Data;

import java.util.List;

/**
 * es查询题库的查询体
 */
@Data
public class QuestionSearchParam {
    public final static String INDEX_NAME = ElasticSearchConstant.INDEX_NAME_QUESTIONS;

    private String title;
    private String answer;
    private String createTime;
    // 查询条件 sort:[{"createTime":" asc/desc"}]
    private List<SortEntity> sort;
}
