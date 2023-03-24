package com.ai.wechat.repository.impl;

import com.ai.wechat.domain.User;
import com.ai.wechat.mapper.UserMapper;
import com.ai.wechat.repository.UserRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
public class UserRepositoryImpl extends ServiceImpl<UserMapper, User> implements UserRepository {

    @Override
    public boolean checkUserExist(String userId) {
        return this.lambdaQuery().eq(User::getOpenId, userId).exists();

    }


}
