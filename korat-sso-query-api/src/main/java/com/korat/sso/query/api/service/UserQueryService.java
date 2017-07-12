package com.korat.sso.query.api.service;

import com.korat.sso.query.api.domain.User;

public interface UserQueryService {
    /**
     * 根据token查询User对象
     * @param token
     * @return
     */
    User queryUserByToken(String token);
}
