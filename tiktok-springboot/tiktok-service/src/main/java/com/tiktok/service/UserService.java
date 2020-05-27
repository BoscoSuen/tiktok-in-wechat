package com.tiktok.service;

import com.tiktok.pojo.Users;

public interface UserService {
    public boolean queryUsernameIsExist(String username);

    public void saveUser(Users user);
}
