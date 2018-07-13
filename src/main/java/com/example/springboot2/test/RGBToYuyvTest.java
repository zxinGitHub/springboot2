package com.example.springboot2.test;

import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.UUID;

public class RGBToYuyvTest {

    public static void main(String[] args) throws Exception {

        byte[] imgdata = RGBToYuyvTest.toByteArray("http://content.perfect365.com/explore/cloudmakeup/temp/60dc3e884cd84ef6919bd90c4cdc7d43-17dbb64f5a2644b29cc248ae20c2fa8d.jpg");
        byte[] pYUYV = covertPathToYuyv(imgdata);
        byte2File(pYUYV, "D:\\ComposeImageTest\\", "hahha.rgb24");
    }

    public static byte[] covertPathToYuyv(byte[] imgdata) throws Exception {
        String templatename = UUID.randomUUID().toString().replaceAll("-","") + "_template.jpg";
        FileUtils.byte2File(imgdata, "/opt/temporary/", templatename);
        File file = new File("/opt/temporary/" + templatename);
        int[] widthAndHeight = RGBToYuyvTest.getImageWidthAndHeight(file);
        int width = widthAndHeight[0];
        int height = widthAndHeight[1];
        BufferedImage bufferedImage = ImageIO.read(file);
        //bufferedImage.getData().getDataBuffer()
        int[] pARGB = new int[width*height];
        bufferedImage.getRGB(0,0, width, height, pARGB, 0, width);
        byte[] pYUYV = new byte[width*height*2];
        int pitch = width*2;
        _fxiang_ARGB8888_to_YUYV(pARGB, pYUYV, width, height, pitch);
        return pYUYV;
    }

    public static int[] getImageWidthAndHeight(File file) throws IOException {
        ImageIcon ii = new ImageIcon(file.getCanonicalPath());
        Image i = ii.getImage();
        int[] widthAndHeight = {i.getWidth(null),i.getHeight(null)};
        return widthAndHeight;
    }

    public static void _fxiang_ARGB8888_to_YUYV(int[] pARGB, byte[] pYUYV,
                                  int width, int height, int pitch)
    {
        int x,y;
        int r,g,b;
        int Y1,Cb1,Cr1;
        int Y2,Cb2,Cr2;
        int Cb,Cr;
        int offset_src;
        int width2 = width&(~1);

        offset_src = pitch - width2*4;

        int idx =0;
        int idx2 =0;
        y = height;
        while (y-- > 0)
        {
            x = width2>>1;
            while (x-- > 0)
            {
                int val1 = pARGB[idx++];
                Color color1 = new Color(val1);
                r = color1.getRed();
                g = color1.getGreen();
                b = color1.getBlue();

                Y1=(19595*r+38470*g+7471*b)>>16;
                Cb1=(32768*b-11059*r-21709*g)>>16;
                Cr1=(32768*r-27439*g-5329*b)>>16;

                int val2 = pARGB[idx++];
                Color color2 = new Color(val2);
                r = color2.getRed();
                g = color2.getGreen();
                b = color2.getBlue();


                Y2=(19595*r+38470*g+7471*b)>>16;
                Cb2=(32768*b-11059*r-21709*g)>>16;
                Cr2=(32768*r-27439*g-5329*b)>>16;

                Cb = ((Cb1 + Cb2)>>1)+128;
                Cr = ((Cr1 + Cr2)>>1)+128;

                pYUYV[idx2++] = (byte) (Y1 > 255 ? ((-(Y1))>>31) : (Y1));
                pYUYV[idx2++] = (byte) (Cb > 255 ? ((-(Cb))>>31) : (Cb));
                pYUYV[idx2++] = (byte) (Y2 > 255 ? ((-(Y2))>>31) : (Y2));
                pYUYV[idx2++] = (byte) (Cr > 255 ? ((-(Cr))>>31) : (Cr));
            }
        }
    }

    public static byte[] toByteArray(String path) throws Exception {
        URL url = new URL(path);
        //打开链接
        InputStream inStream = url.openStream();
        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = readInputStream(inStream);
        return data;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

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
