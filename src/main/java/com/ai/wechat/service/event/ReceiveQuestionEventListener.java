package com.ai.wechat.service.event;

import cn.hutool.core.util.StrUtil;
import com.ai.wechat.domain.Question;
import com.ai.wechat.model.constant.CommonConstant;
import com.ai.wechat.model.enums.QuestionTypeEnum;
import com.ai.wechat.model.event.ReceiveQuestionEvent;
import com.ai.wechat.model.req.OfficialAccountMessageRequest;
import com.ai.wechat.repository.QuestionRepository;
import com.ai.wechat.repository.UserRepository;
import com.ai.wechat.service.answer.AnswerService;
import com.ai.wechat.service.question.QuestionService;
import com.ai.wechat.service.wechat.WeChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@Async
@Component
@RequiredArgsConstructor
public class ReceiveQuestionEventListener implements ApplicationListener<ReceiveQuestionEvent> {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final WeChatService weChatService;
    @Override
    public void onApplicationEvent(ReceiveQuestionEvent receiveQuestionEvent) {

        OfficialAccountMessageRequest officialAccountMessageRequest = receiveQuestionEvent.getOfficialAccountMessageRequest();

        //1. 获取提问者信息
        String fromUser = officialAccountMessageRequest.getFromUserName();

        String questionContent = officialAccountMessageRequest.getContent();
        QuestionTypeEnum questionTypeEnum = getStrategyByQuestion(questionContent);
        String answer = Strings.EMPTY;

        Question question = questionService.createQuestion(officialAccountMessageRequest, questionTypeEnum);
        try {
            switch (questionTypeEnum) {
                case DRAW:
                    answerService.getAnswerByStableDiffusion(question);
                    break;
                case CHAT:
                    answer = answerService.getAnswerByChatGPT(question);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("获取答案失败:{}", question, e);
        }

        //3. 将答案发送给提问者
        if (!StringUtils.isEmpty(answer)) {
            try {
                weChatService.sendMessageToUser(fromUser, answer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private QuestionTypeEnum getStrategyByQuestion(String questionContent) {
        if (StrUtil.contains(questionContent, CommonConstant.DRAW_PREFIX)) {
            return QuestionTypeEnum.DRAW;
        }
        else {
            return QuestionTypeEnum.CHAT;
        }
    }
}
