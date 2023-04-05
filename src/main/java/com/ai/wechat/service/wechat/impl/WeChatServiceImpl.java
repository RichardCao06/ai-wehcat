package com.ai.wechat.service.wechat.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ai.wechat.config.WechatLoginConfig;
import com.ai.wechat.model.resp.WeChatGetMediaListResp;
import com.ai.wechat.model.resp.WeChatSendMessageResp;
import com.ai.wechat.model.resp.WeChatUploadMediaResp;
import com.ai.wechat.service.wechat.WeChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;


@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatServiceImpl implements WeChatService {

    private final WechatLoginConfig wechatLoginConfig;

    private final String SEND_MESSAGE_URL = "https://mp.weixin.qq.com/cgi-bin/singlesend?t=ajax-response&f=json";
    private final String SEND_MESSAGE_TEMPLATE = "tofakeid={}&type=1&content={}&out_trade_no=undefined&appmsg=&quickreplyid={}&token={}&lang=zh_CN&f=json&ajax=1";
    private final String REFERER_HEADER_TEMPLATE = "https://mp.weixin.qq.com/cgi-bin/message?t=message/list&count=20&day=7&token={}&lang=zh_CN";
    private final String GET_MEDIA_LIST_URL = "https://mp.weixin.qq.com/cgi-bin/filepage?action=select&token={}&lang=zh_CN&begin=0&count=2&type=2&f=json&ajax=1";

    private final String SEND_MEDIA_TEMPLATE = "type=2&tofakeid={}&quickReplyId={}&imgcode=&fileid={}&token={}&lang=zh_CN&f=json&ajax=1";

    @Override
    public boolean sendMessageToUser(String openId, String message) throws IOException {

        String tokenId = wechatLoginConfig.getTokenId();
        String quickReplyId = wechatLoginConfig.getQuickReplyId();
        String requestBody = StrUtil.format(SEND_MESSAGE_TEMPLATE, openId, message, quickReplyId, tokenId);
        String referHeader = StrUtil.format(REFERER_HEADER_TEMPLATE, tokenId);

        log.info("tokenId:{}, quickReplyId:{}", tokenId, quickReplyId);

        OkHttpClient client = new OkHttpClient.Builder()
//                .addNetworkInterceptor(new CurlInterceptor(new Loggable() {
//                    @Override
//                    public void log(String s) {
//                        System.out.println(s);
//                    }
//                }))
                .build();
        RequestBody textRequestBody = RequestBody.create(MediaType.parse("text/plain"), requestBody);

        Request request = new Request.Builder()
                .url(SEND_MESSAGE_URL)
                .post(textRequestBody)
                .addHeader("Content-Type", "text/plain")
                .addHeader("referer", referHeader)
                .addHeader("Cookie", wechatLoginConfig.getCookie())
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            WeChatSendMessageResp sendMessageResponse = JSONUtil.toBean(response.body().string(), WeChatSendMessageResp.class);
            log.info("发送消息结果: {}", sendMessageResponse);
            return true;
        }
        return false;
    }



    @Override
    public boolean uploadMedia(File file) throws IOException {
        String url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + wechatLoginConfig.getAccessToken() + "&type=image";

        //调用自定义的方法，通过HTTPS POST请求上传文件，并获取返回结果
        String result = uploadFileByHttps(url, file);

        //解析返回结果，获取media_id和url

        WeChatUploadMediaResp weChatUploadMediaResp = JSONUtil.toBean(result, WeChatUploadMediaResp.class);
        String mediaUrl = weChatUploadMediaResp.getUrl();
        log.info("upload result: {}", weChatUploadMediaResp);
        return !StringUtils.isEmpty(mediaUrl);
    }

    @Override
    public WeChatGetMediaListResp getMediaList() throws IOException {

        WeChatGetMediaListResp weChatGetMediaListResp = new WeChatGetMediaListResp();
        String tokenId = wechatLoginConfig.getTokenId();
        String requestUrl = StrUtil.format(GET_MEDIA_LIST_URL, tokenId);
        String referHeader = StrUtil.format(REFERER_HEADER_TEMPLATE, tokenId);


        OkHttpClient client = new OkHttpClient.Builder()
//                .addNetworkInterceptor(new CurlInterceptor(new Loggable() {
//                    @Override
//                    public void log(String s) {
//                        System.out.println(s);
//                    }
//                }))
                .build();

        Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .addHeader("referer", referHeader)
                .addHeader("Cookie", wechatLoginConfig.getCookie())
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            weChatGetMediaListResp = JSONUtil.toBean(response.body().string(), WeChatGetMediaListResp.class);
            log.info("发送消息结果: {}", weChatGetMediaListResp);
        }

        return weChatGetMediaListResp;
    }

    @Override
    public boolean sendMediaMessageToUser(String openId, String fileId) throws IOException {
        String tokenId = wechatLoginConfig.getTokenId();
        String quickReplyId = wechatLoginConfig.getQuickReplyId();
        String requestBody = StrUtil.format(SEND_MEDIA_TEMPLATE, openId, quickReplyId, fileId, tokenId);
        String referHeader = StrUtil.format(REFERER_HEADER_TEMPLATE, tokenId);

        OkHttpClient client = new OkHttpClient.Builder()
//                .addNetworkInterceptor(new CurlInterceptor(new Loggable() {
//                    @Override
//                    public void log(String s) {
//                        System.out.println(s);
//                    }
//                }))
                .build();
        RequestBody textRequestBody = RequestBody.create(MediaType.parse("text/plain"), requestBody);

        Request request = new Request.Builder()
                .url(SEND_MESSAGE_URL)
                .post(textRequestBody)
                .addHeader("Content-Type", "text/plain")
                .addHeader("referer", referHeader)
                .addHeader("Cookie", wechatLoginConfig.getCookie())
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            WeChatSendMessageResp sendMessageResponse = JSONUtil.toBean(response.body().string(), WeChatSendMessageResp.class);
            log.info("发送消息结果: {}", sendMessageResponse);
            return true;
        }
        return false;
    }

