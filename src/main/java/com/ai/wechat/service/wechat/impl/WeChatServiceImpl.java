package com.ai.wechat.service.wechat.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ai.wechat.model.resp.WeChatSendMessageResponse;
import com.ai.wechat.service.wechat.WeChatService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
public class WeChatServiceImpl implements WeChatService {

    private final String SEND_MESSAGE_URL = "https://mp.weixin.qq.com/cgi-bin/singlesend?t=ajax-response&f=json";
    private final String SEND_MESSAGE_TEMPLATE = "tofakeid={}&type=1&content={}&out_trade_no=undefined&appmsg=&quickreplyid=500000101&token={}&lang=zh_CN&f=json&ajax=1";
    private final String REFERER_HEADER_TEMPLATE = "https://mp.weixin.qq.com/cgi-bin/message?t=message/list&count=20&day=7&token={}&lang=zh_CN";

    private final String COOKIE = "bizuin=3255439072; data_bizuin=3255439072; data_ticket=O4YW9zGZGJb9/QvobZq2hUBMxQwpm/npSdaIuUe5bu8TUdvQxZhaG5G+4YZptfvV; rand_info=CAESIIjUtQ0DYCSjM6eDfht5lZP/hEqVSiRGsnL3zsA11SoS; slave_bizuin=3255439072; slave_sid=QVRHTXNqZVl5X1dWYlZ4bXd6aDRhSzFLalBmcmlqcE9QSU5WT0w5SF9EWWs2NUZ3SkhucTYzNk40MlpIS3NwNFlLZjh6NGJkaTlLTGpDSGdycDluVmJEWlJzd28waHZ6cGo3YlFjc0NxdkNwVV9BQmVwNjZBRWZKcTRRNDZwRWtYMml4c3JFcXN0YzBZZW54; slave_user=gh_9de8a59334a5";


    @Override
    public boolean sendMessageToUser(String openId, String message) throws IOException {

        String tokenId = "997742366";
        String requestBody = StrUtil.format(SEND_MESSAGE_TEMPLATE, openId, message, tokenId);
        String referHeader = StrUtil.format(REFERER_HEADER_TEMPLATE, tokenId);

        OkHttpClient client = new OkHttpClient.Builder()
//                .addNetworkInterceptor(new CurlInterceptor(new Loggable() {
//                    @Override
//                    public void log(String s) {
//                        System.out.println(s);
//                    }
//                }))
                .build();
        RequestBody textRequestBody = RequestBody.create(MediaType.parse("text/plain"), requestBody);

        Request request = new Request.Builder()
                .url(SEND_MESSAGE_URL)
                .post(textRequestBody)
                .addHeader("Content-Type", "text/plain")
                .addHeader("referer", referHeader)
                .addHeader("Cookie", COOKIE)
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
