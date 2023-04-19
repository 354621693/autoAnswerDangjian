package com.leemanshow.app.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.leemanshow.app.service.QuestionService;
import com.leemanshow.app.until.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

//@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ExtendWith({SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * mock 一个service的行为，通过测试方法中的when方法控制
     */
    // @Mock
    @Autowired
    private QuestionService questionService;

    @Test
    public void uploadControllerTest() throws Exception {
        String rightResult = "题目上传成功~";
        // when(questionService.insertQuestion(any(String.class))).thenReturn(rightResult);
        //void类型的service方法要特殊处理
        // doNothing().when(questionService).insertQuestion(any(String.class));
        ObjectNode objectNode = JacksonUtil.getObjectMapper().createObjectNode();
        objectNode.put("question", "中国第一个青年团早期组织（）社会主义青年团是于1920年8月诞生的。");
        objectNode.put("answer", "上海");
        objectNode.put("choice", "c");
        mockMvc.perform(MockMvcRequestBuilders.post("/dati/upload")
                        .header("hh", "hh")
                        .content(objectNode.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        // .andExpect(MockMvcResultMatchers.content().json());

    }

}