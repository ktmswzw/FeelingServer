package com.xecoder.service.service;

import com.xecoder.common.exception.HttpServiceException;
import com.xecoder.common.util.HashPassword;
import com.xecoder.common.util.ImageUtil;
import com.xecoder.common.util.RandomUtils;
import com.xecoder.model.business.Auth;
import com.xecoder.model.business.AuthToken;
import com.xecoder.model.business.User;
import com.xecoder.model.embedded.DeviceEnum;
import com.xecoder.service.core.AbstractService;
import com.xecoder.dao.AuthDao;
import com.xecoder.dao.UserDao;
import com.xecoder.restful.RongCloudController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/16-17:27
 * Feeling.com.xecoder.service.impl
 */

@Service(value = "userService")
public class UserService extends AbstractService<User> {

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

        /*Pageable pageable = new PageRequest(pageBean.getCurrentPage(), pageBean.getMaxPage(), pageBean.getSort(), pageBean.getSortColumn());
    userRepostitory.findAll(pageable);
    */

    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthDao authDao;

    @Autowired
    ImageSignService signService;

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
        Query query = makeQuery(criteria);
        query.skip(calcSkipNum(page, size)).limit(size);
        if (sort != null) {
            query.with(sort);
        }
        return doFind(query, User.class);
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


    public User findByPhone(String phone) {
        return userDao.findByPhone(phone);
    }

    @Transactional
    public String register(String telephone, String password, DeviceEnum device) {

        User user = userDao.findByPhone(telephone);
        if (user != null) {
            throw new HttpServiceException(getLocalException("error.user.is.exist"));
        }

        user = new User();
        user.setPhone(telephone);
        user.setAvatar("0598e899-3524-4349-8e4e-db692ba343d2");
        user.setNickname(telephone.substring(telephone.length() - 4));
        this.save(user);

        String userId = user.getId();
        byte[] salt = RandomUtils.getRadomByte();
        HashPassword hashPassword = HashPassword.encryptPassword(password, salt);
        Auth auth = new Auth(userId, hashPassword.getSalt());
        auth.setPassword(hashPassword.getPassword());
        AuthToken token = null;
        try {
            token = new AuthToken(user, device);
            auth.addToken(token);
            authDao.save(auth);
        } catch (Exception e) {
            this.delete(user.getId());    //回滚
        }

        // imService.register(userId, user.getNickname(), user.getAvatar()); //第三方注册
        return token.getJwt();
    }

    public User login(String telephone, String password, DeviceEnum device, String versionStr) {
        User user = userDao.findByPhone(telephone);

        if (user == null) {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }

        Auth auth = authDao.findByOwner(user.getId());
        if (auth == null) {
            throw new HttpServiceException(getLocalException("error.user.not.register"));
        }
        boolean flag = HashPassword.validatePassword(password, auth.getPassword(), auth.getSalt());
        if (!flag) {
            throw new HttpServiceException(getLocalException("error.user.login.failed"));
        }

        String avatar = StringUtils.isBlank(user.getAvatar())?"": ImageUtil.getPathSmall(user.getAvatar());
        AuthToken loginToken = new AuthToken(user, device);//重新生成
        List<AuthToken> tokens = auth.getEffectiveTokens();
        Iterator<AuthToken> itr = tokens.iterator();
        while (itr.hasNext()) {
            AuthToken t = itr.next();
            if (t.getDevice() == device) {
                itr.remove();//同设备删除
            }
        }
        auth.addToken(loginToken);
        authDao.save(auth);

        RongCloudController rongCloudController = new RongCloudController();
        String imtoken = rongCloudController.getIMK(user.getId(),user.getNickname(),avatar);
        user.setIMToken(StringUtils.isNotBlank(imtoken)?imtoken:"");
        user.setAvatar(avatar);
        user.setJWTToken(loginToken.getJwt());
        user.setSignToken(signService.getQSign());

        return user;
    }

}
