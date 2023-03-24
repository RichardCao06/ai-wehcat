package com.ai.wechat.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class OfficialAccountMessageRequest {

    @JsonProperty("ToUserName")
    private String toUserName;
    @JsonProperty("FromUserName")
    private String fromUserName;
    @JsonProperty("CreateTime")
    private Long createTime;
    @JsonProperty("Content")
    private String content;
}
