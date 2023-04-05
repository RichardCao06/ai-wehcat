package com.ai.wechat.service.strategy.impl;

import com.ai.wechat.domain.Question;
import com.ai.wechat.model.req.OfficialAccountMessageRequest;
import com.ai.wechat.service.answer.AnswerService;
import com.ai.wechat.service.question.QuestionService;
import com.ai.wechat.service.strategy.AiStrategy;
import com.ai.wechat.service.wechat.WeChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatStrategy implements AiStrategy {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final WeChatService weChatService;

    @Override
    public void execute(OfficialAccountMessageRequest request) {
//        String answer = Strings.EMPTY;
//
//        Question question = questionService.createQuestion(request);
//        try {
//            answer = answerService.getAnswerByChatGPT(question);
//        } catch (Exception e) {
//            log.error("获取答案失败:{}", question, e);
//        }
//
//        //3. 将答案发送给提问者
//        if (!StringUtils.isEmpty(answer)) {
//            try {
//                weChatService.sendMessageToUser(request.getFromUserName(), answer);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }
}
