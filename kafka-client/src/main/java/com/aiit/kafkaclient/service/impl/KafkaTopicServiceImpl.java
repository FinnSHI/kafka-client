package com.aiit.kafkaclient.service.impl;

import ch.qos.logback.classic.Logger;
import com.aiit.kafkaclient.entity.KafkaBrokerInfo;
import com.aiit.kafkaclient.repository.entity.CommonResult;
import com.aiit.kafkaclient.service.KafkaTopicService;
import com.aiit.kafkaclient.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*
 * @description: 主题服务层实现类
 * @author: Finn
 * @create: 2022/06/20 14:44
 */
@Slf4j
@Service
public class KafkaTopicServiceImpl implements KafkaTopicService {

    @Override
    public CommonResult<String> createOneTopic(List<KafkaBrokerInfo> brokers, String topic,
                                               Integer partition, Long batchSize, Short replicationFactor) {
        if (partition <= 0) {
            return CommonResult.fail("主题数量不能少于1");
        }


        AdminClient create = null;
        for (KafkaBrokerInfo brokerInfo : brokers) {
            String broker = brokerInfo.getBrokerHost() + ":" + brokerInfo.getBrokerPort();
            try {
                try (KafkaConsumer<String, String> consumer =
                             new KafkaConsumer<>(KafkaUtils.initTopicProperties(broker, batchSize))) {
                    Map<String, List<PartitionInfo>> topics = consumer.listTopics();
                    if (topics.containsKey(topic)) {
                        return CommonResult.fail("该主题已存在！");
                    }
                }
                create = KafkaAdminClient.create(KafkaUtils.initTopicProperties(broker, batchSize));
                create.createTopics(Arrays.asList(new NewTopic(topic, partition, replicationFactor)));
            } catch (Exception e) {
                log.error(e.getMessage());
                return CommonResult.fail(e.getMessage());
            }
        }

        return CommonResult.success("创建主题成功");
    }
}
