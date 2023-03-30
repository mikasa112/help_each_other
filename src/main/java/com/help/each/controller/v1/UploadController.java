package com.help.each.controller.v1;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.help.each.core.constant.Status;
import com.help.each.core.vo.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author Yuanan
 * @date 2023/3/28
 * @description 文件上传Controller
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UploadController {
    @Value("${spring.servlet.multipart.location}")
    private String fileTempPath;

    @Value("${app.config.imageUrl}")
    private String imageUrl;


    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.OfStatus(Status.FILE_CONTENT_IS_EMPTY);
        }
        String filename = file.getOriginalFilename();
        String rawFileName = StrUtil.subBefore(filename, ".", true);
        String fileType = StrUtil.subAfter(filename, ".", true);
        //返回文件的名字加上时间戳
        String savedFileName = rawFileName + "-" + DateUtil.current() + "." + fileType;
        String localFilePath = StrUtil.appendIfMissing(fileTempPath, "/") + savedFileName;
        try {
            file.transferTo(new File(localFilePath));
        } catch (IOException e) {
            log.error("【文件上传失败 errMsg : {}", e.getMessage());
            return ApiResponse.OfStatus(Status.FILE_UPLOAD_FAILED);
        }
        String url = imageUrl + savedFileName;
        return ApiResponse.OfSuccess(url);
    }

}
