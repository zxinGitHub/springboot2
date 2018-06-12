package com.example.springboot2.test;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.*;

/**
 * Created by zx9035 on 2018/6/4.
 */
public class ComposeImageTest {

    /*public static void main(String[] args) throws Exception {
        File file1 = new File("D:\\ComposeImageTest\\4.png");
        File file2 = new File("D:\\ComposeImageTest\\2.png");
        InputStream imagein = new FileInputStream(file1);
        InputStream imagein2 = new FileInputStream(file2);

        BufferedImage image = ImageIO.read(imagein);
        BufferedImage image2 = ImageIO.read(imagein2);
        Graphics g = image.getGraphics();
        //g.drawImage(image2, image.getWidth() - image2.getWidth() - 150, image.getHeight() - image2.getHeight() + 100,
        //        image2.getWidth() + 400, image2.getHeight() + 200, null);
        g.drawImage(image2, -270, 80,
                image2.getWidth()+500, image2.getHeight()+100, null);
        OutputStream outImage = new FileOutputStream("D:\\ComposeImageTest\\6.jpg");
        ImageIO.write(image, "jpg", outImage);
        //JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(outImage);
        //enc.encode(image);
        imagein.close();
        imagein2.close();
        outImage.close();
    }*/

    public static void main(String[] args) throws Exception {
        byte[] data = new byte[100*100*4];
        for (int i =0; i < 100*100*2 ; i ++) {
            data[i] = 0;
        }
        for (int i =100*100*2; i < 100*100*4 ; i ++) {
            data[i] = (byte) 255;
        }

        BufferedImage image = new BufferedImage(100,100, BufferedImage.TYPE_INT_ARGB_PRE);

        for (int i =0; i < 50 ; i ++) {
            for (int j =0; j < 100 ; j ++) {
                image.setRGB(i,j,0x00123456);
            }
        }

        for (int i =50; i < 100 ; i ++) {
            for (int j =0; j < 100 ; j ++) {
                image.setRGB(i,j,0xffffff00);
            }
        }
        OutputStream outImage = new FileOutputStream("D:\\ComposeImageTest\\112.png");
        ImageIO.write(image, "png", outImage);
        /*Graphics g = image.getGraphics();
        Raster raster = image.getData();
        g.drawBytes(data, 0, 100, 100, 0);*/

    }


    /*public static void main(String[] args) {

    }*/


    public static File byte2File(byte[] buf, String filePath, String fileName)
    {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try
        {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory())
            {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


}
