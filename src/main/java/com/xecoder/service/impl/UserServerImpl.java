package com.xecoder.service.impl;

import com.xecoder.common.exception.CustomException;
import com.xecoder.common.util.CryptoUtils;
import com.xecoder.common.util.RadomUtils;
import com.xecoder.model.business.Auth;
import com.xecoder.model.business.AuthToken;
import com.xecoder.model.business.DeviceEnum;
import com.xecoder.model.business.User;
import com.xecoder.service.core.AbstractService;
import com.xecoder.service.dao.AuthRepository;
import com.xecoder.service.dao.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

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
    private UserRepository userRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private AuthServerImpl authServer;

    @Inject
    private MessageSource messageSource;

    @Override
    protected MongoRepository<User, String> getRepository() {
        return userRepository;
    }

    @Override
    protected long count(User searchCondition) {
        Criteria criteria = makeCriteria(searchCondition);
        Query query = makeQuery(criteria);
        return doCount(query, User.class);
    }

    @Override
    protected List<User> search(int page, int size, Sort sort, User searchCondition) {
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
    protected Update makeAllUpdate(User model) {
        Update update = new Update();
        update.set("nickname", model.getNickname());
        update.set("avatar", model.getAvatar());
        return update;
    }

    @Override
    public User findByPk(Object... keys) {
        return userRepository.findByPhone((String) keys[0]);
    }

    @Override
    public Iterable<User> findByNameLike(String name, String sortColumn) {
        return null;
    }

    @Override
    public long searchCount(String keyword) {
        return userRepository.searchCount(keyword);
    }

    @Override
    public Iterable<User> search(String keyword, int page, int size, String sortColumn) {
        return null;
    }

    public String register(String telephone, String password, DeviceEnum device) {

        User user = userRepository.findByPhone(telephone);
        if (user != null) {
            throw new CustomException(messageSource.getMessage("first.name",null, Locale.getDefault()));
        }

        String salt = RadomUtils.getRadomStr();

        user = new User();
        user.setPhone(telephone);
        user.setAvatar("cb3cecad-fbbb-4171-b881-6ed5f281f235");
        user.setNickname(telephone.substring(telephone.length() - 4));
        this.save(user);

        String userId = user.getId();
        Auth auth = new Auth(userId, salt);
        auth.setPassword(CryptoUtils.cryptoPassword(password, salt));
        AuthToken token = new AuthToken(user, device);
        authServer.storeToken(token);
        auth.addToken(token);
        authRepository.save(auth);

       // imService.register(userId, user.getNickname(), user.getAvatar());
        return token.getToken();
    }
}
