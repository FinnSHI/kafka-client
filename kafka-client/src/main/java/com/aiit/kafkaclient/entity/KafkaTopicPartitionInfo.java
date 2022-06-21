package com.aiit.kafkaclient.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * @description: Topic和具体分区的信息
 * @author: Finn
 * @create: 2022/06/16 14:11
 */
@Data
@AllArgsConstructor
public class KafkaTopicPartitionInfo {
    // 主题名称
    public final String topic;
    // 分区id
    public final int partitionID;
}
