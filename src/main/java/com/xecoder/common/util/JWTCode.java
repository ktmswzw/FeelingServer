package com.xecoder.common.util;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/2/8-14:51
 * Feeling.com.xecoder.common.util
 */
public interface JWTCode {
    String SECERT = "FEELING_ME007";
    String AUTHORIZATION_STR = "Authorization";
    JWTSigner SIGNER = new JWTSigner(SECERT);
    JWTVerifier VERIFIER = new JWTVerifier(SECERT);
}
