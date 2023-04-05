package com.ai.wechat.service.question;

import com.ai.wechat.domain.Question;
import com.ai.wechat.model.enums.QuestionTypeEnum;
import com.ai.wechat.model.req.OfficialAccountMessageRequest;

import java.util.Date;

public interface QuestionService {

    Question createQuestion(OfficialAccountMessageRequest request, QuestionTypeEnum questionType);

    String buildQuestionContentByHistory(String quizzer, String questionContent, Date questionDate);

}
