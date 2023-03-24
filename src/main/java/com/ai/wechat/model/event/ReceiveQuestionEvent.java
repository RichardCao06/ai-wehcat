package com.ai.wechat.model.event;

import com.ai.wechat.model.req.OfficialAccountMessageRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;
public class ReceiveQuestionEvent extends ApplicationEvent {

    private OfficialAccountMessageRequest officialAccountMessageRequest;

    public ReceiveQuestionEvent(Object source, OfficialAccountMessageRequest officialAccountMessageRequest) {
        super(source);
        this.officialAccountMessageRequest = officialAccountMessageRequest;
    }

    public OfficialAccountMessageRequest getOfficialAccountMessageRequest() {
        return officialAccountMessageRequest;
    }

    public void setOfficialAccountMessageRequest(OfficialAccountMessageRequest officialAccountMessageRequest) {
        this.officialAccountMessageRequest = officialAccountMessageRequest;
    }
}
