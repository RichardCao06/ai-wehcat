package com.ai.wechat.service.draw;

import com.ai.wechat.domain.Question;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DrawService {
    void drawByStableDiffusion(String request) throws IOException, URISyntaxException;
}
