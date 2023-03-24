package com.ai.wechat.model.resp;

import lombok.Data;

@Data
public class BaixingApiResponse {
    private Integer code;
    private String message;
    private String data;
}
