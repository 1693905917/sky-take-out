package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @BelongsProject: sky-take-out
 * @BelongsPackage: com.sky.controller.admin
 * @Author: ASUS
 * @CreateTime: 2023-07-28  12:35
 * @Description: TODO
 * @Version: 1.0
 */
/*
 * @description:通用接口  后面展示菜单也需要这个图片展示接口 所以这里就设置为通用接口
 * @author:  HZP
 * @date: 2023/7/28 12:35
 * @param:
 * @return:
 **/
@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /*
     * @description:文件上传
     **/
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传:{}",file);
        try {
            //原始文件名：你上传前这个图片的名称
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀  dfdfdf.png  就是截取出dfdfdf
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称:为了是防止你上传的图片名重名导致图片覆盖
            String objectName=UUID.randomUUID().toString() + extension;
            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败:{}",e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
