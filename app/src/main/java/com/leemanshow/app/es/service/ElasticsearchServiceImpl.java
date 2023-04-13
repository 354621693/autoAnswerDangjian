package com.leemanshow.app.es.service;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Resource
    private RestHighLevelClient client;

    public String find(String query){
        return null;
    }
}
