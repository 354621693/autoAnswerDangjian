package com.leemanshow.app.controller;

import com.leemanshow.app.common.ResponseVO;
import com.leemanshow.app.es.bo.QuestionSearchResult;
import com.leemanshow.app.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 题目服务入口
 */

@RestController
@RequestMapping("/dati")
public class QuestionController {
    @Resource
    QuestionService questionService;


    @GetMapping("/search")
    public ResponseVO<QuestionSearchResult> findQuestions(String question) throws IOException {
        return ResponseVO.success(questionService.getQuestion(question));
    }

    @PostMapping("/upload")
    public ResponseVO<Void> uploadQuestion(@RequestBody String json) throws IOException {
        questionService.insertQuestion(json);
        return ResponseVO.success(null, "题目上传成功~");
    }
}
