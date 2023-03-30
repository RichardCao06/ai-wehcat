package com.ai.wechat.service.answer.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.ai.wechat.domain.Question;
import com.ai.wechat.model.resp.BaixingApiResponse;
import com.ai.wechat.repository.QuestionRepository;
import com.ai.wechat.service.answer.AnswerService;
import com.ai.wechat.service.question.QuestionService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final String API_KEY = "TZYGGCEZ";
    private final String BAIXING_API_URL = "https://gpt.baixing.com";

    @Override
    public String getAnswerByChatGPT(Question question) throws IOException {
        String questionContent = questionService.buildQuestionContentByHistory(question.getQuizzerId(), question.getQuestionContent(), question.getQuestionTime());
        String answerContent = getAnswerByApi(questionContent);
        if (StringUtils.isEmpty(answerContent)) {
            return null;
        }

        questionRepository.updateQusetionAnswer(question.getQuestionId(), answerContent);
        return answerContent;
    }

    @Override
    public String getAnswerInDataBase(String questionContent) {
        return null;
    }

    private String getAnswerByApi(String questionContent) throws IOException {


        HttpResponse response = HttpRequest.post(BAIXING_API_URL)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(new HashMap<String, String>() {{
                    put("p", questionContent);
                    put("k", API_KEY);
                }}))
                .execute();

        if (response.isOk()) {
            BaixingApiResponse baixingApiResponse = JSONUtil.toBean(response.body(), BaixingApiResponse.class);
            if (baixingApiResponse.getCode() == 0) {
                return baixingApiResponse.getData();
            }
        }

        return null;
    }
}
