package com.chilly.service;

import com.chilly.dao.UserDAO;
import com.chilly.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Chilly Cui on 2020/9/9.
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDAO userDAO;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User login(User user) {
        User userDB = userDAO.login(user);
        if (userDB != null) {
            return userDB;
        }
        throw new RuntimeException("认证失败");
    }
}
