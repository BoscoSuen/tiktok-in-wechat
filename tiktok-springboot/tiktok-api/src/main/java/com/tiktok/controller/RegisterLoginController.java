package com.tiktok.controller;

import com.tiktok.pojo.Users;
import com.tiktok.utils.TiktokSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterLoginController {
	
	@PostMapping("/regist")
	public TiktokSONResult Hello(@RequestBody Users user) {
		// 1. 判断用户名密码不能为空
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return TiktokSONResult.errorMsg("用户名和密码不能为空");
		}
		// 2. 判断用户名是否存在

		// 3. 保存+注册

		return TiktokSONResult.ok();
	}
	
}
