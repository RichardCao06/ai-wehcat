package com.ai.wechat.service.wechat.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ai.wechat.config.WechatLoginConfig;
import com.ai.wechat.model.resp.WeChatSendMessageResponse;
import com.ai.wechat.service.wechat.WeChatService;
import com.moczul.ok2curl.CurlInterceptor;
import com.moczul.ok2curl.logger.Loggable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatServiceImpl implements WeChatService {

    private final WechatLoginConfig wechatLoginConfig;

    private final String SEND_MESSAGE_URL = "https://mp.weixin.qq.com/cgi-bin/singlesend?t=ajax-response&f=json";
    private final String SEND_MESSAGE_TEMPLATE = "tofakeid={}&type=1&content={}&out_trade_no=undefined&appmsg=&quickreplyid={}&token={}&lang=zh_CN&f=json&ajax=1";
    private final String REFERER_HEADER_TEMPLATE = "https://mp.weixin.qq.com/cgi-bin/message?t=message/list&count=20&day=7&token={}&lang=zh_CN";

    @Override
    public boolean sendMessageToUser(String openId, String message) throws IOException {

        String tokenId = wechatLoginConfig.getTokenId();
        String quickReplyId = wechatLoginConfig.getQuickReplyId();
        String requestBody = StrUtil.format(SEND_MESSAGE_TEMPLATE, openId, message, quickReplyId, tokenId);
        String referHeader = StrUtil.format(REFERER_HEADER_TEMPLATE, tokenId);

        log.info("tokenId:{}, quickReplyId:{}", tokenId, quickReplyId);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new CurlInterceptor(new Loggable() {
                    @Override
                    public void log(String s) {
                        System.out.println(s);
                    }
                }))
                .build();
        RequestBody textRequestBody = RequestBody.create(MediaType.parse("text/plain"), requestBody);

        Request request = new Request.Builder()
                .url(SEND_MESSAGE_URL)
                .post(textRequestBody)
                .addHeader("Content-Type", "text/plain")
                .addHeader("referer", referHeader)
                .addHeader("Cookie", wechatLoginConfig.getCookie())
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            WeChatSendMessageResponse sendMessageResponse = JSONUtil.toBean(response.body().string(), WeChatSendMessageResponse.class);
            log.info("发送消息结果: {}", sendMessageResponse);
            return true;
        }
        return false;
    }
}
