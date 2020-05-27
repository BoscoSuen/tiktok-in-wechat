package com.tiktok.controller;

import com.tiktok.pojo.Users;
import com.tiktok.service.UserService;
import com.tiktok.utils.MD5Utils;
import com.tiktok.utils.TiktokSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterLoginController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/regist")
	public TiktokSONResult Hello(@RequestBody Users user) throws Exception {
		// 1. 判断用户名密码不能为空
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return TiktokSONResult.errorMsg("用户名和密码不能为空");
		}
		// 2. 判断用户名是否存在
		// 调用service层
		boolean userNameIsExist = userService.queryUsernameIsExist(user.getUsername());

		// 3. 保存+注册
		if (!userNameIsExist) {
			// 保存
			user.setNickname(user.getUsername());
			user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			user.setFansCounts(0);
			user.setReceiveLikeCounts(0);
			user.setFollowCounts(0);
			userService.saveUser(user);
		} else {
			return TiktokSONResult.errorMsg("用户名已存在");
		}

		return TiktokSONResult.ok();
	}
	
}
