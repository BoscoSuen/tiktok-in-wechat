package com.tiktok.controller;

import com.tiktok.pojo.Users;
import com.tiktok.pojo.vo.UsersVO;
import com.tiktok.service.UserService;
import com.tiktok.utils.MD5Utils;
import com.tiktok.utils.TiktokSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.util.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@Api(value = "用户相关业务接口", tags = {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController extends BasicController {

	@Autowired
	private UserService userService;



	@ApiOperation(value="用户上传头像", notes = "用户上传头像的接口")
	@ApiImplicitParam(name = "userId", value="用户id", required = true, dataType = "String", paramType = "query")
	@PostMapping("/uploadFace")
	public TiktokSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return TiktokSONResult.errorMsg("id不能为空");
		}


		// 文件保存的命名空间
		String fileSpace = "/Users/suen/project/tiktok-dev";

		//保存到数据库中的相对路径
		String uploadPathDB = "/" + userId + "/face";
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;

		try {
			if (files != null && files.length > 0) {


				String fileName = files[0].getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {
					// 文件上传的最终保存路径
					String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
					// 设置数据库保存的路径
					uploadPathDB += ("/" + fileName);

					File outFile = new File(finalFacePath);
					if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
						// 创建父文件夹
						outFile.getParentFile().mkdirs();
					}

					fileOutputStream = new FileOutputStream(outFile);
					inputStream = files[0].getInputStream();
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

		Users user = new Users();
		user.setId(userId);
		user.setFaceImage(uploadPathDB);
		userService.updateUserInfo(user);

		return TiktokSONResult.ok(uploadPathDB);
	}


	@ApiOperation(value="查询用户信息", notes = "查询用户信息的接口")
	@ApiImplicitParam(name = "userId", value="用户id", required = true, dataType = "String", paramType = "query")
	@PostMapping("/query")
	public TiktokSONResult query(String userId) throws Exception {

		if (StringUtils.isBlank(userId)) {
			return TiktokSONResult.errorMsg("id不能为空");
		}

		Users userInfo = userService.queryUserInfo(userId);
		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(userInfo, userVO);

		return TiktokSONResult.ok(userVO);
	}
}
