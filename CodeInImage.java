package com.company;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CodeInImage
{
    Scanner in = new Scanner(System.in);
    private byte[] MessageBytes = null;
    private int[] MessageInBinary = null;
    ImageEditor ImgEditor = new ImageEditor();

    CodeInImage() {}

    public void StartToCode()
    {
        String message = GetMessageToCode();
        GetMessageBytes(message);
        GetMessageInBinary();

        // I will separate this 00000000 to this 00|000|000
        // So make it 9 digit (000|000|000) to divide by 3 and get right answer
        int SizeOfMesgInPixels = ((MessageInBinary.length + MessageInBinary.length / 8) / 3);

        ImgEditor.GetImageBytes("pngimage2.png");

        CodeIn(ImgEditor.GetRedPixelsArray(),
                ImgEditor.GetGreenPixelsArray(),
                ImgEditor.GetBluePixelsArray(),
                SizeOfMesgInPixels,
                ImgEditor.GetHeight());

        ImgEditor.SetImageBytes("new-pngimage2.png");
    }

    public void CodeIn(int[][] R, int[][] G, int[][] B, int SizeOfMessageInPixels, int height)
    {
        int ThreePixelStepCount = SizeOfMessageInPixels;
        int StepsCount = 1;

        while(ThreePixelStepCount > height)
        {
            ThreePixelStepCount -= height;
            StepsCount++;
        }

        int[] AllBits = new int[height * StepsCount * 8];

        int count = 1;
        int index = 0;
        int startOf = 0;
        while(index < StepsCount)
        {
            if (count == 1)
            {
                CodeColumn(AllBits, R, height, index, startOf);
                index++;
            }
            if (count == 2)
            {
                CodeColumn(AllBits, G, height, index, startOf);
                index++;
            }
            if (count == 3)
            {
                CodeColumn(AllBits, B, height, index, startOf);
                index++;
                count = 0;
            }
            count++;
            startOf += height*8;
        }

        ChangeBits(MessageInBinary, AllBits);

        AllBits = ToByteValues(AllBits);

        int columnIndex = 0;
        count = 1;
        index = 0;
        while(columnIndex < StepsCount)
        {
            if (count == 1)
            {
                CodeColorColumn(R, AllBits, height, index, columnIndex);
            }
            if(count == 2 )
            {
                CodeColorColumn(G, AllBits, height, index, columnIndex);
            }
            if(count == 3 )
            {
                CodeColorColumn(B, AllBits, height, index, columnIndex);
                count = 0;
            }
            count++;
            index += height;
            columnIndex++;
        }
    }

    private void CodeColorColumn(int[][] Color, int[] AllBits, int height, int index, int coulumnIndex)
    {
        int count = 0;
        for (int i = index; i < AllBits.length; i++)
        {
            if(count < height)
            {
                Color[count][coulumnIndex] = AllBits[i];
                count++;
            }
            else
            {
                break;
            }
        }
    }


    private void CodeColumn(int[] AllBits, int[][] Color, int height, int index, int startOf)
    {
        int[] Binary = Int2DToBinary(Color, height, index);

        int count = 0;

        while(startOf < AllBits.length)
        {
            if(count < Binary.length)
            {
                AllBits[startOf] = Binary[count];
                count++;
            }
            else
            {
                break;
            }
            startOf++;
        }
    }

    public static int[] Int2DToBinary(int[][] StartArr, int Height, int ColumnIndex)
    {
        int[] FinalArr = new int[Height * 8];
        int count = 0;
        for(int j = 0; j < Height; j++)
        {
            int tempValue = StartArr[j][ColumnIndex];
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

    private void ChangeBits(int[] arr, int[]NewArr)
    {
        int Choise = 1;
        int step1 = 0, num = 0;
        int index = 0;
        int count = 0;

        // MA
        // 1011001010000010 00000000
        //

        for(int i = 0; i < NewArr.length; i++)
        {
            switch (Choise)
            {
                case 1:
                    step1 = 6;
                    num = 2;
                    break;
                case 2:
                    step1 = 5;
                    num = 3;
                    break;
                case 3:
                    step1 = 5;
                    num = 3;
                    break;
            }

            if(index < arr.length)
            {
                NewArr[i] = arr[index];
                index++;
                count++;
                if(count == num)
                {
                    i+=step1;
                    count = 0;
                    Choise++;
                    if(Choise > 3)
                    {
                        Choise = 1;
                    }
                }
            }
        }
    }

    private String GetMessageToCode()
    {
        System.out.print("Enter message you want to code: ");
        String message = in.nextLine();
        return message;
    }

    private void GetMessageBytes(String message)
    {
        MessageBytes = message.getBytes(StandardCharsets.UTF_8);
    }

    private void GetMessageInBinary()
    {
        MessageInBinary = ImgEditor.ByteToBinary(MessageBytes, 8);
    }

    public int[] ToByteValues(int[] arr)
    {
        int[] tempArray = new int[arr.length / 8];
        int index = 0;
        int temp = 0;
        int count = 0;
        for(int i = 0; i < arr.length; i++)
        {
            if(count < 8 && index < tempArray.length)
            {
                if(arr[i] == 1)
                {
                    temp += Math.pow(2, count);
                }
                count++;
            }
            if(count >= 8)
            {
                tempArray[index] = temp;
                index++;
                count = 0;
                temp = 0;
            }
        }
        return tempArray;
    }
}
