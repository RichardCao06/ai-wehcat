package com.ai.wechat.service.question.impl;

import com.ai.wechat.domain.Question;
import com.ai.wechat.model.req.OfficialAccountMessageRequest;
import com.ai.wechat.repository.QuestionRepository;
import com.ai.wechat.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    public String buildQuestionContentByHistory(String quizzer, String questionContent, Date questionDate) {
        Date historyDate = new Date(questionDate.getTime() - TimeUnit.MINUTES.toMillis(30));
        List<Question> historyQuestions = questionRepository.getByQuizzerIdAndTimeZone(quizzer, questionDate, historyDate);
        StringBuilder stringBuilder = new StringBuilder();
        if (!CollectionUtils.isEmpty(historyQuestions)) {
            historyQuestions.forEach(
                    question -> {
                        stringBuilder.append(question.getQuestionContent());
                        stringBuilder.append("，");
                    }
            );
        }
        stringBuilder.append(questionContent);
        questionContent = stringBuilder.toString();
        return questionContent;
    }
}
