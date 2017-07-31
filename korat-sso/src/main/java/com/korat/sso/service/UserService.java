package com.korat.sso.service;

import com.github.abel533.entity.Example;
import com.korat.sso.domain.User;
import com.korat.sso.mapper.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 * UserService
 *
 * @author solar
 * @date 2017/6/24
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 验证用户是否存在
     * @param param
     * @param type
     * @return
     */
    public Boolean check(String param, Integer type) {
        if (StringUtils.isBlank(param) && StringUtils.isBlank(type.toString())) {
            return false;
        }
        User user=new User();
        switch (type) {
            case 1:
                user.setUsername(param);
                break;
            case 2:
                user.setPhone(param);
                break;
            case 3:
                user.setEmail(param);
                break;
            default:
                return null;
        }
        return userMapper.selectOne(user)==null;
    }

    /**
     * 保存用户
     *
     * @param user
     * @return
     */
    public Boolean saveUser(User user) {
        if (user == null) {
            return false;
        }
        user.setCreated(new Date());
        user.setUpdated(new Date());
        user.setId(null);
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        Integer count = userMapper.insert(user);
        return count ==1;
    }

    /**
     * 通过用户名查找用户信息
     *
     * @param userName
     * @return
     */
    public User queryUserByUsername(String userName) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username", userName);
        return userMapper.selectByExample(example).get(0);
    }
}
