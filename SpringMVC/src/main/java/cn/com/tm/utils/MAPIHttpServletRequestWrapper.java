package cn.com.tm.utils;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MAPIHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body; // 报文

    public MAPIHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        cn.com.tm.utils.StreamUtils streamUtils = new cn.com.tm.utils.StreamUtils();
        body = streamUtils.readFromStream(request.getInputStream());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }
}