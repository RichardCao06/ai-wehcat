package com.ai.wechat.service.question;

import com.ai.wechat.domain.Question;
import com.ai.wechat.model.req.OfficialAccountMessageRequest;

import java.util.Date;

public interface QuestionService {

    Question createQuestion(OfficialAccountMessageRequest request);

    String buildQuestionContentByHistory(String quizzer, String questionContent, Date questionDate);
}
