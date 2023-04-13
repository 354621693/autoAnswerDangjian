package com.leemanshow.app.controller;

import com.leemanshow.app.common.ResponseVO;
import com.leemanshow.app.domain.vo.FindQuestionVO;
import com.leemanshow.app.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 题目服务入口
 */

@RestController
@RequestMapping("/dati")
public class QuestionController {
    @Resource
    QuestionService questionService;


    @GetMapping("/questions")
    public ResponseVO<FindQuestionVO> findQuestions(String question){
        return ResponseVO.success(questionService.getQuestion(question));
    }
}
