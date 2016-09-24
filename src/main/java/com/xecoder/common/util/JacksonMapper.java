package com.xecoder.common.util;

/**
 * Created by vincent on 16/9/22.
 * Duser.name = 224911261@qq.com
 * Feeling
 */
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author xuanyin
 *
 */
public class JacksonMapper {

    /**
     *
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     *
     */
    private JacksonMapper() {

    }

    /**
     *
     * @return
     */
    public static ObjectMapper getInstance() {

        return mapper;
    }

}