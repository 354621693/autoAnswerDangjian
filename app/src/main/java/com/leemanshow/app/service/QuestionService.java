package com.leemanshow.app.service;

import com.leemanshow.app.domain.vo.FindQuestionVO;

public interface QuestionService {
    FindQuestionVO getQuestion(String question);
}
