package com.ai.wechat.service.wechat;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
