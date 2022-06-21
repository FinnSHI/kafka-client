package com.aiit.kafkaclient.controller.request;

import com.aiit.kafkaclient.entity.KafkaBrokerInfo;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/*
 * @description: kafka请求
 * @author: Finn
 * @create: 2022/05/27 16:12
 */
@Data
public class KafkaRequest {

    /*
    * 集群
    * */
    @NotNull
    private List<KafkaBrokerInfo> brokers;

    /*
    * clientId
    * */
    private String clientId;

    /*
    * 主题
    * */
    @NotNull
    private String topic;

    /*
    * 分区
    * */
    private Integer partition;

    /*
    * 消息
    * */
    private Object message;

    /*
    * 偏移量
    * */
    private Long offset = 0L;

    /*
    * 消息最大读取数量
    * */
    private Long limit = -1L;

    /* 
    * batch size
    */
    private Long batchSize = 16384L;

    /*
     * 分区副本
     */
    private Short replicationFactor= 1;
}
