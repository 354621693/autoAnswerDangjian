package com.leemanshow.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leemanshow.app.domain.vo.FindQuestionVO;
import com.leemanshow.app.es.bo.QuestionSearchResult;

import java.io.IOException;

public interface QuestionService {
    QuestionSearchResult getQuestion(String question) throws IOException;

    void insertQuestion(String json) throws IOException;
}
