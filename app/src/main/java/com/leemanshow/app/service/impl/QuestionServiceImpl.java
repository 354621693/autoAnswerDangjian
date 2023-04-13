package com.leemanshow.app.service.impl;

import com.leemanshow.app.domain.vo.FindQuestionVO;
import com.leemanshow.app.es.service.ElasticsearchService;
import com.leemanshow.app.service.QuestionService;
import org.elasticsearch.client.ElasticsearchClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Resource
    ElasticsearchService esService;

    @Override
    public FindQuestionVO getQuestion(String question) {
        return null;
    }
}
