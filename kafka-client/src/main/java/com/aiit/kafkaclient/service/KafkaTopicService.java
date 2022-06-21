package com.aiit.kafkaclient.service;

import com.aiit.kafkaclient.entity.KafkaBrokerInfo;
import com.aiit.kafkaclient.repository.entity.CommonResult;

import java.util.List;

/*
 * @description: kafka 主题服务器
 * @author: Finn
 * @create: 2022/06/20 14:43
 */
public interface KafkaTopicService {

    CommonResult<String> createOneTopic(List<KafkaBrokerInfo> brokers,
                                        String topic,
                                        Integer partition,
                                        Long batchSize,
                                        Short replicationFactor);
}
