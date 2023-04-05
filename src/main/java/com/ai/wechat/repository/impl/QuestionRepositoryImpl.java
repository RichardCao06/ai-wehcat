package com.ai.wechat.repository.impl;

import com.ai.wechat.domain.Question;
import com.ai.wechat.mapper.QuestionMapper;
import com.ai.wechat.model.enums.QuestionTypeEnum;
import com.ai.wechat.repository.QuestionRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class QuestionRepositoryImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionRepository {
    @Override
    public boolean checkQuestionExist(String question) {
        return this.lambdaQuery()
                .eq(Question::getQuestionContent, question)
                .eq(Question::getAnswered, Boolean.TRUE)
                .exists();
    }

    @Override
    public boolean updateQusetionAnswer(String questionId, String answerContent) {
        return this.lambdaUpdate().eq(Question::getQuestionId, questionId)
                .set(Question::getAnswerContent, answerContent)
                .set(Question::getAnswered, Boolean.TRUE)
                .set(Question::getAnswerTime, new Date())
                .update();
    }

    @Override
    public List<Question> getByQuizzerIdAndTimeZone(String fromUserName, Date currentDate, Date historyDate) {
        return this.lambdaQuery().eq(Question::getQuizzerId, fromUserName)
                .gt(Question::getQuestionTime, historyDate)
                .lt(Question::getQuestionTime, currentDate)
                .last("limit 3")
                .list();
    }

    @Override
    public Optional<Question> getLastUnAnsweredQuestion(QuestionTypeEnum questionType) {
        return this.lambdaQuery()
                .eq(Question::getQuestionType, questionType.toString())
                .eq(Question::getAnswered, Boolean.FALSE)
                .orderByDesc(Question::getQuestionTime)
                .last("limit 1")
                .oneOpt();
    }
}
