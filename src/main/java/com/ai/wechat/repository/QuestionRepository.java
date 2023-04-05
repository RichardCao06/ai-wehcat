package com.ai.wechat.repository;

import com.ai.wechat.domain.Question;
import com.ai.wechat.model.enums.QuestionTypeEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends IService<Question> {
    boolean checkQuestionExist(String question);

    boolean updateQusetionAnswer(String questionId, String answerContent);
    List<Question> getByQuizzerIdAndTimeZone(String fromUserName, Date currentDate, Date historyDate);

    Optional<Question> getLastUnAnsweredQuestion(QuestionTypeEnum questionType);

}
