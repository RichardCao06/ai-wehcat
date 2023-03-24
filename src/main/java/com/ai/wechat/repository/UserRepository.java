package com.ai.wechat.repository;

import com.ai.wechat.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserRepository extends IService<User> {

    boolean checkUserExist(String userId);
}
