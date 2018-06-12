package com.example.springboot2.test;

import ch.qos.logback.core.util.FileUtil;
import com.sun.jmx.snmp.SnmpUnsignedInt;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.util.UUID;

public class RgbaTest {

    public static void main(String[] args) throws Exception {

        byte[] bytes = RgbaTest.File2byte("D:\\ComposeImageTest\\821b73fac3354bec9b728bc615e4175a-result_1342X1728.rgb32");
        System.out.println(bytes.length);
        BufferedImage image = new BufferedImage(1342,1728, BufferedImage.TYPE_INT_ARGB);
        int idx = 0;
        for (int j =0;j<image.getHeight();j++ )
        {
            for (int i = 0; i <image.getWidth();i++){
                int r = bytes[idx++] & 0xFF;
                int g = bytes[idx++] & 0xFF;
                int b = bytes[idx++] & 0xFF;
                int a = bytes[idx++] & 0xFF;
                //a = 0xFF;
                //Color color = new Color(r,g,b,a);
                int val = (a<<24)+(r<<16)+(g<<8)+b;
                image.setRGB(i, j, val);

                if (i == 752 && j == 110) {
                    System.out.println("111");
                }

            }
        }
        String filename = "1214.png";
        OutputStream outImage = new FileOutputStream("D:\\ComposeImageTest\\" + filename);
        ImageIO.write(image, "png", outImage);

    }
/*
    public int getRGB(int x, int y) {
        int pos = (y * pixelLength * width) + (x * pixelLength);
        int argb = -16777216;
        if (hasAlphaChannel) {
            argb = (((int) pixels[pos++] & 0xff) << 24);
        }
        argb += ((int) pixels[pos++] & 0xff);
        argb += (((int) pixels[pos++] & 0xff) << 8);
        argb += (((int) pixels[pos++] & 0xff) << 16);
        return argb;
    }*/

    public static byte[] File2byte(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }



}
