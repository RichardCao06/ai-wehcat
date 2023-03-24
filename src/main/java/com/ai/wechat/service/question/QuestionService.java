package com.ai.wechat.service.question;

import com.ai.wechat.domain.Question;
import com.ai.wechat.model.req.OfficialAccountMessageRequest;

public interface QuestionService {

    Question createQuestion(OfficialAccountMessageRequest request);
}
