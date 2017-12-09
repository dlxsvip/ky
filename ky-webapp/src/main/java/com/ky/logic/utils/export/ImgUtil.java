package com.ky.logic.utils.export;

import com.ky.logic.utils.FileUtil;
import com.ky.logic.utils.ReadUtil;
import com.ky.logic.utils.WriteUtil;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by yl on 2017/8/30.
 */
public enum ImgUtil {

    INSTANCE;

    public static final String IMG_DATA_TYPE_REG = "data:image/.*;base64,";

    public static final String ENCODE = "UTF-8";

    /**
     * 将base64编码字符串转换为图片
     *
     * @param b64     图片的 b64 字符串
     * @param imgFile 生成的图片路径
     * @return
     */
    public boolean b642img(String b64, String imgFile) {
        if (StringUtils.isEmpty(b64)) {
            return false;
        }

        if (b64.contains("data:image/")) {
            b64 = b64.replaceFirst(IMG_DATA_TYPE_REG, "");
        }


        try (OutputStream out = new FileOutputStream(imgFile)) {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(b64);

            for (int i = 0; i < b.length; i++) {

                // 调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            out.write(b);
            out.flush();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据图片地址转换为base64编码字符串
     * Base64 编码后的文件体积一般比源文件大 30% 左右
     *
     * @param imgFile 图片地址
     * @return base64编码字符串
     */
    public String img2b64(String imgFile) {

        try (InputStream in = new FileInputStream(imgFile)) {
            byte[] data = new byte[in.available()];

            in.read(data);

            BASE64Encoder encoder = new BASE64Encoder();


            return encoder.encode(data);
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }

    }


    /**
     * 返回浏览器识别的b64
     *
     * @param imgFile 图片地址
     * @return base64编码字符串
     */
    public String img2b64AddType(String imgFile) {

        // 图片b64
        String b64 = img2b64(imgFile);

        // 文件后缀名
        String pfix = FileUtil.pfix(imgFile);

        // 图片前缀
        String dataType = IMG_DATA_TYPE_REG.replace(".*", pfix);

        return dataType + b64;
    }


    /**
     * 下载网络图片到本地
     *
     * @param strUrl    网络图片
     * @param localPath 保存到本地地址
     */
    public boolean download4http(String strUrl, String localPath) {
        return WriteUtil.url2local(strUrl, localPath);
    }

    /**
     * 将b64编码的图片用post方式发送到url中
     *
     * @param url    url地址
     * @param b64Img b64编码的图片
     */
    public void sendImgByPost(String url, String b64Img) {
        try {
            HttpURLConnection huc = (HttpURLConnection) new URL(url).openConnection();
            huc.setDoInput(true);
            huc.setDoOutput(true);
            huc.setRequestMethod("POST");

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(huc.getOutputStream()));
            pw.print(b64Img);
            pw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
            String content = "";
            String line = br.readLine();
            while (line != null) {
                content = content + line;
                line = br.readLine();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 将接受过来的信息生成文件
     *
     * @param request
     * @param imgFile
     */
    public boolean createImg(HttpServletRequest request, String imgFile) {
        try {
            String content = ReadUtil.receiveContent(request);

            // b64 解码
            BASE64Decoder base = new BASE64Decoder();
            byte[] imgBytes = base.decodeBuffer(content);

            // 写入本地
            return WriteUtil.byte2local(imgBytes, imgFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 对图片进行缩放
     *
     * @param srcPath 原始图片路径(绝对路径)
     * @param newPath 缩放后图片路径（绝对路径）
     * @param times   大于1放大倍数、小于1缩小倍数
     * @param hints   缩放模式
     * @return 是否成功
     */
    public boolean zoomImage(String srcPath, String newPath, double times, Integer hints) {
        BufferedImage bufferedImage = getBufferedImage(srcPath);
        if (null == bufferedImage) {
            return false;
        }

        // 文件后缀名
        String pfix = FileUtil.pfix(newPath);
        bufferedImage = zoomImage(bufferedImage, times, getImageHints(hints));
        try {
            ImageIO.write(bufferedImage, pfix, new File(newPath)); //保存修改后的图像,全部保存为JPG格式
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    private BufferedImage getBufferedImage(String srcPath) {
        BufferedImage bufferedImage = null;
        try {
            File of = new File(srcPath);
            if (of.canRead()) {
                bufferedImage = ImageIO.read(of);
            }

        } catch (IOException e) {
            //TODO: 打印日志
        }

        return bufferedImage;
    }


    private int getImageHints(Integer hints) {
        if (null == hints) {
            // 不开启高保真压缩
            return 0;
        }

        if (hints == 1) {
            //默认缩放模式
            return Image.SCALE_DEFAULT;
        }

        if (hints == 2) {
            //速度优先
            return Image.SCALE_FAST;
        }

        if (hints == 4) {
            //平滑优先
            return Image.SCALE_SMOOTH;
        }

        if (hints == 8) {
            //像素复制型缩放
            return Image.SCALE_REPLICATE;
        }

        if (hints == 16) {
            //区域均值
            return Image.SCALE_AREA_AVERAGING;
        }

        //默认缩放模式
        return Image.SCALE_DEFAULT;
    }

    /**
     * 对图片进行缩放
     *
     * @param originalImage 原始图片
     * @param times         大于1放大倍数、小于1缩小倍数
     * @param hints         缩放模式
     * @return 缩放后的图片
     */
    private BufferedImage zoomImage(BufferedImage originalImage, double times, int hints) {
        int width = (int) (originalImage.getWidth() * times);
        int height = (int) (originalImage.getHeight() * times);
        return newImage(originalImage, width, height, hints);
    }

    // 图片缩放
    private BufferedImage newImage(BufferedImage originalImage, int width, int height, int hints) {
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();

        if (0 != hints) {
            // 高保真缩放
            g.drawImage(originalImage.getScaledInstance(width, height, hints), 0, 0, null);
        } else {
            // 有损缩放
            g.drawImage(originalImage, 0, 0, width, height, null);
        }
        g.dispose();

        return newImage;
    }
}
