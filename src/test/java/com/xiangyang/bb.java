package com.xiangyang;


import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;

public class bb {
    public static void main(String[] args) {
        PdfDocument pdf = new PdfDocument("D:\\test\\Linux命令行与Shell脚本编程大全(第2版).pdf");
        pdf.saveToFile("D:\\test\\Linux命令行与Shell脚本编程大全(第2版).docx", FileFormat.DOCX);
//        PdfDocument pdf = new PdfDocument("D:\\test\\aa.pdf");
//        pdf.saveToFile("D:\\test\\aa.docx", FileFormat.DOCX);
    }
}
