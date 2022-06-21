package com.aiit.kafkaclient.service;

import com.aiit.kafkaclient.entity.KafkaBrokerInfo;
import com.aiit.kafkaclient.repository.entity.CommonResult;

import java.util.List;

/*
 * @description: 消费者
 * @author: Finn
 * @create: 2022/06/16 11:30
 */
public interface KafkaConsumerService {

    CommonResult<List<String>> consumeMessage(List<KafkaBrokerInfo> brokers,
                                              String topic,
                                              Integer partition,
                                              Boolean autoCommitOffset,
                                              String offsetReset,
                                              Long offset,
                                              Long limit);


    CommonResult<Long> countMessage(List<KafkaBrokerInfo> brokers,
                                            String topic,
                                            Integer partition,
                                            Boolean autoCommitOffset,
                                            String offsetReset);

    CommonResult<Long> countPartition(List<KafkaBrokerInfo> brokers,
                                      String topic,
                                      Boolean autoCommitOffset,
                                      String offsetReset);

    CommonResult<List<String>> consumeTopicMessage(List<KafkaBrokerInfo> brokers,
                                                   String topic,
                                                   Boolean autoCommitOffset,
                                                   String offsetReset,
                                                   Long limit);
}
