
package com.darian.darianLuceneVertx.repository;


import com.darian.darianLuceneVertx.domain.CustomerFile;
import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomerFileRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerFileRepository.class);


    private static final Map<String, CustomerFile> CUSTOMER_FILE_MAP = new HashMap<>();

    private static boolean INIT_FINISHED = false;

    /**
     * 标记 已经初始化完成
     */
    public static void setInitFinshFinshed() {
        INIT_FINISHED = true;
    }

    /**
     * 获取所有文件的 MAP 集合的 Key
     *
     * @return
     */
    public static List<String> getAllCustomerFileKeyList() {
        return new ArrayList<>(CUSTOMER_FILE_MAP.keySet());
    }

    /**
     * 根据 Key 返回一个 克隆对象
     *
     * @param filePathSubGitFilePath
     * @return
     */
    public static CustomerFile findCopyCustomerFile(String filePathSubGitFilePath) {
        LOGGER.debug("[findCopyCustomerFile]: " + filePathSubGitFilePath);
        CustomerFile responseCustomerFile = new CustomerFile();
        CustomerFile customerFile = CUSTOMER_FILE_MAP.get(filePathSubGitFilePath);
        if (customerFile == null) {
            LOGGER.warn("content[" + filePathSubGitFilePath + "] 找不到");
            return null;
        }

        BeanUtils.copyProperties(customerFile, responseCustomerFile);
        return responseCustomerFile;
    }


    /**
     * 添加一个 CustomerFile
     *
     * @param filePathSubGitFilePath
     * @param customerFile
     */
    public static void insertCustomerFile(String filePathSubGitFilePath, CustomerFile customerFile) {
        AssertUtils.assertFalse(INIT_FINISHED, "CustomerFileRepository is initFinished ... ");
        // 避免多线程用户使用这个方法，但是，单线程访问具有偏向锁，性能忽略不计
        synchronized (CUSTOMER_FILE_MAP) {
            CustomerFile put = CUSTOMER_FILE_MAP.put(filePathSubGitFilePath, customerFile);
            if (Objects.nonNull(put)) {
                LOGGER.error("有同路径的文件....");
                LOGGER.error("[putCustomerFile]--->>[filePathSubGitFilePath]", filePathSubGitFilePath, "[customerFileModule]", customerFile);
            }
        }
        return;
    }


}