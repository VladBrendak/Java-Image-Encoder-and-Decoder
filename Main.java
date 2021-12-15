package com.company;

public class Main
{
    public static void main(String[] args)
    {
        CodeInImage CII = new CodeInImage();
        CII.StartToCode();

        EncodeImage EI = new EncodeImage("new-pngimage2.png");
        EI.Decode();
    }
}

