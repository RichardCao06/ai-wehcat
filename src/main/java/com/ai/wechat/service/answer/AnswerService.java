package com.ai.wechat.service.answer;

import com.ai.wechat.domain.Question;

import java.io.IOException;
import java.net.URISyntaxException;

public interface AnswerService {

    String getAnswerByChatGPT(Question question) throws IOException;

    String getAnswerByStableDiffusion(Question question) throws IOException, URISyntaxException;
}
