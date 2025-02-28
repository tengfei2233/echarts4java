package com.tengfei2233;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @Author: tengfei
 * @CreateTime: 2024-12-16 19:36
 * @Description:
 * @Version: 1.0
 */

public class Echarts {
    static {
        String fileName;
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            // Windows操作系统
            fileName = "win.dll";
        } else if (os.startsWith("Linux")) {
            // Linux操作系统
            fileName = "linux.so";
        } else {
            // 其他操作系统
            throw new RuntimeException("Unsupported Operating System");
        }
        Path path = Paths.get(fileName);
        try (InputStream resourceAsStream = Echarts.class.getResourceAsStream("/lib/" + fileName); OutputStream outputStream = Files.newOutputStream(path)) {
            Objects.requireNonNull(resourceAsStream, "Unable to load this dynamic link library");
            copyStream(resourceAsStream, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = path.toFile();
        System.out.println(file.getAbsolutePath());
        System.load(file.getAbsolutePath());
    }

    private static void copyStream(InputStream in, OutputStream out) throws IOException {
        // 定义缓冲区大小
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            // 将读取的字节写入 OutputStream
            out.write(buffer, 0, bytesRead);
        }
    }

    /**
     * 渲染图表到指定路径
     *
     * @param width  图表宽度 px
     * @param height 图表高度 px
     * @param path   文件生成路径，根据文件扩展名生成对应格式
     * @param option 图表渲染使用的json数据，参考echarts官网
     */
    public static native void save(int width, int height, String path, String option);

    /**
     * 渲染图表到指定路径
     *
     * @param width  图表宽度 px
     * @param height 图表高度 px
     * @param type   图片格式
     * @param option 图表渲染使用的json数据，参考echarts官网
     * @return
     */
    public static native byte[] render(int width, int height, String type, String option);

    public enum ImageType {
        SVG("svg"),
        PNG("png"),
        JPEG("jpeg");

        private String type;

        ImageType(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }

    }
}
