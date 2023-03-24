package com.ai.wechat.service.question.impl;

import com.ai.wechat.domain.Question;
import com.ai.wechat.model.req.OfficialAccountMessageRequest;
import com.ai.wechat.repository.QuestionRepository;
import com.ai.wechat.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;

    @Override
    public Question createQuestion(OfficialAccountMessageRequest request) {
        Question question = new Question();
        question.setQuestionId(UUID.randomUUID().toString());
        question.setQuestionContent(request.getContent());
        question.setQuestionTime(new Date());
        question.setQuizzerId(request.getFromUserName());
        questionRepository.save(question);
        return question;
    }
}