//    public static void main(String[] args) {
//        String text = "{\"base_resp\":{\"err_msg\":\"\",\"ret\":0},\"can_use_voice\":1,\"page_info\":{\"file_cnt\":{\"app_msg_cnt\":13,\"app_msg_sent_cnt\":14,\"appmsg_template_cnt\":0,\"commondity_msg_cnt\":0,\"draft_count\":0,\"img_cnt\":59,\"mediaapi_appmsg_cnt\":0,\"short_video_cnt\":0,\"total\":68,\"video_cnt\":0,\"video_msg_cnt\":0,\"voice_cnt\":0},\"file_group_list\":{\"biz_uin\":3255439072,\"file_group\":[{\"count\":4,\"files\":[],\"id\":4,\"name\":\"最近使用\"},{\"count\":55,\"files\":[],\"id\":0,\"name\":\"我的图片\"},{\"count\":43,\"files\":[],\"id\":1,\"name\":\"未分组\"},{\"count\":12,\"files\":[],\"id\":3,\"name\":\"文章配图\"}],\"transferred_biz_uin\":[]},\"file_item\":[{\"cdn_url\":\"https://mmbiz.qpic.cn/mmbiz_png/glzpLa8icRjKwSRIp4EsOdzcwa2zFg4tYclqO1tyhr5RnT83Hqjrorvib9pd8ZxHnPWE069SfjHl8twfZiaC6jxkA/0?wx_fmt=png\",\"file_id\":100000093,\"group_id\":1,\"img_format\":\"png\",\"name\":\"1680503551837.png\",\"seq\":7217707800946802688,\"size\":\"335.3\\tK\",\"str_seq\":\"7217707800946802688\",\"type\":2,\"update_time\":1680503553,\"video_cdn_id\":\"\",\"video_thumb_cdn_url\":\"\"},{\"cdn_url\":\"https://mmbiz.qpic.cn/mmbiz_png/glzpLa8icRjKwSRIp4EsOdzcwa2zFg4tYVsqNibicUZLjBJAF910OCTmNts7SKka0kHxVIe5x1VKeRlu03oEQnOmw/0?wx_fmt=png\",\"file_id\":100000092,\"group_id\":1,\"img_format\":\"png\",\"name\":\"1680503550590.png\",\"seq\":7217707792356868096,\"size\":\"336.5\\tK\",\"str_seq\":\"7217707792356868096\",\"type\":2,\"update_time\":1680503551,\"video_cdn_id\":\"\",\"video_thumb_cdn_url\":\"\"}],\"material_status\":0,\"type\":2,\"watermark_status\":2}}";
//        WeChatGetMediaListResp weChatGetMediaListResp = JSONUtil.toBean(text, WeChatGetMediaListResp.class);
//        System.out.println(weChatGetMediaListResp);
//    }

    private String uploadFileByHttps(String url, File file) throws IOException {

        //创建一个URL对象
        URL urlObj = new URL(url);

        //打开连接，强转为HttpsURLConnection
        HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();

        //设置连接属性
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");

        //设置边界，用于分隔表单数据
        String boundary = "----------" + System.currentTimeMillis();
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        //构建输出流，用于写入表单数据
        OutputStream out = new DataOutputStream(conn.getOutputStream());

        //第一部分：写入文件相关的信息，包括文件名、长度、类型等
        StringBuilder sb = new StringBuilder();
        sb.append("--"); //必须多两道线
        sb.append(boundary);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");

        //将字符串转为字节数组，写入输出流
        byte[] head = sb.toString().getBytes("utf-8");
        out.write(head);

        //第二部分：写入文件的内容，以字节流的形式
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] buffer = new byte[1024];
        while ((bytes = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytes);
        }

        //关闭输入流
        in.close();

        //第三部分：写入结束标志，即边界符和两道线
        byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");
        out.write(foot);

        //刷新并关闭输出流
        out.flush();
        out.close();

        //构建输入流，用于读取响应数据
        StringBuffer strBuffer = new StringBuffer();
        BufferedReader reader = null;

        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }

            //返回响应数据
            return strBuffer.toString();

        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            return null;

        } finally {
            //关闭输入流
            if (reader != null) {
                reader.close();
            }

            //断开连接
            if (conn != null) {
                conn.disconnect();
            }

        }
    }
}
