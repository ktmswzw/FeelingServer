package com.xecoder.interceptor;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by  moxz
 * User: 224911261@qq.com
 * 2016/1/11-13:21
 * Feeling.com.xecoder.model
 */
public class ContentResposeWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream content = new ByteArrayOutputStream();

    private final ServletOutputStream outputStream = new ResponseServletOutputStream();


    public ContentResposeWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return this.outputStream;
    }

    @Override
    public void resetBuffer() {
        this.content.reset();
    }

    @Override
    public void reset() {
        super.reset();
        this.content.reset();
    }

    public void closeContent() throws IOException {
        content.flush();
        content.close();
    }

    /**
     * Return the cached response content as a byte array.
     */
    public byte[] getContentAsByteArray() throws IOException {
        return this.content.toByteArray();
    }

    public void copyBodyToResponse() throws IOException {
        if (this.content.size() > 0) {
            getResponse().setContentLength(this.content.size());
            StreamUtils.copy(this.content.toByteArray(), getResponse().getOutputStream());
        }
    }


    private class ResponseServletOutputStream extends ServletOutputStream {

        @Override
        public void write(int b) throws IOException {
            content.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            content.write(b, off, len);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

    }
}
