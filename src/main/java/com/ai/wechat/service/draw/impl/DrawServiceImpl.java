package com.ai.wechat.service.draw.impl;

import cn.hutool.core.util.StrUtil;
import com.ai.wechat.client.WebSocketClient;
import com.ai.wechat.domain.Question;
import com.ai.wechat.model.constant.CommonConstant;
import com.ai.wechat.service.draw.DrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
@RequiredArgsConstructor
public class DrawServiceImpl implements DrawService {
    private final WebSocketClient webSocketClient;

    private String STABLE_DIFFUSION_URL = "wss://stabilityai-stable-diffusion-1.hf.space/queue/join";
    private String SESSION_MESSAGE = "{\"session_hash\":\"uuwyx0aakg\",\"fn_index\":2}";
    private String REQUEST_MESSAGE = "\"fn_index\":2,\"data\":[\"{}\"],\"session_hash\":\"uuwyx0aakg\"";

    @Override
    public void drawByStableDiffusion(String request) throws IOException, URISyntaxException {

        String drawRequest = StrUtil.removePrefix(request, CommonConstant.DRAW_PREFIX);
        String requestMessage ="{" + StrUtil.format(REQUEST_MESSAGE, drawRequest) + "}";

        webSocketClient.sendMessage(SESSION_MESSAGE);
        webSocketClient.sendMessage(requestMessage);

    }
}
