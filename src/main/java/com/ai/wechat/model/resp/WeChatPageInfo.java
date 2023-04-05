package com.ai.wechat.model.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WeChatPageInfo {

    @JsonProperty("file_item")
    List<FileItem> fileItem;


    @Data
    public
    class FileItem {

        @JsonProperty("cdn_url")
        private String cdnUrl;

        @JsonProperty("file_id")
        private String fileId;

        @JsonProperty("img_format")
        private String imgFormat;

        private String name;

        private String seq;

        private String size;

        @JsonProperty("str_seq")
        private String strSeq;

        private String type;

        @JsonProperty("update_time")
        private Date updateTime;

        @JsonProperty("video_cdn_id")
        private String videoCdnId;

        @JsonProperty("video_cdn_url")
        private String videoThumbCdnUrl;

    }
}
