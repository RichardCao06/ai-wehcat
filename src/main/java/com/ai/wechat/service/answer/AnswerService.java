package com.ai.wechat.service.answer;

import com.ai.wechat.domain.Question;

import java.io.IOException;

public interface AnswerService {

    String getAnswerByChatGPT(Question question) throws IOException;

    String getAnswerInDataBase(String questionContent);
}
