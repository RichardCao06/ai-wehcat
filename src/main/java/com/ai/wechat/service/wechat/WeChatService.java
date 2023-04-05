package com.ai.wechat.service.wechat;

import com.ai.wechat.model.resp.WeChatGetMediaListResp;

import java.io.File;
import java.io.IOException;

public interface WeChatService {

    boolean sendMessageToUser(String openId, String message) throws IOException;

    boolean uploadMedia(File file) throws IOException;
    WeChatGetMediaListResp getMediaList() throws IOException;

    boolean sendMediaMessageToUser(String openId, String fileId) throws IOException;

}
