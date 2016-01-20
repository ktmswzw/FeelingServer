package com.xecoder.model.business;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document(collection = "auth_db")
public class Auth {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * auth的拥有者id
     */
    private String owner;

    /**
     * 密码salt+hash
     */
    private String password;

    private String salt;

    /**
     * 有效令牌
     */
    private List<AuthToken> tokens = new ArrayList<>();

    private HashMap<String, String> platform = new HashMap<>();

    public enum ThirdPlatform {
        weixin, qq, weibo, qqWeb
    }

    public Auth(String owner, String salt) {
        this.owner = owner;
        this.salt = salt;
    }

    public Auth(String owner) {
        this.owner = owner;
    }

    public Auth() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AuthToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<AuthToken> tokens) {
        this.tokens = tokens;
    }

    public void addToken(AuthToken token) {
        this.tokens.add(token);
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<AuthToken> getEffectiveTokens() {
        if (tokens.isEmpty()) {
            return Collections.emptyList();
        }

        Iterator<AuthToken> itr = tokens.iterator();
        while (itr.hasNext()) {
            AuthToken token = itr.next();
            if (token.isExpired()) {
                itr.remove();
            }
        }
        return tokens;
    }

    public List<AuthToken> getEffectiveTokens(String token) {
        if (tokens.isEmpty()) {
            return Collections.emptyList();
        }

        Iterator<AuthToken> itr = tokens.iterator();
        while (itr.hasNext()) {
            AuthToken tk = itr.next();
            if (tk.isExpired() || tk.getToken().equals(token)) {
                itr.remove();
            }
        }
        return tokens;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void addPlatform(ThirdPlatform name, String id) {
        platform.put(name.name(), id);
    }

    public void removePlatform(ThirdPlatform name) {
        platform.remove(name.name());
    }

    public HashMap<String, String> getPlatform() {
        return platform;
    }

    public void setPlatform(HashMap<String, String> platform) {
        this.platform = platform;
    }
}
