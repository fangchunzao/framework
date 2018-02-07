package cn.com.tm.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Michael on 12/16 0016.
 */
public class StreamUtils {

    public byte[] readFromStream(InputStream in) {
        BufferedInputStream bufin = new BufferedInputStream(in);
        int buffSize = 1024;
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);
        byte[] temp = new byte[buffSize];

        try {
            int size = 0;
            while ((size = bufin.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            bufin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] content = out.toByteArray();

        return content;
    }
}
