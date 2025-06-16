package com.bupt.pojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

//Word读取
@Data
@NoArgsConstructor
@Component
public class WordFileReader {
    public String readWordFile(String fileName) throws IOException {
        return readDocxFIle(fileName);
    }
    private String readDocxFIle(String fileName) throws IOException{
        try (
                FileInputStream fis = new FileInputStream(fileName);
                XWPFDocument document = new XWPFDocument(fis)) {

            StringBuilder text = new StringBuilder();
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                text.append(paragraph.getText()).append("\n");
            }
            // 转化为字符串
            String result = text.toString();
            return result;
        }
    }

}
