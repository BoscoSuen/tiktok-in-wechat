package com.tiktok.service;

import com.tiktok.pojo.Users;

public interface UserService {
    public boolean queryUsernameIsExist(String username);

    /**
     * 保存用户(用于注册)
     * @param user
     */
    public void saveUser(Users user);

    /**
     * 根据用户名和密码查询用户(用于登陆)
     * @param username
     * @param password
     * @return
     */
    public Users queryUserForLogin(String username, String password);
}
