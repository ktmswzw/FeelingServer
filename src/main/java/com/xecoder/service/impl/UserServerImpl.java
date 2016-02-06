package com.xecoder.service.impl;

import com.xecoder.common.exception.HttpServiceException;
import com.xecoder.common.util.HashPassword;
import com.xecoder.common.util.RadomUtils;
import com.xecoder.model.business.Auth;
import com.xecoder.model.business.AuthToken;
import com.xecoder.model.business.User;
import com.xecoder.model.embedded.DeviceEnum;
import com.xecoder.service.core.AbstractService;
import com.xecoder.service.dao.AuthDao;
import com.xecoder.service.dao.UserDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/16-17:27
 * Feeling.com.xecoder.service.impl
 */

@Service
public class UserServerImpl extends AbstractService<User> {

    private static Logger logger = LoggerFactory.getLogger(UserServerImpl.class);

        /*Pageable pageable = new PageRequest(pageBean.getCurrentPage(), pageBean.getMaxPage(), pageBean.getSort(), pageBean.getSortColumn());
    userRepostitory.findAll(pageable);
    */

    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private AuthServerImpl authServer;

    @Override
    protected MongoRepository<User, String> getRepository() {
        return userDao;
    }

    @Override
    protected long count(User searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query = makeQuery(criteria);
        return doCount(query, User.class);
    }

    @Override
    public List<User> search(int page, int size, Sort sort, User searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query= makeQuery(criteria);
        query.skip(calcSkipNum(page, size)).limit(size);
        if (sort != null) {
            query.with(sort);
        }
        return doFind(query, User.class);
    }

    @Override
    protected Criteria makeCriteriaByPk(User model) {
        return null;
    }

    @Override
    protected Criteria makeCriteria(User model) {
        Criteria criteria = null;
        if (StringUtils.isNotEmpty(model.getNickname())) {
            criteria = makeCriteria(criteria, "nickname", model.getNickname());
        }
        return criteria;
    }

    @Override
    public User findByPk(Object... keys) {
        return userDao.findByPhone((String) keys[0]);
    }

    @Override
    public Iterable<User> findByNameLike(String name, String sortColumn) {
        return null;
    }
//
//    @Override
//    public long searchCount(String keyword) {
//        return userDao.searchCount(keyword);
//    }

    @Override
    public Iterable<User> search(String keyword, int page, int size, String sortColumn) {
        return null;
    }

    public User findByPhone(String phone)
    {
        return userDao.findByPhone(phone);
    }

    public String register(String telephone, String password, DeviceEnum device) {

        User user = userDao.findByPhone(telephone);
        if (user != null) {
            throw new HttpServiceException(getLocalException("error.user.is.exist"));
        }

        user = new User();
        user.setPhone(telephone);
        user.setAvatar("cb3cecad-fbbb-4171-b881-6ed5f281f235");
        user.setNickname(telephone.substring(telephone.length() - 4));
        this.save(user);

        String userId = user.getId();
        byte[] salt = RadomUtils.getRadomByte();
        HashPassword hashPassword = HashPassword.encryptPassword(password, salt);
        Auth auth =new Auth(userId, hashPassword.getSalt());
        auth.setPassword(hashPassword.getPassword());
        AuthToken token = new AuthToken(user, device);
        authServer.storeToken(token);
        auth.addToken(token);
        authDao.save(auth);

       // imService.register(userId, user.getNickname(), user.getAvatar()); //第三方注册
        return token.getToken();
    }

    public AuthToken login(String telephone, String password, DeviceEnum device, String versionStr) {
        User user = userDao.findByPhone(telephone);

        if (user == null) {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }

        Auth auth = authDao.findByOwner(user.getId());
        if (auth == null) {
                throw new HttpServiceException(getLocalException("error.user.not.register"));
        }
        boolean flag = HashPassword.validatePassword(password,auth.getPassword(),auth.getSalt());
        if (!flag) {
                    throw new HttpServiceException(getLocalException("error.user.login.failed"));
        }

        List<AuthToken> tokens = auth.getEffectiveTokens();
        for (AuthToken token : tokens) {
            if (token.getDevice().equals(device)) {
                //authServer.storeToken(token);
                return token;
            }
        }
        AuthToken loginToken = new AuthToken(user, device);
        authServer.storeToken(loginToken);
        auth.addToken(loginToken);
        authDao.save(auth);

        return loginToken;
    }

}
