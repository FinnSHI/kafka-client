package com.aiit.kafkaclient.controller;

import com.aiit.kafkaclient.controller.request.KafkaRequest;
import com.aiit.kafkaclient.repository.entity.CommonResult;
import com.aiit.kafkaclient.service.KafkaConsumerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/*
 * @description: 消费者控制层
 * @author: Finn
 * @create: 2022/05/27 16:28
 */
@Api(tags = {"kafka消费者"})
@RestController
@Slf4j
@RequestMapping("api/kafka/consume/topic")
public class ConsumerController {

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @ApiOperation(value = "消费一个分区里的数据")
    @PostMapping("/partition/message")
    public CommonResult<List<String>> consumeMessage(@RequestBody KafkaRequest request) {
        try {
            return  kafkaConsumerService.consumeMessage(request.getBrokers(),
                    request.getTopic(),
                    request.getPartition(),
                    false,
                    "earliest",
                    request.getOffset(),
                    request.getLimit());
        } catch (Exception e) {
            return CommonResult.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "消费一个主题里的数据")
    @PostMapping("/message")
    public CommonResult<List<String>> consumeTopicMessage(@RequestBody KafkaRequest request) {
        try {
            return  kafkaConsumerService.consumeTopicMessage(request.getBrokers(),
                    request.getTopic(),
                    true,
                    "earliest",
                    request.getLimit());
        } catch (Exception e) {
            return CommonResult.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "查看一个主题里的分区数量")
    @PostMapping("/partition/total")
    public CommonResult<Long> countPartition(@RequestBody KafkaRequest request) {
        try {
            return  kafkaConsumerService.countPartition(request.getBrokers(),
                    request.getTopic(),
                    false,
                    "earliest");
        } catch (Exception e) {
            return CommonResult.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "查看一个分区里的数据数量")
    @PostMapping("/partition/message/total")
    public CommonResult<Long> countMessage(@RequestBody KafkaRequest request) {
        try {
            return  kafkaConsumerService.countMessage(request.getBrokers(),
                    request.getTopic(),
                    request.getPartition(),
                    false,
                    "earliest");
        } catch (Exception e) {
            return CommonResult.fail(e.getMessage());
        }
    }
}
