package com.leemanshow.app.domain.vo;

import com.leemanshow.app.domain.entity.QuestionEntity;
import com.leemanshow.app.es.bo.QuestionSearchResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Deprecated
public class FindQuestionVO {
    boolean isFound;
    List<QuestionEntity> questionEntityList;

    public FindQuestionVO(QuestionSearchResult searchResult){
        this.isFound = searchResult.hasResult();
        this.questionEntityList = searchResult.getQuestionList();
    }
}
