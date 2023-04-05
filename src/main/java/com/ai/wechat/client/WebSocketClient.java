package com.ai.wechat.client;

import cn.hutool.json.JSONUtil;
import com.ai.wechat.model.event.MediaUploadEvent;
import com.ai.wechat.model.resp.WebSocketResp;
import com.ai.wechat.utils.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CopyOnWriteArraySet;

@ClientEndpoint
@Component
public class WebSocketClient {

    private Session session;

    @Autowired
    private ApplicationContext applicationContext;
    private String url = "wss://stabilityai-stable-diffusion-1.hf.space/queue/join";
    public WebSocketClient() {
        try {
            URI uri = new URI(url);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.setDefaultMaxSessionIdleTimeout(1200000);
            container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024 * 100);
            container.setDefaultMaxTextMessageBufferSize(1024 * 1024 * 100);
            container.connectToServer(this, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        // 处理接收到的消息
        try {
            WebSocketResp webSocketResp = JSONUtil.toBean(message, WebSocketResp.class);
            if (!ObjectUtils.isEmpty(webSocketResp.getOutput())) {
                WebSocketResp.Output output = webSocketResp.getOutput();
                String[][] data = output.getData();
                for (String[] datum : data) {
                    for (String s : datum) {
                        String fileName = "/Users/cao/Documents/private/ai-wehcat/" + System.currentTimeMillis() + ".png";
                        File file = Base64Util.base64ToImage(s, fileName);
                        MediaUploadEvent mediaUploadEvent = new MediaUploadEvent(this, file);
                        applicationContext.publishEvent(mediaUploadEvent);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose() throws IOException, URISyntaxException, DeploymentException {
        URI uri = new URI(url);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxSessionIdleTimeout(1200000);
        container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024 * 100);
        container.setDefaultMaxTextMessageBufferSize(1024 * 1024 * 100);
        container.connectToServer(this, uri);
        System.out.println("Connection closed");
    }

    public void sendMessage(String message) {
        // 发送消息到服务器
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
