package com.darian.darianLuceneVertx.domain;


import com.darian.darianLuceneVertx.contants.DocumentContants;
import com.darian.darianLuceneVertx.exception.CustomerRuntimeException;
import com.darian.darianLuceneVertx.other.RegularExpressionsUtils;
import com.darian.darianLuceneVertx.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/14  0:26
 */
public class ConvertUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertUtils.class);

    private static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        if (uuid.length() > 32) {
            return uuid.substring(0, 31);
        }
        return uuid;
    }

    public static String getImgContextString(CustomerFile customerFile) {
        String name = customerFile.getFileSimpleName();
        String type = name.substring(name.lastIndexOf(".") + 1, name.length());
        return "![" + customerFile.getFileSimpleName()
                + "](data:image/" + type + ";base64," + customerFile.getContentDetail() + ")";
    }

    public static String getImgContextStringData(CustomerFile customerFile) {
        String name = customerFile.getFileSimpleName();
        String type = name.substring(name.lastIndexOf(".") + 1, name.length());
        ;
        return "data:image/" + type + ";base64," + customerFile.getContentDetail();
    }


    /**
     * 添加内容之前做预处理
     *
     * @param itemFile
     * @return
     */
    public static String doInsertContextIndexPre(File itemFile) {
        String filePath = itemFile.getPath();
        try {
            String fileContent = null;
            if (filePath.endsWith(".bat")) {
                // 保存的时候，需要转化为 ASCII 码，在CMD 运行时，才不会乱码 bat 文件用 GBK 读取，
                fileContent = FileUtils.readFileToString(itemFile, DocumentContants.GBK);
            } else if (filePath.endsWith(".md")
                    || filePath.endsWith(".java")
                    || filePath.endsWith(".sh")) {
                fileContent = RegularExpressionsUtils.doInsertContextIndexPre(itemFile, DocumentContants.UTF_8);
            } else {
                fileContent = "";
            }

            return fileContent;
        } catch (IOException e) {
            LOGGER.error("内容处理发生错误：", e);
            throw new CustomerRuntimeException("内容处理发生错误：", e);
        }
    }
}
