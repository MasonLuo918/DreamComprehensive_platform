package com.Dream.service.abstract_class;

import com.Dream.dao.OauthDao;
import com.Dream.entity.Oauth;
import com.Dream.service.OauthService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractOauth implements OauthService {

    @Autowired
    protected OauthDao oauthDao;

    @Override
    public Oauth insert(Oauth oauth) {
        oauthDao.insert(oauth);
        return oauth;
    }

    @Override
    public Oauth update(Oauth oauth) {
        oauthDao.update(oauth);
        return oauth;
    }

    @Override
    public Oauth findByOpenID(String openID) {
        Oauth oauth = new Oauth();
        oauth.setOpenID(openID);
        return oauthDao.selectOne(oauth);
    }

    @Override
    public Oauth findByUserID(String userID) {
        Oauth oauth = new Oauth();
        oauth.setUserID(userID);
        return oauthDao.selectOne(oauth);
    }
}
