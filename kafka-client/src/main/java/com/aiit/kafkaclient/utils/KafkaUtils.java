package com.aiit.kafkaclient.utils;

import com.aiit.kafkaclient.constants.KafkaConstants;
import com.aiit.kafkaclient.entity.KafkaBrokerInfo;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.List;
import java.util.Properties;

/*
 * @description: 常用工具类
 * @author: Finn
 * @create: 2022/06/16 18:02
 */
public class KafkaUtils {

    /*
     * @Description: 休眠
     * @Param: []
     * @return: void
     * @Author: Finn
     * @Date: 2022/06/16 16:52
     */
    public static void sleep(Long retryIntervalMillis) throws InterruptedException {
        Thread.sleep(retryIntervalMillis);
    }

    /*
    * @Description: 获得 kafka producer 配置
    * @Param: [broker]
    * @return: java.util.Properties
    * @Author: Finn
    * @Date: 2022/06/17 13:57
    */
    public static Properties initProducerProperties(String broker) throws ConfigException {
        Properties props = new Properties();
        //设置Kafka服务器地址
        props.put(KafkaConstants.BOOTSTRAP_SERVERS, broker);
        //设置数据key的序列化处理类
        props.put(KafkaConstants.KEY_SERIALIZER, StringSerializer.class.getName());
        //设置数据value的序列化处理类
        props.put(KafkaConstants.VALUE_SERIALIZER, StringSerializer.class.getName());
        return props;
    }

    /*
    * @Description: 获得 kafka consumer 配置
    * @Param: [broker]
    * @return: java.util.Properties
    * @Author: Finn
    * @Date: 2022/06/17 17:43
    */
    public static Properties initConsumerProperties(List<KafkaBrokerInfo> brokers, String groupId,
                                                    Boolean autoCommitOffset, String offsetReset) throws ConfigException {
        Properties props = new Properties();
        props.put(KafkaConstants.BOOTSTRAP_SERVERS, getBootstrapServers(brokers));
        props.put(KafkaConstants.GROUP_ID, groupId);
        // 是否自动提交位移
        props.put(KafkaConstants.ENABLE_AUTO_COMMIT, autoCommitOffset);
        props.put(KafkaConstants.KEY_SERIALIZER, StringSerializer.class.getName());
        props.put(KafkaConstants.VALUE_SERIALIZER, StringSerializer.class.getName());
        props.put(KafkaConstants.KEY_DESERIALIZER, StringDeserializer.class.getName());
        props.put(KafkaConstants.VALUE_DESERIALIZER, StringDeserializer.class.getName());
        // kafka的offset策略: earliest, latest, none
        props.put(KafkaConstants.AUTO_OFFSET_RESET, offsetReset);
        return props;
    }

    /*
    * @Description: 获得 kafka topic 相关配置
    * @Param: [brokers, groupId, autoCommitOffset, offsetReset]
    * @return: java.util.Properties
    * @Author: Finn
    * @Date: 2022/06/20 14:49
    */
    public static Properties initTopicProperties(String broker, Long batchSize) throws ConfigException {
        Properties props = new Properties();
        props.put(KafkaConstants.BOOTSTRAP_SERVERS, broker);
        props.put(KafkaConstants.BATCH_SIZE, batchSize);
        props.put(KafkaConstants.KEY_SERIALIZER, StringSerializer.class.getName());
        props.put(KafkaConstants.VALUE_SERIALIZER, StringSerializer.class.getName());
        props.put(KafkaConstants.KEY_DESERIALIZER, StringDeserializer.class.getName());
        props.put(KafkaConstants.VALUE_DESERIALIZER, StringDeserializer.class.getName());
        return props;
    }

    private static String getBootstrapServers(List<KafkaBrokerInfo> brokers) {
        StringBuilder sb = new StringBuilder();
        for (KafkaBrokerInfo broker : brokers) {
            sb.append(broker.getBrokerHost()).append(":").append(broker.getBrokerPort()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
