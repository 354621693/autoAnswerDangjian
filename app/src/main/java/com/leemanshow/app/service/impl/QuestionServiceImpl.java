package com.leemanshow.app.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.leemanshow.app.domain.entity.QuestionEntity;
import com.leemanshow.app.es.bo.QuestionSearchParam;
import com.leemanshow.app.es.bo.QuestionSearchResult;
import com.leemanshow.app.es.service.ElasticsearchService;
import com.leemanshow.app.service.QuestionService;
import com.leemanshow.app.until.JacksonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Resource
    ElasticsearchService esService;

    @Override
    public QuestionSearchResult getQuestion(String question) throws IOException {
        QuestionSearchParam searchParam = new QuestionSearchParam();
        searchParam.setTitle(question);
        return esService.searchQuestion(searchParam);
    }

    @Override
    public void insertQuestion(String json) throws IOException {
        JsonNode jsonNode = JacksonUtil.getObjectMapper().readTree(json);
        QuestionEntity entity = new QuestionEntity();
        entity.setTitle(jsonNode.get("title").asText());
        entity.setAnswer(jsonNode.get("answer").asText());
        entity.setChoice(jsonNode.get("choice").asText());
        entity.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        esService.InsertQuestion(entity);
    }
}
