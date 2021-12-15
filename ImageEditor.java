package com.company;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.*;


public class ImageEditor
{
    private int[] RGBarray = null;
    private int w = 0;
    private int h = 0;

    //Declare color arrays
    private int [][] alphaPixels = null;
    private int [][] redPixels = null;
    private int [][] greenPixels = null;
    private int [][] bluePixels = null;

    private BufferedImage imgBuf =null;
    private String filepath = "D:\\CODE practice\\Java\\fileWriteTest";

    ImageEditor() {}

    public void GetImageBytes(String ImageName)
    {
        try
        {
            //Read image file
            imgBuf = ImageIO.read(new File(filepath + "\\" + ImageName));

            //Get width and hieght of image
            h = imgBuf.getHeight();
            w = imgBuf.getWidth();

            RGBarray = imgBuf.getRGB(0,0,w,h,null,0,w);

            alphaPixels = new int [h][w];
            redPixels = new int [h][w];
            greenPixels = new int [h][w];
            bluePixels = new int [h][w];

            //Bit shift values into arrays
            int i=0;
            for(int row=0; row<h; row++)
            {
                for(int col=0; col<w; col++)
                {
                    alphaPixels[row][col] = ((RGBarray[i]>>24)&0xff);
                    redPixels[row][col] = ((RGBarray[i]>>16)&0xff);
                    greenPixels[row][col] = ((RGBarray[i]>>8)&0xff);
                    bluePixels[row][col] = (RGBarray[i]&0xff);
                    i++;
                }
            }
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        };
    }

    public int[][] GetRedPixelsArray() { return redPixels;}

    public int[][] GetGreenPixelsArray()
    {
        return greenPixels;
    }

    public int[][] GetBluePixelsArray()
    {
        return bluePixels;
    }

    public int GetHeight(){ return h; }

    public int GetWidth(){ return w; }

    public int[] ByteToBinary(byte[] StartArr, int ExtraSpace)
    {
        int[] FinalArr = new int[StartArr.length * 8 + ExtraSpace];
        int count = 0;
        for(int i = 0; i < StartArr.length; i++)
        {
            int tempValue = StartArr[i];
            byte CountOfBites = 8;
            while(CountOfBites > 0)
            {
                FinalArr[count] = tempValue % 2;
                count++;
                tempValue /= 2;
                CountOfBites--;
            }
        }
        return FinalArr;
    }

    public void SetImageBytes(String ImageName)
    {
        try
        {
            for(int row=0; row<h; row++)
            {
                for(int col=0; col<w; col++)
                {
                    int rgb = (alphaPixels[row][col] & 0xff) << 24 | (redPixels[row][col] & 0xff) << 16 | (greenPixels[row][col] & 0xff) << 8 | (bluePixels[row][col] & 0xff);
                    imgBuf.setRGB(col, row, rgb);
                }
            }

            //Write back image
            ImageIO.write(imgBuf, "png", new File(filepath + "\\" + ImageName));
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        };
    }
}

