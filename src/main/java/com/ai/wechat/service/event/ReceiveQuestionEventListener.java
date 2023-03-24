package com.ai.wechat.service.event;

import com.ai.wechat.domain.Question;
import com.ai.wechat.domain.User;
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
import java.util.UUID;

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

        //1. 获取提问者信息，判断是否在用户表存在，不存在则创建用户
        String fromUser = officialAccountMessageRequest.getFromUserName();
        boolean userExist = userRepository.checkUserExist(fromUser);
        if (!userExist) {
            User user = new User();
            user.setOpenId(fromUser);
            userRepository.save(user);
        }

        //2. 获取问题内容，判断是否在问题表存在，不存在则创建问题
        String questionContent = officialAccountMessageRequest.getContent();
        String answer = Strings.EMPTY;

        Question question = questionService.createQuestion(officialAccountMessageRequest);
        try {
            answer = answerService.getAnswerByChatGPT(question);
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
}
