package com.wt.taoapiinterface.controller;

import com.wt.QrCode;
import com.wt.QrCodeToImg;
import com.wt.response.CommonResult;
import lombok.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * @author xcx
 * @date
 */
@RestController
@RequestMapping("/w/qrCode")
public class QrCodeController {
    String path = "X:\\QrCodeCache\\";

    @GetMapping("/")
    public CommonResult getQrCode(String name, String content, HttpServletResponse response) throws IOException {
        if (!StringUtils.hasText(name) || !StringUtils.hasText(content)) {
            CommonResult commonResult = new CommonResult();
            commonResult.setCode(555);
            commonResult.setData("参数不正确");
            return commonResult;
        }
        if(name.equals("wt")){
            throw new RuntimeException();
        }

        QrCode qrCode = QrCode.codeText(content);
        BufferedImage img = QrCodeToImg.toImage(qrCode, 10, 4);
        File imgFile = new File(path, name + ".png");   // File path for output
        ImageIO.write(img, "png", imgFile);

        FileInputStream fileInputStream = new FileInputStream(imgFile);
        FileChannel channel = fileInputStream.getChannel();
        ArrayList<Byte> res = new ArrayList<>();
        //写模式
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        while (channel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            int pos = byteBuffer.position();
            int lim = byteBuffer.limit();
            for (int i = pos; i < lim; i++) {
                res.add(byteBuffer.get(i));
            }
            byteBuffer.clear();
        }
        /*byte[] bytes =  new byte[res.size()];
        for(int i = 0; i < res.size(); i++){
            bytes[i] = res.get(i);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("image/png;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.close();*/

        return CommonResult.success(res, 1, "png");
    }

}