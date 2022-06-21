package com.aiit.kafkaclient.service;


import com.aiit.kafkaclient.entity.KafkaBrokerInfo;
import com.aiit.kafkaclient.repository.entity.CommonResult;

import java.util.List;
import java.util.concurrent.ExecutionException;

/*
 * @description: kafka
 * @author: Finn
 * @create: 2022/05/27 16:09
 */
public interface KafkaProducerService {

    CommonResult<String> produceTopicMessage(List<KafkaBrokerInfo> brokers, String topic, Object message);

    CommonResult<String> produceMessage(List<KafkaBrokerInfo> brokers, String topic, Integer partition, Object message);


}
