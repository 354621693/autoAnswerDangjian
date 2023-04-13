package com.leemanshow.app.es;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "es")
public class ElasticsearchHighLevelClientConfig {
    private String esHost;
    @Bean
    public RestHighLevelClient getHighLevelClient(){
        HttpHost host = HttpHost.create(esHost);
        RestClientBuilder builder = RestClient.builder(host);
        return new RestHighLevelClient(builder);
    }
}
