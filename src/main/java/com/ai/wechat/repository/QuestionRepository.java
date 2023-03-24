package com.ai.wechat.repository;

import com.ai.wechat.domain.Question;
import com.baomidou.mybatisplus.extension.service.IService;

public interface QuestionRepository extends IService<Question> {
    boolean checkQuestionExist(String question);

    boolean updateQusetionAnswer(String questionId, String answerContent);
}
