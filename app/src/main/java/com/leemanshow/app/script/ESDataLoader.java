package com.leemanshow.app.script;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ESDataLoader {

    public static void main(String[] args) throws IOException {
        List<String> list = loadJsonFromFile();
        bulkInsertJsonList(getClient(), "questions", list, 100);
    }

    private static RestHighLevelClient getClient() {
        HttpHost host = HttpHost.create("http://114.132.175.198:9200");
        RestClientBuilder builder = RestClient.builder(host);
        return new RestHighLevelClient(builder);
    }

    private static List<String> loadJsonFromFile() throws IOException {
        // 读取本地JSON文件
        File file = new File("C:/Users/35462/Downloads/tk.json");
        String jsonStr = FileUtils.readFileToString(file, Charset.defaultCharset());

        // 将JSON字符串解析为Jackson的JsonNode对象
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonStr);
        // 遍历JSON节点并输出
        List<String> result = new ArrayList<>();
        if (rootNode.isArray()) {
            for (JsonNode jsonNode : rootNode) {
//                Map<String, Object> map = objectMapper.convertValue(jsonNode, Map.class);
                String s = jsonNode.toString();
                result.add(s);
            }

        } else {
//            Map<String, Object> resultMap = objectMapper.convertValue(rootNode, Map.class);
            String s = rootNode.toString();
            result.add(s);
            for (JsonNode node : rootNode) {
                System.out.println(node.toString());
            }
        }
        return result;
    }


    public static void bulkInsertJsonList(RestHighLevelClient client, String index, List<String> jsonList, int batchSize) {
        BulkRequest bulkRequest = new BulkRequest();
        int count = 0;

        ObjectMapper objectMapper = new ObjectMapper();  // 创建 ObjectMapper 对象

        for (String json : jsonList) {
            try {
                Map<String, Object> map = objectMapper.readValue(json, Map.class);  // 将 JSON 字符串解析为 Map 对象
                IndexRequest indexRequest = new IndexRequest(index)
                        .source(map, XContentType.JSON);  // 将 Map 对象设置到 IndexRequest 的 source 中
                bulkRequest.add(indexRequest);  // 将 IndexRequest 对象添加到 BulkRequest 对象中
                count++;

                if (count == batchSize) {
                    try {// 如果满足了 BulkRequest 请求的文档数量条件，则执行 BulkRequest 请求
                        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);  // 执行 BulkRequest 请求
                        if (bulkResponse.hasFailures()) {  // 判断 BulkRequest 请求是否有失败的操作
                            // 处理失败情况
                            System.out.println("BulkRequest has failures: " + bulkResponse.buildFailureMessage());
                        }
                    }catch (IOException e){
                        log.warn("es 版本高于springboot版本的报错");
                    }
                    bulkRequest = new BulkRequest();  // 创建新的 BulkRequest 对象
                    count = 0;
                    log.info("100========================================");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (count > 0) {  // 处理最后剩余的文档，重新执行一次 BulkRequest 请求
            try {// 如果满足了 BulkRequest 请求的文档数量条件，则执行 BulkRequest 请求
                BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);  // 执行 BulkRequest 请求
                if (bulkResponse.hasFailures()) {  // 判断 BulkRequest 请求是否有失败的操作
                    // 处理失败情况
                    System.out.println("BulkRequest has failures: " + bulkResponse.buildFailureMessage());
                }
            }catch (IOException e){
                log.warn("es 版本高于springboot版本的报错");
            }
        }
    }


}
