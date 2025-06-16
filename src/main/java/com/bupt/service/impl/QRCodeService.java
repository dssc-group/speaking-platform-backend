package com.bupt.service.impl;
import com.bupt.mapper.ClassMapper;
import com.bupt.pojo.Account;
import com.bupt.pojo.Member;
import com.bupt.service.MemService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//二维码生成
@Service
public class QRCodeService {
    @Autowired
    private AccountServiceA accountServiceA;
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private MemService memService;

    public byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public Integer createAccAndAttend(Account account){
            int code = accountServiceA.crateStudentAccount(account);
            if(code == -1){
                return -1;
            }
            //创建账户成功
            classMapper.addStudent(account.getUserId(),account.getAttendClassId());
            return 0;
    }
    public Integer loginAndAttend(Integer classId,String userName){
        Member member =  memService.getInfoByName(userName);
        classMapper.addStudent(member.getUserId(),classId);
        return 0;
    }
}
