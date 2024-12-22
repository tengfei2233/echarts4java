package top.magicpotato;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: tengfei
 * @CreateTime: 2024-12-16 19:43
 * @Description:
 * @Version: 1.0
 */
public class EchartsUtil {


    /**
     * 生成图片byte数组
     *
     * @param option echarts配置option
     * @return
     */
    public static byte[] getImageByte(String option) {
        return getImageByte(option, 1280, 720, Echarts.ImageType.PNG);
    }

    /**
     * 生成图片byte数组
     *
     * @param option echarts配置option
     * @param width  图片宽度
     * @param height 图片高度
     * @return
     */
    public static byte[] getImageByte(String option, float width, float height) {
        return getImageByte(option, width, height, Echarts.ImageType.PNG);
    }

    /**
     * 生成图片byte数组
     *
     * @param option echarts配置option
     * @param width  图片宽度
     * @param height 图片高度
     * @param type   图片类型
     * @return
     */
    public static byte[] getImageByte(String option, float width, float height, Echarts.ImageType type) {
        byte[] bytes = Echarts.render((int) width, (int) height, Echarts.ImageType.SVG.getType(), option);
        if (Echarts.ImageType.SVG.getType().equals(type.getType())) {
            return bytes;
        } else {
            Transcoder transcoder = null;
            switch (type) {
                case PNG:
                    transcoder = new PNGTranscoder();
                    transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width);
                    transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, height);
                    break;
                case JPEG:
                    transcoder = new JPEGTranscoder();
                    transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 1f);
                    transcoder.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, width);
                    transcoder.addTranscodingHint(JPEGTranscoder.KEY_HEIGHT, height);
                    break;
                default:
                    throw new RuntimeException("Not support this type");
            }
            byte[] outs;
            try (InputStream is = new ByteArrayInputStream(bytes); ByteArrayOutputStream bos = new ByteArrayOutputStream(8192)) {
                TranscoderOutput transcoderOutput = new TranscoderOutput(bos);
                TranscoderInput transcoderInput = new TranscoderInput(is);
                transcoder.transcode(transcoderInput, transcoderOutput);
                outs = bos.toByteArray();
            } catch (IOException | TranscoderException e) {
                throw new RuntimeException(e);
            }
            return outs;
        }
    }
}
