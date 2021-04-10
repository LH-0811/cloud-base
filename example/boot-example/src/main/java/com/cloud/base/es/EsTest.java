package com.cloud.base.es;

import com.alibaba.fastjson.JSON;
import com.cloud.base.es.entity.EsBook;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

/**
 * @author lh0811
 * @date 2021/4/10
 */
@Slf4j
public class EsTest {

    public static void main(String[] args) throws IOException {

        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );
        log.info("连接es服务完成");

        // 1. 创建索引
//        CreateIndexRequest book = new CreateIndexRequest("book");
//        CreateIndexResponse createIndexResponse = esClient.indices().create(book, RequestOptions.DEFAULT);
//        log.info("创建索引结果:{}", JSON.toJSONString(createIndexResponse));

        // 2. 查询索引
//        GetIndexRequest getIndexRequest = new GetIndexRequest("book");
//        GetIndexResponse getIndexResponse = esClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
//        log.info("查询索引结果:{}", getIndexResponse.getAliases());

        // 3. 删除索引
//        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("book");
//        AcknowledgedResponse delete = esClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//        log.info("删除索引结果:{}",JSON.toJSONString(delete));

        // 4. 向索引中添加数据
//        IndexRequest indexRequest = new IndexRequest();
//        indexRequest.index("book").id("1");
//        // 构建对象
//        EsBook esBook = new EsBook();
//        esBook.setName("Es学习手册");
//        esBook.setPages("100");
//        esBook.setAuther("lh0811");
//        // 将对象转json后存放到request
//        indexRequest.source(JSON.toJSONString(esBook), XContentType.JSON);
//
//        // 存放数据到index
//        IndexResponse response = esClient.index(indexRequest, RequestOptions.DEFAULT);
//        log.info("存放数据到index:{}",JSON.toJSONString(response));

        // 5. 修改数据
//        UpdateRequest updateRequest = new UpdateRequest();
//        updateRequest.index("book").id("1");
//        EsBook esBook = new EsBook();
//        esBook.setName("Es学习手册2");
//        esBook.setPages("150");
//        updateRequest.doc(JSON.toJSONString(esBook),XContentType.JSON);
//        UpdateResponse updateResponse = esClient.update(updateRequest, RequestOptions.DEFAULT);
//        log.info("修改数据:{}", JSON.toJSONString(updateResponse));
        // 6. 查询数据
//        GetRequest getRequest = new GetRequest();
//        getRequest.index("book").id("1");
//        GetResponse response = esClient.get(getRequest, RequestOptions.DEFAULT);
//        log.info("查询数据:{}",JSON.toJSONString(response));
//        log.info("查询数据:{}",response.getSourceAsString());
        // 7. 删除数据
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.index("book").id("1");
        DeleteResponse delete = esClient.delete(deleteRequest, RequestOptions.DEFAULT);


        esClient.close();
        log.info("成功关闭esClient");
    }
}
