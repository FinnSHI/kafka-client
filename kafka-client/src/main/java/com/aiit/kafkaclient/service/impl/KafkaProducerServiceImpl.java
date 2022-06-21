package com.aiit.kafkaclient.service.impl;

import com.aiit.kafkaclient.entity.KafkaBrokerInfo;
import com.aiit.kafkaclient.repository.entity.CommonResult;
import com.aiit.kafkaclient.service.KafkaProducerService;
import com.aiit.kafkaclient.utils.KafkaUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.config.ConfigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/*
 * @description: kafka接口实现类
 * @author: Finn
 * @create: 2022/05/27 16:10
 */
@Slf4j
@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // 最大重试次数
    @Value("${kafka.maxRetryTimes}")
    private int maxRetryTimes;

    // 最大超时时间
    @Value("${kafka.maxTimeOut}")
    private long maxTimeOut;

    @Override
    public CommonResult<String> produceTopicMessage(List<KafkaBrokerInfo> brokers, String topic, Object message) {
        KafkaProducer<String, String> producer = null;
        Future<RecordMetadata> send = null;
        for (KafkaBrokerInfo brokerInfo : brokers) {
            String broker = brokerInfo.getBrokerHost() + ":" + brokerInfo.getBrokerPort();
            try {
                producer = new KafkaProducer<>(KafkaUtils.initProducerProperties(broker));
                if (message instanceof String) {
                    JSON.parse((String) message);
                }
                String strMessage = JSON.toJSONString(message);
                send = producer.send(new ProducerRecord<>(topic, null, strMessage));
                send.get();
            } catch (JSONException | InterruptedException | ExecutionException je) {
                log.error(je.getMessage());
                send = producer.send(new ProducerRecord<>(topic, null, (String) message));
                try {
                    send.get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error(e.getMessage());
                    return CommonResult.fail("生产消息失败！");
                }

                return CommonResult.success("生产消息成功！但消息不是json格式。");
            } catch (ConfigException ce) {
                log.error(ce.getMessage());
                return CommonResult.fail("配置信息错误！");
            } catch (Exception e) {
                log.error(e.getMessage());
                return CommonResult.fail("生产消息失败！");
            }
        }

        return CommonResult.success("生产消息成功！");
    }

    @Override
    public CommonResult<String> produceMessage(List<KafkaBrokerInfo> brokers, String topic, Integer partition, Object message) {
        KafkaProducer<String, String> producer = null;
        Future<RecordMetadata> send = null;
        for (KafkaBrokerInfo brokerInfo : brokers) {
            String broker = brokerInfo.getBrokerHost() + ":" + brokerInfo.getBrokerPort();
            try {
                producer = new KafkaProducer<>(KafkaUtils.initProducerProperties(broker));
                if (message instanceof String) {
                    JSON.parse((String) message);
                }
                String strMessage = JSON.toJSONString(message);
                send = producer.send(new ProducerRecord<>(topic, partition, null, strMessage));
                send.get();
            } catch (JSONException | InterruptedException | ExecutionException je) {
                log.error(je.getMessage());
                send = producer.send(new ProducerRecord<>(topic, partition, null, (String) message));
                try {
                    send.get();
                } catch (InterruptedException | ExecutionException e ) {
                    log.error(e.getMessage());
                    return CommonResult.fail("生产消息失败！");
                }

                return CommonResult.success("生产消息成功！但消息不是json格式。");
            } catch (ConfigException ce) {
                log.error(ce.getMessage());
                return CommonResult.fail("配置信息错误！");
            } catch (Exception e) {
                log.error(e.getMessage());
                return CommonResult.fail("生产消息失败！");
            }
        }

        return CommonResult.success("生产消息成功！");
    }
}
