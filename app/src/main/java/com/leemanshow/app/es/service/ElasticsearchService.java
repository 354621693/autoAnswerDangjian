package com.leemanshow.app.es.service;

import com.leemanshow.app.domain.entity.QuestionEntity;
import com.leemanshow.app.es.bo.QuestionSearchParam;
import com.leemanshow.app.es.bo.QuestionSearchResult;

import java.io.IOException;

public interface ElasticsearchService {

    public QuestionSearchResult searchQuestion(QuestionSearchParam query) throws IOException;

    public void InsertQuestion(QuestionEntity questionEntity) throws IOException;

}
