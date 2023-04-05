package com.ai.wechat.service.strategy.impl;

import cn.hutool.core.util.StrUtil;
import com.ai.wechat.model.constant.CommonConstant;
import com.ai.wechat.model.req.OfficialAccountMessageRequest;
import com.ai.wechat.service.strategy.AiStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiDrawStrategy implements AiStrategy {

    @Override
    public void execute(OfficialAccountMessageRequest request) {
        String content = request.getContent();
        StrUtil.removePrefix(content, CommonConstant.DRAW_PREFIX);


    }
}
