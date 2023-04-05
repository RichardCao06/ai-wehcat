package com.ai.wechat.service.event;

import com.ai.wechat.domain.Question;
import com.ai.wechat.model.enums.QuestionTypeEnum;
import com.ai.wechat.model.event.MediaUploadEvent;
import com.ai.wechat.model.resp.WeChatGetMediaListResp;
import com.ai.wechat.model.resp.WeChatPageInfo;
import com.ai.wechat.repository.QuestionRepository;
import com.ai.wechat.service.question.QuestionService;
import com.ai.wechat.service.wechat.WeChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MediaUploadEventListener implements ApplicationListener<MediaUploadEvent> {

    private final WeChatService weChatService;
    private final QuestionRepository questionRepository;
    @Override
    public void onApplicationEvent(MediaUploadEvent mediaUploadEvent) {
        try {
            weChatService.uploadMedia(mediaUploadEvent.getFile());
            Optional<Question> lastUnAnsweredQuestion = questionRepository.getLastUnAnsweredQuestion(QuestionTypeEnum.DRAW);
            if (lastUnAnsweredQuestion.isPresent()) {
                Question question = lastUnAnsweredQuestion.get();
                String quizzerId = question.getQuizzerId();
                WeChatGetMediaListResp mediaList = weChatService.getMediaList();
                if (mediaList != null && mediaList.getPageInfo() != null && mediaList.getPageInfo().getFileItem().size() > 0) {
                    List<WeChatPageInfo.FileItem> fileItem = mediaList.getPageInfo().getFileItem();
                    WeChatPageInfo.FileItem lastFileItem = fileItem.get(0);
                    weChatService.sendMediaMessageToUser(quizzerId, lastFileItem.getFileId());
                    questionRepository.updateQusetionAnswer(question.getQuestionId(), lastFileItem.getFileId());
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
