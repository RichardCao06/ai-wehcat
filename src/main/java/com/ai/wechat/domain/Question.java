package com.ai.wechat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Date;

@TableName("question")
@Data
public class Question {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String questionId;
    private String quizzerId;

    private String questionContent;

    private String answerContent;

    private Boolean answered;

    private Date questionTime;

    private Date answerTime;

}
