package com.ai.wechat.repository;

import com.ai.wechat.domain.Question;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

public interface QuestionRepository extends IService<Question> {
    boolean checkQuestionExist(String question);

    boolean updateQusetionAnswer(String questionId, String answerContent);
    List<Question> getByQuizzerIdAndTimeZone(String fromUserName, Date currentDate, Date historyDate);
}
