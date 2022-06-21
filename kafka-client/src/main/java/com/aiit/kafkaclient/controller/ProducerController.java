package com.aiit.kafkaclient.controller;

import com.aiit.kafkaclient.controller.request.KafkaRequest;
import com.aiit.kafkaclient.repository.entity.CommonResult;
import com.aiit.kafkaclient.service.KafkaProducerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @description: 生产者控制层
 * @author: Finn
 * @create: 2022/05/27 16:03
 */
@Api(tags = {"kafka生产者"})
@RestController
@Slf4j
@RequestMapping("api/kafka/produce/topic/")
public class ProducerController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @ApiOperation(value = "不指定分区生产一条消息")
    @PostMapping("/message")
    public CommonResult<String> produceOneMessage(@RequestBody KafkaRequest request) {
        try {
            return kafkaProducerService.produceTopicMessage(request.getBrokers(), request.getTopic(), request.getMessage());
        }  catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.fail("生产消息失败！");
        }
    }

    @ApiOperation(value = "在指定分区中生产一条消息")
    @PostMapping("/partition/message")
    public CommonResult<String> produceMessage(@RequestBody KafkaRequest request) {
        try {
            return kafkaProducerService.produceMessage(request.getBrokers(), request.getTopic(),
                    request.getPartition(), request.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CommonResult.fail(e.getMessage());
        }
    }
}
