package com.ky.logic.utils;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class JsonStringHttpMessageConverter extends AbstractHttpMessageConverter<String> {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private final Charset defaultCharset;

    public JsonStringHttpMessageConverter() {
        this(DEFAULT_CHARSET);
    }

    public JsonStringHttpMessageConverter(Charset defaultCharset) {
        super(new MediaType[]{new MediaType("application", "json", defaultCharset), MediaType.ALL});
        this.defaultCharset = defaultCharset;
    }

    public boolean supports(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage) throws IOException {
        Charset charset = this.getContentTypeCharset(inputMessage.getHeaders().getContentType());
        return StreamUtils.copyToString(inputMessage.getBody(), charset);
    }

    protected Long getContentLength(String s, MediaType contentType) {
        Charset charset = this.getContentTypeCharset(contentType);

        try {
            return Long.valueOf((long)s.getBytes(charset.name()).length);
        } catch (UnsupportedEncodingException var5) {
            throw new IllegalStateException(var5);
        }
    }

    protected void writeInternal(String s, HttpOutputMessage outputMessage) throws IOException {
        Charset charset = this.getContentTypeCharset(outputMessage.getHeaders().getContentType());
        StreamUtils.copy(s, charset, outputMessage.getBody());
    }

    private Charset getContentTypeCharset(MediaType contentType) {
        return contentType != null && contentType.getCharSet() != null?contentType.getCharSet():this.defaultCharset;
    }
}

