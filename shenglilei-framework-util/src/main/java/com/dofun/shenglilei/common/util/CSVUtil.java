package com.dofun.shenglilei.common.util;

import com.csvreader.CsvReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 读写CSV文件
 * <p>
 * 10万行，1秒内读完
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2019/3/15
 * Time:18:47
 */
@Slf4j
public class CSVUtil implements AutoCloseable {
    private InputStream inputStream;
    private CsvReader reader;
    private Charset charset = Charset.forName("gbk");

    public CSVUtil(InputStream inputStream) {
        this.inputStream = inputStream;
        this.reader = new CsvReader(inputStream, charset);
    }

    public CSVUtil(InputStream inputStream, Charset charset) {
        this.inputStream = inputStream;
        this.charset = charset;
        this.reader = new CsvReader(inputStream, charset);
    }

    /**
     * 读取CSV的所有行
     * <p>
     * 最多读取前20万行，避免OOM
     */
    public static List<List<String>> readAllLine(InputStream inputStream) throws Exception {
        long s = Instant.now().toEpochMilli();
        List<List<String>> list = new ArrayList<>();
        final long maxLine = 200000L;
        try (CSVUtil util = new CSVUtil(inputStream)) {
//            util.readHead();
            //开始读取数据行
            List<String> rowValues;
            int count = 0;
            while ((rowValues = util.readNextLine()) != null) {
                if (count <= maxLine) {
                    list.add(rowValues);
                } else {
                    log.info("总行数超过 " + maxLine + ",skiped");
                    break;
                }
                count++;
            }
            log.info("总时间：" + (Instant.now().toEpochMilli() - s) + "ms");
        }
        return list;
    }

    public static void testRead(MultipartFile file) {
        long s = Instant.now().toEpochMilli();
        log.info("begin");
        try (CSVUtil util = new CSVUtil(file.getInputStream())) {
            log.info("表头：" + util.readHead().toString());
            //开始读取数据行
            List<String> rowValues;
            int count = 0;
            while ((rowValues = util.readNextLine()) != null) {
                if (count < 100) {
                    //打印前100行，循环打印日志也比较耗性能
                    log.info(rowValues.toString());
                }
                count++;
            }
            log.info("总行数：" + count + ",时间：" + (Instant.now().toEpochMilli() - s));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 读取CSV的第一行表头
     * <p>
     * 读取失败返回null
     * <p>
     * 没有数据返回空列表
     */
    public List<String> readHead() throws IOException {
        if (!this.reader.readHeaders()) {
            log.error("readHead is failed.");
            return null;
        }
        return Arrays.asList(this.reader.getHeaders());
    }

    /**
     * 读取CSV的下一行
     * <p>
     * 跳过第1行表头
     * <p>
     * 读取失败返回null
     * <p>
     * 没有数据返回空列表
     */
    public List<String> readNextLine() throws IOException {
        if (!this.reader.readRecord()) {
            //如果是最后一行报这个错，就不需要处理
            log.error("readRecord is failed,may be end of file.");
            return null;
        }
        int count = this.reader.getColumnCount();
        if (count <= 0) {
            log.warn("count is " + count);
            return new ArrayList<>();
        }
        return Arrays.asList(this.reader.getValues());
    }

    @Override
    public void close() throws Exception {
        reader.close();
        inputStream.close();
    }
}