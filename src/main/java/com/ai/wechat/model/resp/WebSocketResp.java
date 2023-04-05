package com.ai.wechat.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WebSocketResp {

    private String msg;
    private Output output;
    private Boolean success;

    @Data
    public
    class Output {

        @JsonProperty("average_duration")
        private Double averageDuration;

        private String[][] data;
        private Double duration;
    }
}
