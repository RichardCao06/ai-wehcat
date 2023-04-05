package com.ai.wechat.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeChatSendMessageResp {

    @JsonProperty("base_resp")
    private BaseResp baseResp;

    @JsonProperty("is_miss_reply")
    private Integer isMissReply;

    @JsonProperty("stage")
    private Integer stage;

    @Data
    class BaseResp {
        private int ret;
        @JsonProperty("err_msg")
        private String errMsg;
    }
}
