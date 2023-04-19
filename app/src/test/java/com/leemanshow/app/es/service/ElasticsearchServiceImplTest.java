package com.leemanshow.app.es.service;

import com.leemanshow.app.domain.entity.QuestionEntity;
import com.leemanshow.app.es.bo.QuestionSearchParam;
import com.leemanshow.app.es.bo.QuestionSearchResult;
import com.leemanshow.app.until.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
class ElasticsearchServiceImplTest {
    @Autowired
    ElasticsearchService service;

    @Test
    @DisplayName("测试es查询题库")
    void searchQuestion() throws IOException {
        QuestionSearchParam questionSearchParam = new QuestionSearchParam();
        questionSearchParam.setQuestion("伟大");
        QuestionSearchResult result = service.searchQuestion(questionSearchParam);
        log.info(JacksonUtil.getObjectMapper().writeValueAsString(result));
    }

    @Test
    @DisplayName("测试es插入题目")
    void insertQuestion() throws IOException {
        QuestionEntity entity = new QuestionEntity();
        entity.setQuestion("深入学习和贯彻“创新、协调、绿色、开放、共享”五大发展理念对破解我国发展难题具有重要意义，其中“协调”发展理念致力于破解（）难题。");
        entity.setAnswer("发展结构失衡");
        entity.setChoice(null);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        entity.setCreateTime(now.format(formatter));

        service.InsertQuestion(entity);
    }


}