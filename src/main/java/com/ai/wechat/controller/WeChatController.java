package com.ai.wechat.controller;

import com.ai.wechat.client.WebSocketClient;
import com.ai.wechat.config.WechatLoginConfig;
import com.ai.wechat.model.event.ReceiveQuestionEvent;
import com.ai.wechat.model.resp.OfficalAccountMessageResp;
import com.ai.wechat.model.req.OfficialAccountMessageRequest;
import com.ai.wechat.model.resp.WeChatGetMediaListResp;
import com.ai.wechat.model.resp.WeChatPageInfo;
import com.ai.wechat.service.wechat.WeChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeChatController {

    private final ApplicationContext applicationContext;
    private final WebSocketClient webSocketClient;

    private final WeChatService weChatService;

    private static final String DEFAULT_REPLY_MESSAGE = "您的问题已收到，我们会尽快处理，请等待一小会儿";
    private static final String DEFAULT_REPLY_MESSAGE_TYPE = "text";

    @Autowired
    WechatLoginConfig testConfig;

    @GetMapping("/")
    String hello() {
        return "欢迎使用微信云托管！";
    }

    @PostMapping("/message/reply")
    OfficalAccountMessageResp AutoReplyMessage(@RequestBody OfficialAccountMessageRequest request) {

        log.info("收到消息:{}", request);
        OfficalAccountMessageResp officalAccountMessageResp = new OfficalAccountMessageResp();
        officalAccountMessageResp.setFromUserName(request.getToUserName());
        officalAccountMessageResp.setToUserName(request.getFromUserName());
        officalAccountMessageResp.setCreateTime(System.currentTimeMillis());
        officalAccountMessageResp.setMsgType(DEFAULT_REPLY_MESSAGE_TYPE);
        officalAccountMessageResp.setContent(DEFAULT_REPLY_MESSAGE);
        ReceiveQuestionEvent receiveQuestionEvent = new ReceiveQuestionEvent(this, request);
        applicationContext.publishEvent(receiveQuestionEvent);

        return officalAccountMessageResp;

    }

}
