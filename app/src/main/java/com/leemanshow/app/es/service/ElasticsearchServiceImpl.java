package com.leemanshow.app.es.service;

import com.leemanshow.app.domain.entity.QuestionEntity;
import com.leemanshow.app.es.ElasticSearchConstant;
import com.leemanshow.app.es.bo.QuestionSearchParam;
import com.leemanshow.app.es.bo.QuestionSearchResult;
import com.leemanshow.app.until.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.ActiveShardCount;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ElasticsearchServiceImpl implements ElasticsearchService {
    @Resource
    private RestHighLevelClient client;

    @Override
    public QuestionSearchResult searchQuestion(QuestionSearchParam query) throws IOException {

        SearchRequest searchRequest = buildSearchRequest(query);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        QuestionSearchResult result = new QuestionSearchResult();
        if (searchResponse.getHits().getTotalHits().value > 0) {
            List<QuestionEntity> list = new ArrayList<>();
            for (SearchHit hit : searchResponse.getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                // 处理搜索结果
                sourceAsMap.forEach((k, v) -> {
                    QuestionEntity questionEntity = new QuestionEntity();
                    questionEntity.setAnswer((String) sourceAsMap.get("answer"));
                    questionEntity.setTitle((String) sourceAsMap.get("title"));
                    questionEntity.setChoice((String) sourceAsMap.get("choice"));
                    list.add(questionEntity);
                });
                result.setQuestionList(list);
            }
        }
        return result;
    }

    @Override
    public void InsertQuestion(QuestionEntity questionEntity) throws IOException {
        IndexRequest indexRequest = new IndexRequest(ElasticSearchConstant.INDEX_NAME_QUESTIONS);
//                .source(XContentFactory.jsonBuilder()
//                        .startObject()
//                        .field("title", questionEntity.getTitle())
//                        .field("answer", questionEntity.getAnswer())
//                        .field("choice", questionEntity.getChoice())
//                        .field("creatTime", questionEntity.getCreateTime())
//                        .endObject()
//                );
        String questionJson = JacksonUtil.getObjectMapper().writeValueAsString(questionEntity);
        // indexRequest.source(XContentType.JSON,"title","正式党员不足7人的党支部，设1名书记，1名副书记（）","answer","错");
        indexRequest.source(questionJson, XContentType.JSON);
        //计算md5作为es文档的id，可以避免重复插入的问题。但是在多节点es中，可能被重复插到不同的分片中，所以必要时要在查询时使用聚合来去重。
        String md5 = DigestUtils.md5DigestAsHex(questionJson.getBytes(StandardCharsets.UTF_8));
        indexRequest.id("sd");
        // 目前spirng data只支持7.x及其以下的es版本 暂时用try处理
        try {
            client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            if (!e.getMessage().contains("201 Created") && !e.getMessage().contains("200 OK")) {
                throw e;
            }
        }
    }

    private SearchRequest buildSearchRequest(QuestionSearchParam query) {
        // 构建DSL
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (StringUtils.isNotBlank(query.getTitle())) {
            boolQuery.must(QueryBuilders.matchQuery("title", query.getTitle()));
        }
        if (StringUtils.isNotBlank(query.getAnswer())) {
            boolQuery.must(QueryBuilders.matchQuery("answer", query.getAnswer()));
        }
        if (StringUtils.isNotBlank(query.getCreateTime())) {
            boolQuery.must(QueryBuilders.matchQuery("creatTime", query.getCreateTime()));
        }
        sourceBuilder.query(boolQuery);
        //排序
        if (ObjectUtils.isNotEmpty(query.getSort()) && query.getSort().size() > 0) {
            query.getSort().forEach(v -> sourceBuilder.sort(v.getFieldName(), v.getSortOrder()));
        }

        SearchRequest request = new SearchRequest();
        request.indices(QuestionSearchParam.INDEX_NAME);
        request.source(sourceBuilder);
        log.info("DSL:{}", sourceBuilder);
        return request;
    }

    /**
     * 创建党建问题的索引
     */
    void createQuestionsIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("questions");
        //指定映射方式2  索引名称必须小写  这个测试成功了 但是只有一个字段 如果想要多个字段呢?
        Map<String, Object> title = new HashMap<>();
        title.put("type", "text");
        title.put("analyzer", "ik_smart");

        Map<String, Object> answer = new HashMap<>();
        answer.put("type", "text");
        answer.put("analyzer", "ik_smart");
        //为字段声明映射的数据类型
        Map<String, Object> choice = new HashMap<>();
        choice.put("type", "keyword");

        //把声明好的字段塞到配置中
        Map<String, Object> properties = new HashMap<>();
        properties.put("title", title);
        properties.put("answer", answer);
        properties.put("choice", choice);

        //把配置塞到映射中
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);
        createIndexRequest.mapping(mapping);
        //设置别名
        createIndexRequest.alias(new Alias("questions"));

        // 额外参数
        //设置超时时间
        createIndexRequest.setTimeout(TimeValue.timeValueMinutes(2));
        //设置主节点超时时间
        createIndexRequest.setMasterTimeout(TimeValue.timeValueMinutes(1));
        //在创建索引API返回响应之前等待的活动分片副本的数量,以int形式表示
        createIndexRequest.waitForActiveShards(ActiveShardCount.from(0));
        createIndexRequest.waitForActiveShards(ActiveShardCount.DEFAULT);


        //操作索引的客户端
        IndicesClient indices = client.indices();
        //执行创建索引库
        CreateIndexResponse response = indices.create(createIndexRequest, RequestOptions.DEFAULT);

        log.info("创建索引完成，结果：{}", JacksonUtil.getObjectMapper().writeValueAsString(response));

    }


}
