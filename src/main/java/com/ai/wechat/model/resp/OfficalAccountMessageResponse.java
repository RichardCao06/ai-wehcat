package com.ai.wechat.model.resp;

import lombok.Data;

@Data
public class OfficalAccountMessageResponse {
    private String ToUserName;
    private String FromUserName;
    private Long CreateTime;
    private String Content;
    private String MsgType;
}
