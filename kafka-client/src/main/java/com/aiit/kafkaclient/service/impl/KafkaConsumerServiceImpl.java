package com.aiit.kafkaclient.service.impl;

import com.aiit.kafkaclient.constants.KafkaConstants;
import com.aiit.kafkaclient.entity.KafkaBrokerInfo;
import com.aiit.kafkaclient.exception.ApiException;
import com.aiit.kafkaclient.repository.entity.CommonResult;
import com.aiit.kafkaclient.service.KafkaConsumerService;
import com.aiit.kafkaclient.utils.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

/*
 * @description:
 * @author: Finn
 * @create: 2022/05/27 16:57
 */
@Service
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    // 最大重试次数
    @Value("${kafka.maxRetryTimes}")
    private int maxRetryTimes;

    // 最大超时时间
    @Value("${kafka.maxTimeOut}")
    private long maxTimeOut;

    /*
    * @Description: 指定主题、分区、offset 和 limit 来消费数据
    * @Param: [request]
    * @return: java.util.List<java.lang.String>
    * @Author: Finn
    * @Date: 2022/06/16 16:02
    */
    @Override
    public CommonResult<List<String>> consumeMessage(List<KafkaBrokerInfo> brokers,
                                                     String topic,
                                                     Integer partition,
                                                     Boolean autoCommitOffset,
                                                     String offsetReset,
                                                     Long offset,
                                                     Long limit) {
        if (Objects.isNull(partition)) {
            return CommonResult.fail("请指定分区！");
        }
        Properties props = KafkaUtils.initConsumerProperties(brokers,
                    getGroupId(topic, partition), autoCommitOffset, offsetReset);

        try (KafkaConsumer<String, String> consumer =  new KafkaConsumer<>(props)) {
            initConnection(consumer, topic, partition, offset);
            ConsumerRecords<String, String> records = consumer.poll(Duration.ZERO.plusMillis(maxTimeOut));
            List<String> ret = new ArrayList<>();
            for (ConsumerRecord<String, String> record : records) {
                if (limit == 0) {
                    break;
                }
                ret.add(record.value());
                limit--;
            }

            return CommonResult.success(ret);

        } catch (ApiException ae) {
            log.error("获取分区数量失败！原因是：" + ae.getMessage());
            return CommonResult.fail(ae.getMessage());
        } catch (IllegalStateException e) {
            log.error("找不到指定的分区！");
            return CommonResult.fail("找不到指定的分区！");
        } catch (Exception e) {
            log.error("消费消息失败！原因是：" + e.getMessage());
            return CommonResult.fail("消费消息失败！");
        }
    }

    /*
    * @Description: 统计一个分区内消息的数量
    * @Param: [brokers, topic, partition, autoCommitOffset, offsetReset]
    * @return: com.aiit.kafkaclient.repository.entity.CommonResult<java.lang.Long>
    * @Author: Finn
    * @Date: 2022/06/20 15:58
    */
    @Override
    public CommonResult<Long> countMessage(List<KafkaBrokerInfo> brokers, String topic, Integer partition,
                                           Boolean autoCommitOffset, String offsetReset) {
        if (Objects.isNull(partition)) {
            return CommonResult.fail("请指定分区！");
        }
        Properties props = KafkaUtils.initConsumerProperties(brokers,
                getGroupId(topic, partition), autoCommitOffset, offsetReset);

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            initConnection(consumer, topic, partition, 0L);
            ConsumerRecords<String, String> records = consumer.poll(Duration.ZERO.plusMillis(maxTimeOut));

            return CommonResult.success((long)records.count());
        } catch (ApiException ae) {
            log.error("获取分区数量失败！原因是：" + ae.getMessage());
            return CommonResult.fail(ae.getMessage());
        } catch (IllegalStateException e) {
            log.error("找不到指定的分区！");
            return CommonResult.fail("找不到指定的分区！");
        } catch (Exception e) {
            log.error("统计消息失败！原因是：" + e.getMessage());
            return CommonResult.fail("统计消息失败！");
        }
    }

    @Override
    public CommonResult<Long> countPartition(List<KafkaBrokerInfo> brokers,
                                             String topic, Boolean autoCommitOffset, String offsetReset) {
        Properties props = KafkaUtils.initConsumerProperties(brokers,
                getGroupId(topic, null), autoCommitOffset, offsetReset);

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));
            Set<TopicPartition> assignment = consumer.assignment();
            int maxRetryTimes = this.maxRetryTimes;
            while (assignment.size() == 0) {
                if (maxRetryTimes == 0) {
                    throw new IllegalStateException();
                }
                consumer.poll(Duration.ZERO.plusMillis(maxTimeOut));
                assignment = consumer.assignment();
                maxRetryTimes--;
            }
            return CommonResult.success((long)assignment.size());
        } catch (IllegalStateException e) {
            log.error("找不到指定的分区！");
            return CommonResult.fail("找不到指定的分区！");
        } catch (Exception e) {
            log.error("获取分区数量失败！原因是：" + e.getMessage());
            return CommonResult.fail("获取分区数量失败！");
        }
    }

    /* 
    * @Description: 不指定分区消费数据
    * @Param: [brokers, topic, autoCommitOffset, offsetReset, limit] 
    * @return: com.aiit.kafkaclient.repository.entity.CommonResult<java.util.List<java.lang.String>> 
    * @Author: Finn
    * @Date: 2022/06/20 17:50
    */
    @Override
    public CommonResult<List<String>> consumeTopicMessage(List<KafkaBrokerInfo> brokers, String topic,
                                                          Boolean autoCommitOffset, String offsetReset, Long limit) {
        Properties props = KafkaUtils.initConsumerProperties(brokers,
                getGroupId(topic, null), autoCommitOffset, offsetReset);

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            initConnection(consumer, topic, null, null);
            Set<TopicPartition> assignment = consumer.assignment();
            int maxRetryTimes = this.maxRetryTimes;
            ConsumerRecords<String, String> records = null;
            while (assignment.size() == 0) {
                if (maxRetryTimes == 0) {
                    throw new Exception();
                }
                records = consumer.poll(Duration.ZERO.plusMillis(maxTimeOut));
                assignment = consumer.assignment();
                maxRetryTimes--;
            }

            List<String> ret = new ArrayList<>();
            for (ConsumerRecord<String, String> record : records) {
                if (limit == 0) {
                    break;
                }
                ret.add(record.value());
                limit--;
            }

            return CommonResult.success(ret);
        } catch (ApiException ae) {
            log.error("获取分区数量失败！原因是：" + ae.getMessage());
            return CommonResult.fail(ae.getMessage());
        }  catch (Exception e) {
            log.error("统计消息失败！原因是：" + e.getMessage());
            return CommonResult.fail("统计消息失败！");
        }
    }


    /*
    * @Description: 根据 topic 和 partition 来获取 group id
    * @Param: []
    * @return: java.lang.String
    * @Author: Finn
    * @Date: 2022/06/17 18:03
    */
    private String getGroupId(String topic, Integer partition) {
        if (Objects.nonNull(partition)) {
            return KafkaConstants.CLIENT + "_" + topic + "_" + partition;
        } else {
            return KafkaConstants.CLIENT + "_" + topic;
        }
    }

    /*
    * @Description: 初始化 kafka 连接
    * @Param: []
    * @return: void
    * @Author: Finn
    * @Date: 2022/06/20 15:59
    */
    private void initConnection(KafkaConsumer<String, String> consumer,
                                String topic, Integer partition, Long offset) {

        consumer.subscribe(Arrays.asList(topic));
        Map<String, List<PartitionInfo>> topics = consumer.listTopics();
        if (!topics.containsKey(topic)) {
            throw new ApiException("该主题不存在！");
        }
        if (Objects.nonNull(partition) && Objects.nonNull(offset)) {
            Set<TopicPartition> assignment = consumer.assignment();
            int maxRetryTimes = this.maxRetryTimes;
            while (assignment.size() == 0) {
                if (maxRetryTimes == 0) {
                    throw new IllegalStateException();
                }
                consumer.poll(Duration.ZERO.plusMillis(maxTimeOut));
                assignment = consumer.assignment();
                maxRetryTimes--;
            }
            try {
                for (TopicPartition tp : assignment) {
                    if (tp.partition() == partition) {
                        consumer.seek(tp, offset);
                    }
                }
            } catch (IllegalStateException e) {
                throw new IllegalStateException();
            }
        }
    }


}
