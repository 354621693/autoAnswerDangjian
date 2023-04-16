package com.leemanshow.app.es.bo;

import com.leemanshow.app.domain.entity.QuestionEntity;
import lombok.Data;

import java.util.List;

/**
 * es搜索响应体
 */
@Data
public class QuestionSearchResult {
    /**
     * 查询到的所有题目信息信息
     */
    private List<QuestionEntity> questionList;

    /*** 当前页码*/
    private Integer pageNum;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 总页码
     */
    private Integer totalPages;

    public boolean hasResult() {
        return this.questionList.size() > 0;
    }
}
