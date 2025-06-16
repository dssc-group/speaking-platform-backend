package com.bupt.controller;

import com.bupt.pojo.Account;
import com.bupt.pojo.Result;
import com.bupt.service.impl.QRCodeService;
import com.bupt.utils.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class QRCodeController {

    private final QRCodeService qrCodeService;

    public QRCodeController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping(value = "/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generateQRCode(@RequestParam("url") String url, @RequestParam("width") int width, @RequestParam("height") int height) throws Exception {
        byte[] image = qrCodeService.generateQRCodeImage(url, width, height);
        return new ResponseEntity<>(image, HttpStatus.OK);
    }


    @PostMapping("/createAccountAndAttend")
    public Result createAccountAndAttend(@ModelAttribute Account account){
        if(qrCodeService.createAccAndAttend(account)==-1){
            return Result.error("账户已存在，请登录！");
        };
        return Result.success("加入班级成功！");
    }
    @GetMapping("/loginAccountAndAttend")
    public Result loginAndAttend(@RequestParam("classId") Integer classId,@RequestHeader("token") String token ){
        Map<String,Object> claims = JwtUtils.parseJwt(token);
        if(qrCodeService.loginAndAttend(classId, String.valueOf(claims.get("username")))==0){
            return Result.success("加入班级成功");
        }
        return Result.error("加入班级失败，请重试");
    }
}
