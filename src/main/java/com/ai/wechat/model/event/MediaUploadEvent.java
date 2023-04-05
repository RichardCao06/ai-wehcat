package com.ai.wechat.model.event;

import org.springframework.context.ApplicationEvent;

import java.io.File;

public class MediaUploadEvent extends ApplicationEvent {
    private File file;
    public MediaUploadEvent(Object source, File file) {
        super(source);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
