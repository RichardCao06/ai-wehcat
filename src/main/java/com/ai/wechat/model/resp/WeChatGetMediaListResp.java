package com.ai.wechat.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeChatGetMediaListResp {

    @JsonProperty("base_resp")
    private WeChatBaseResp baseResp;

    @JsonProperty("can_use_voice")
    private Integer canUseVoice;

    @JsonProperty("page_info")
    private WeChatPageInfo pageInfo;

}
