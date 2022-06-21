package com.aiit.kafkaclient.controller;

import com.aiit.kafkaclient.controller.request.KafkaRequest;
import com.aiit.kafkaclient.repository.entity.CommonResult;
import com.aiit.kafkaclient.service.KafkaProducerService;
import com.aiit.kafkaclient.service.KafkaTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @description: 主题相关控制层
 * @author: Finn
 * @create: 2022/06/20 14:43
 */
@Api(tags = {"kafka主题"})
@RestController
@Slf4j
@RequestMapping("api/kafka/topic")
public class TopicController {

    @Autowired
    private KafkaTopicService kafkaTopicService;

    @ApiOperation(value = "创建一个主题")
    @PostMapping("/create")
    public CommonResult<String> createOneTopic(@RequestBody KafkaRequest request) {
        try {
            return kafkaTopicService.createOneTopic(request.getBrokers(), request.getTopic(),
                    request.getPartition(), request.getBatchSize(), request.getReplicationFactor());
        }  catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.fail(e.getMessage());
        }
    }


}
