package com.leemanshow.app.es.service;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Resource
    private RestHighLevelClient client;

    public String find(String query){
        return null;
    }

    void testCreateIndex() throws IOException {

        CreateIndexRequest request = new CreateIndexRequest("books");
        client.indices().create(request, RequestOptions.DEFAULT);

    }
}
