package com.xecoder;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/2/7-21:25
 * Feeling.com.xecoder
 */

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FeelingApplication.class)
public class JWTTest {

    private static JWTSigner signer = new JWTSigner("secret");
    private static JWTVerifier verifier = new JWTVerifier("secret");
    @Test
    public void shouldSignStringOrURI3() throws Exception {
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("aud", "123123");
        claims.put("iss","fuller.li");
        claims.put("oooooo","1111111111111");
        String token = signer.sign(claims);
        //assertEquals("eyJ[0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmdWxsZXIubGkiLCJhdWQiOiIxMjMxMjMiLCJvb29vb28iOiIxMTExMTExMTExMTExIn0.K00sztVpajtv3rKBr-4uJzTnG2RHLeM0vkpI7RKBblg", token);
    }

    @Test
    public void shouldSignStringOrURI2() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJmdWxsZXIubGkiLCJhdWQiOiIxMjMxMjMiLCJjdXN0b20iOiJIaSJ9.ULgxPMZqNGhXflthFeWP3uu188-bymSJPPO8EmTY24A";

        Map<String, Object> claims = verifier.verify(token);
        claims.forEach((id, val) -> System.out.println(val));
    }
}
