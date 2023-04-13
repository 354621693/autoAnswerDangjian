package com.leemanshow.app.domain.vo;

import com.leemanshow.app.domain.entity.QuestionEntity;
import lombok.Data;

import java.util.List;

@Data
public class FindQuestionVO {
    boolean isFound;
    List<QuestionEntity> questionEntityList;
}
