package com.xiangyang;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;

public class pdfToWordTest {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\test\\aa.pdf");
        PDDocument doc=PDDocument.load(file);
        int pagenumber=doc.getNumberOfPages();
        System.out.print("pages"+pagenumber);
        FileOutputStream fos=new FileOutputStream("D:\\test\\Linux命令行与Shell脚本编程大全(第2版).doc");
        Writer writer=new OutputStreamWriter(fos,"UTF-8");
        PDFTextStripper stripper=new PDFTextStripper();

        stripper.setSortByPosition(true);//排序
        stripper.setStartPage(200);//设置转换的开始页
        stripper.setEndPage(10000000);//设置转换的结束页
        stripper.writeText(doc,writer);
        writer.close();
        doc.close();

    }
}
