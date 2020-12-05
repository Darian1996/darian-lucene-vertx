package com.darian.darianLuceneVertx.utils;

import com.darian.darianLuceneVertx.domain.CustomerFile;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/11/14  23:17
 */
public class BeanUtils {

    public static void copyProperties(Object source, Object target) {

    }

    public static void copyProperties(CustomerFile customerFile, CustomerFile responseCustomerFile) {
        responseCustomerFile.setFileSimpleName(customerFile.getFileSimpleName());
        responseCustomerFile.setFileFullName(customerFile.getFileFullName());
        responseCustomerFile.setContentDetail(customerFile.getContentDetail());
        responseCustomerFile.setFileSize(customerFile.getFileSize());
        responseCustomerFile.setGitHubUrl(customerFile.getGitHubUrl());
        responseCustomerFile.setGiteeUrl(customerFile.getGiteeUrl());
        responseCustomerFile.setGiteeRawUrl(customerFile.getGiteeRawUrl());
        responseCustomerFile.setFilePathSubDocsFilePath(customerFile.getFilePathSubDocsFilePath());
        responseCustomerFile.setOwnImgUrl(customerFile.getOwnImgUrl());

    }


}
