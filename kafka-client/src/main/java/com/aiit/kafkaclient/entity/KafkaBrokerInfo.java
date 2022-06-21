package com.aiit.kafkaclient.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * @description: Kafka服务器连接参数
 * @author: Finn
 * @create: 2022/06/16 14:00
 */
@Data
@AllArgsConstructor
public class KafkaBrokerInfo {
    // 主机名
    public final String brokerHost;
    // 端口号
    public final int brokerPort;

    public KafkaBrokerInfo(String brokerHost) {
        // 使用默认参数进行连接
        this(brokerHost, 9092);
    }
}
