package com.example.springboot2.test;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by zx9035 on 2018/6/4.
 */
public class ComposeImageTest {

    public static void main(String[] args) throws Exception {
        File file1 = new File("D:\\ComposeImageTest\\1.jpg");
        File file2 = new File("D:\\ComposeImageTest\\2.png");
        InputStream imagein = new FileInputStream(file1);
        InputStream imagein2 = new FileInputStream(file2);

        BufferedImage image = ImageIO.read(imagein);
        BufferedImage image2 = ImageIO.read(imagein2);
        Graphics g = image.getGraphics();
        g.drawImage(image2, image.getWidth() - image2.getWidth() - 150, image.getHeight() - image2.getHeight() + 100,
                image2.getWidth() + 400, image2.getHeight() + 200, null);
        OutputStream outImage = new FileOutputStream("D:\\ComposeImageTest\\3.jpg");
        JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(outImage);
        enc.encode(image);
        imagein.close();
        imagein2.close();
        outImage.close();
    }

}
