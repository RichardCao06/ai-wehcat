package com.ai.wechat.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeChatBaseResp {

    private int ret;
    @JsonProperty("err_msg")
    private String errMsg;
}
