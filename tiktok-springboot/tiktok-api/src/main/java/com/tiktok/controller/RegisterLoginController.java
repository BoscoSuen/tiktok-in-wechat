package com.tiktok.controller;

import com.tiktok.pojo.Users;
import com.tiktok.pojo.vo.UsersVO;
import com.tiktok.service.UserService;
import com.tiktok.utils.MD5Utils;
import com.tiktok.utils.TiktokSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Api(value = "用户注册登录接口", tags = {"注册和登陆的controller"})
public class RegisterLoginController extends BasicController {

	@Autowired
	private UserService userService;

	public UsersVO setUserRedisSessionToken(Users user) {
		String uniqueToken = UUID.randomUUID().toString();
		redis.set(USER_REDIS_SESSION + ":" + user.getId(), uniqueToken, 1000 * 60 * 30); // ttl 30min

		UsersVO userVO = new UsersVO();
		BeanUtils.copyProperties(user, userVO);
		userVO.setUserToken(uniqueToken);
		return userVO;
	}

	@ApiOperation(value = "用户登录", notes = "用户注册接口")
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

		user.setPassword("");	// 前端无法查看密码

//		String uniqueToken = UUID.randomUUID().toString();
//		redis.set(USER_REDIS_SESSION + ":" + user.getId(), uniqueToken, 1000 * 60 * 30); // ttl 30min
//
//		UsersVO userVO = new UsersVO();
//		BeanUtils.copyProperties(user, userVO);
//		userVO.setUserToken(uniqueToken);

		UsersVO userVO = setUserRedisSessionToken(user);

		return TiktokSONResult.ok(userVO);
	}

	@ApiOperation(value="用户登录", notes = "用户登录接口")
	@PostMapping("/login")
	public TiktokSONResult login(@RequestBody Users user) throws Exception {
		String username = user.getUsername();
		String password = user.getPassword();

		// 1. 判断用户名密码不能为空
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return TiktokSONResult.errorMsg("用户名和密码不能为空");
		}

		// 2. 判断用户名是否存在
		// 调用service层
		Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));

		// 3. 返回
		if (userResult != null) {
			userResult.setPassword("");
			UsersVO userVO = setUserRedisSessionToken(userResult);
			return TiktokSONResult.ok(userVO);
		} else {
			return TiktokSONResult.errorMsg("用户名或密码不正确, 请重试...");
		}

	}
	
}
