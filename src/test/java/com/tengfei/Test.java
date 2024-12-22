package com.tengfei;

import org.apache.batik.transcoder.TranscoderException;
import top.magicpotato.Echarts;
import top.magicpotato.EchartsUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author: tengfei
 * @CreateTime: 2024-12-22 21:42
 * @Description:
 * @Version: 1.0
 */
public class Test {
    public static void main(String[] args) throws IOException, TranscoderException {
        InputStream is = Test.class.getResourceAsStream("/option.txt");
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading from InputStream", e);
        }
        System.out.println(content);

        byte[] bytes = EchartsUtil.getImageByte(content.toString(), 1280, 720, Echarts.ImageType.JPEG);
        Files.write(Paths.get("test.jpg"), bytes);
    }
}
