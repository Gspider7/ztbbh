package com.acrobat.ztb.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ImagesUtil {

    /**
      * compressImage
      * 
      * @param imageByte
      *  Image source array
      * @param ppi
      * @return
      */
    public static byte[] compressImage(byte[] imageByte, int ppi) {
        byte[] smallImage = null;
        int width = 0, height = 0;

        if (imageByte == null)
            return null;

        ByteArrayInputStream byteInput = new ByteArrayInputStream(imageByte);
        try {
            Image image = ImageIO.read(byteInput);
            int w = image.getWidth(null);
            int h = image.getHeight(null);
            // adjust weight and height to avoid image distortion
            double scale = 0;
            scale = Math.min((float) ppi / w, (float) ppi / h);
            width = (int) (w * scale);
            width -= width % 4;
            height = (int) (h * scale);

            if (scale >= (double) 1)
                return imageByte;

            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            buffImg.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(buffImg, "png", out);
            smallImage = out.toByteArray();
            return smallImage;

        } catch (IOException e) {
            log.error("图片压缩失败！", e);
            return null;
        }
    }

    /**
     * compressImage
     *
     * @param path
     * @param ppi
     * @return
     */
    public static byte[] compressImage(String path, int ppi) {
        byte[] smallImage = null;

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Thumbnails.of(path).size(ppi, ppi).outputFormat("png").toOutputStream(out);
            smallImage = out.toByteArray();
            return smallImage;
        } catch (IOException e) {
            log.error("图片压缩失败！", e);
            return null;
        }
    }

    public static void compressImage(String fromPath, String toPath,
                                     double ratio) throws IOException {
        File imageFile = new File(fromPath);

        Image image = ImageIO.read(imageFile);
        int w = image.getWidth(null);
        int h = image.getHeight(null);

        int newWidth = (int) (w * ratio);
        int newHeight = (int) (h * ratio);
        Thumbnails.of(fromPath).size(newWidth, newHeight).toFile(toPath);
    }

    public static void main(String[] args) throws IOException {

        compressImage("C:\\Users\\xt\\Desktop\\a\\申请表.jpg",
                "C:\\Users\\xt\\Desktop\\a\\申请表2.jpg", 0.6);
    }
}