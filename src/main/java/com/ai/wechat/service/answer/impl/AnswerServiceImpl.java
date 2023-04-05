package com.ai.wechat.service.answer.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.ai.wechat.client.WebSocketClient;
import com.ai.wechat.domain.Question;
import com.ai.wechat.model.constant.CommonConstant;
import com.ai.wechat.model.resp.BaixingApiResp;
import com.ai.wechat.repository.QuestionRepository;
import com.ai.wechat.service.answer.AnswerService;
import com.ai.wechat.service.draw.DrawService;
import com.ai.wechat.service.question.QuestionService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final String API_KEY = "TZYGGCEZ";
    private final String BAIXING_API_URL = "https://gpt.baixing.com";

    private final DrawService drawService;
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
    public String getAnswerByStableDiffusion(Question question) throws IOException, URISyntaxException {
        String questionContent = question.getQuestionContent();
        String drawRequest = StrUtil.removePrefix(questionContent, CommonConstant.DRAW_PREFIX);
        drawService.drawByStableDiffusion(drawRequest);
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
            BaixingApiResp baixingApiResp = JSONUtil.toBean(response.body(), BaixingApiResp.class);
            if (baixingApiResp.getCode() == 0) {
                return baixingApiResp.getData();
            }
        }

        return null;
    }
}
