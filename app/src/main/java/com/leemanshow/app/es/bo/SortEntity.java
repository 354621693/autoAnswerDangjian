package com.leemanshow.app.es.bo;


import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;

/**
 * 排序体
 */
@Data
public class SortEntity {
    private String fieldName;
    private SortOrder sortOrder;
}
