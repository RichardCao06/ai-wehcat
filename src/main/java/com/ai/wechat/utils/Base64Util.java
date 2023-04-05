package com.ai.wechat.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class Base64Util {

    public static File base64ToImage(String data, String fileName) throws IOException {
        data = data.replace("\n","");
        // 分割数据类型和编码后的数据
        String[] parts = data.split(",");
        String imageString = parts[1];

        // 解码数据
        byte[] imageBytes = Base64.getDecoder().decode(imageString);

        // 转换成图片对象
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));

        // 写入到文件系统中
        File outputfile = new File(fileName);
        ImageIO.write(img, "png", outputfile);
        return outputfile;
    }
}
