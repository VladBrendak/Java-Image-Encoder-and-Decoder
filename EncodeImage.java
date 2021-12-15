package com.company;

import java.util.ArrayList;
import java.nio.charset.StandardCharsets;


public class EncodeImage
{
    ImageEditor ImgEd = new ImageEditor();
    String ImageName;

    EncodeImage(String ImgName){ ImageName = ImgName; }

    public void Decode()
    {
        ImgEd.GetImageBytes(ImageName);
        int[][] R = ImgEd.GetRedPixelsArray();
        int[][] G = ImgEd.GetGreenPixelsArray();
        int[][] B = ImgEd.GetBluePixelsArray();

        ArrayList<Integer> FinalBits = new ArrayList<>();
        int[] TwoLimitors = {0, 0};

        int count = 1;
        int columnCount = 0;

        while(TwoLimitors[0] != 8 && TwoLimitors[1] != 8)
        {
            if(count == 1)
            {
                GetPiceOfBits(FinalBits, R, TwoLimitors, columnCount);
            }
            if(count == 2)
            {
                GetPiceOfBits(FinalBits, G, TwoLimitors, columnCount);
            }
            if(count == 3)
            {
                GetPiceOfBits(FinalBits, B, TwoLimitors, columnCount);
                count = 0;
            }
            count++;
            columnCount++;
        }

        int[]Bits = FinalBits.stream().mapToInt(i -> i).toArray();
        byte[] Bites = new byte[Bits.length / 8 - 1];

        DecodeBinaryToBite(Bits, Bites);

        String DecodedMessage = new String(Bites, StandardCharsets.UTF_8);

        System.out.println( "DecodedMessage: " + DecodedMessage);
    }

    private void DecodeBinaryToBite(int[] Bits, byte[] Bites)
    {
        int count = 0;
        int temp = 0;
        int index = 0;
        for(int i = 0; i < Bits.length; i++)
        {
            if(count < 8)
            {
                if(Bits[i] == 1)
                {
                    temp += Math.pow(2, count);
                }
                count++;
            }
            if(count >= 8 && index < Bites.length)
            {
                Bites[index] = (byte)temp;
                index++;
                count = 0;
                temp = 0;
            }
        }
    }

    private void GetPiceOfBits(ArrayList<Integer> FinalBites, int[][] Color, int[] TwoLimitors,int columnCount)
    {
        int count = 1;
        int lim = 0;
        int EightZeroStop = TwoLimitors[0];
        int limiter = TwoLimitors[1];
        for(int i =0; i < Color.length; i++)
        {
            int[] OneBinaryNumb = GetOneBinaryNumb(Color[i][columnCount]);

            if(count > 3)
            {
                count = 1;
            }

            if(count == 1)
            {
                lim = 2;
            }
            else
            {
                lim = 3;
            }

            for(int j = 0; j < lim; j++)
            {
                FinalBites.add(OneBinaryNumb[j]);

                limiter++;
                if(OneBinaryNumb[j] == 0)
                {
                    EightZeroStop++;
                }

                if(limiter == 8 && EightZeroStop < 8)
                {
                    limiter = 0;
                    EightZeroStop = 0;
                }

                if(EightZeroStop == 8)
                {
                    TwoLimitors[0] = EightZeroStop;
                    TwoLimitors[1] = limiter;
                    break;
                }
            }
            if(EightZeroStop == 8 && limiter == 8)
            {
                break;
            }
            count++;
        }
    }

    private int[] GetOneBinaryNumb(int numb)
    {
        int[] FinalArr = new int[8];
        int count = 0;

        int tempValue = numb;
        for(int i =0; i < 8; i++)
        {
            FinalArr[i] = tempValue % 2;
            tempValue /= 2;
        }
        return FinalArr;
    }
}
