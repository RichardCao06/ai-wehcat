package com.ai.wechat.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeChatUploadMediaResp {

    private String url;

    @JsonProperty("media_id")
    private String mediaId;
}
