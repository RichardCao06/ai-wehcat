package com.ai.wechat.service.wechat;

import java.io.IOException;

public interface WeChatService {

    boolean sendMessageToUser(String openId, String message) throws IOException;
}
