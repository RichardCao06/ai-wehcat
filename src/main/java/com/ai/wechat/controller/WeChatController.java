package com.ai.wechat.controller;

import com.ai.wechat.model.event.ReceiveQuestionEvent;
import com.ai.wechat.model.resp.OfficalAccountMessageResponse;
import com.ai.wechat.model.req.OfficialAccountMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeChatController {

    private final ApplicationContext applicationContext;

    private static final String DEFAULT_REPLY_MESSAGE = "您的问题已收到，我们会尽快处理，请等待一小会儿";
    private static final String DEFAULT_REPLY_MESSAGE_TYPE = "text";

    @GetMapping("/")
    String hello() {
        return "欢迎使用微信云托管！";
    }

    @PostMapping("/message/reply")
    OfficalAccountMessageResponse AutoReplyMessage(@RequestBody OfficialAccountMessageRequest request) {

        log.info("收到消息:{}", request);
        OfficalAccountMessageResponse officalAccountMessageResponse = new OfficalAccountMessageResponse();
        officalAccountMessageResponse.setFromUserName(request.getToUserName());
        officalAccountMessageResponse.setToUserName(request.getFromUserName());
        officalAccountMessageResponse.setCreateTime(System.currentTimeMillis());
        officalAccountMessageResponse.setMsgType(DEFAULT_REPLY_MESSAGE_TYPE);
        officalAccountMessageResponse.setContent(DEFAULT_REPLY_MESSAGE);
        ReceiveQuestionEvent receiveQuestionEvent = new ReceiveQuestionEvent(this, request);
        applicationContext.publishEvent(receiveQuestionEvent);

        return officalAccountMessageResponse;

    }




}
