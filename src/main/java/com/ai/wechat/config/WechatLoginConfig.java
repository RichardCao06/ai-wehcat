package com.ai.wechat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatLoginConfig {

    private String cookie;
    private String quickReplyId;
    private String tokenId;
    private String accessToken;

}
