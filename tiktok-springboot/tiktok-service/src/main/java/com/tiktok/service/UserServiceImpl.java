package com.tiktok.service;

import com.tiktok.mapper.UsersMapper;
import com.tiktok.pojo.Users;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    // 支持当前事务，如果当前没有事务，就以非事务方式执行
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);

        Users result = usersMapper.selectOne(user);

        return result != null;
    }

    // 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users user) {
        // 使用自动生成的id(common utils idworker),不使用自增的
        String userId = sid.nextShort();
        user.setId(userId);
        usersMapper.insert(user);
    }
}
