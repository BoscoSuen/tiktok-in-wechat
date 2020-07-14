package com.tiktok.service.Impl;

import com.tiktok.mapper.BgmMapper;
import com.tiktok.pojo.Bgm;
import com.tiktok.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    // 支持当前事务，如果当前没有事务，就以非事务方式执行
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Bgm> queryBgmList() {
        return bgmMapper.selectAll();
    }
}
