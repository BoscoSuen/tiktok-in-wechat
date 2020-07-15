package com.tiktok.controller;

import com.tiktok.pojo.Users;
import com.tiktok.utils.TiktokSONResult;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@Api(value="视频相关业务的接口", tags = {"视频相关业务的controller"})
@RequestMapping("/video")
public class VideoController {

	@ApiOperation(value="上传视频", notes="上传视频的接口")
	@ApiImplicitParams({
					@ApiImplicitParam(name="userId", value="用户id", required=true,
									dataType="String", paramType="form"),
					@ApiImplicitParam(name="bgmId", value="背景音乐id", required=false,
									dataType="String", paramType="form"),
					@ApiImplicitParam(name="videoSeconds", value="背景音乐播放长度", required=true,
									dataType="String", paramType="form"),
					@ApiImplicitParam(name="videoWidth", value="视频宽度", required=true,
									dataType="String", paramType="form"),
					@ApiImplicitParam(name="videoHeight", value="视频高度", required=true,
									dataType="String", paramType="form"),
					@ApiImplicitParam(name="desc", value="视频描述", required=false,
									dataType="String", paramType="form")
	})
	@PostMapping(value="/upload", headers="content-type=multipart/form-data")
	// headers: wx.upload 后端处理的内容
	public TiktokSONResult upload(String userId,
																String bgmId, double videoSeconds, int videoWidth, int videoHeight, String desc,
																@ApiParam(value="短视频", required=true)
																MultipartFile file) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return TiktokSONResult.errorMsg("id不能为空");
		}


		// 文件保存的命名空间
		String fileSpace = "/Users/suen/project/tiktok-dev/video";

		//保存到数据库中的相对路径
		String uploadPathDB = "/" + userId + "/video";
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;

		try {
			if (file != null) {


				String fileName = file.getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {
					// 文件上传的最终保存路径
					String finalVideoPath = fileSpace + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);

					File outFile = new File(finalVideoPath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父文件夹
						outFile.getParentFile().mkdirs();
					}

					fileOutputStream = new FileOutputStream(outFile);
					inputStream = file .getInputStream();
					// IOUtils: org.apache.common.io
					IOUtils.copy(inputStream, fileOutputStream);
				}
			} else {
				return TiktokSONResult.errorMsg("上传错误");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return TiktokSONResult.errorMsg("上传错误");
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.flush();
				fileOutputStream.close();
			}
		}

		return TiktokSONResult.ok();
	}
	
}
