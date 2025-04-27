package com.dofun.shenglilei.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;

@Slf4j
public class FileUtils {

    /**
     * 将流转成File
     */
    public static void inputStreamToFile(InputStream in, File file) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            IOUtils.copy(in, out);
            in.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void byteToFile(byte[] b, File file) {

        try (FileOutputStream fout = new FileOutputStream(file);
             BufferedOutputStream bout = new BufferedOutputStream(fout)) {
            bout.write(b);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * 删除临时文件
     **/
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                boolean b = file.delete();
                if (!b) {
                    log.error("failed to delete, file:{}", file.getPath());
                }
            } else {
                log.error("file not exists");
            }
        }
    }

    /**
     * 将file转成byte
     */
    private byte[] fileToByte(File file) {

        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将流转成byte
     */
    private byte[] inputStreamToByte(InputStream is) {

        try (ByteArrayOutputStream bytestream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int ch;
            while ((ch = is.read(buffer)) != -1) {
                bytestream.write(buffer, 0, ch);
            }
            is.close();
            return bytestream.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
