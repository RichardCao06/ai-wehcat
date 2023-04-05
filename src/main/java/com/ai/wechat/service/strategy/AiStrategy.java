package com.ai.wechat.service.strategy;

import com.ai.wechat.model.req.OfficialAccountMessageRequest;

public interface AiStrategy {
    void execute(OfficialAccountMessageRequest request);
}
