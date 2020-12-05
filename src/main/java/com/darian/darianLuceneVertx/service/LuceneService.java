package com.darian.darianLuceneVertx.service;


import com.darian.darianLuceneVertx.domain.CustomerFile;
import com.darian.darianLuceneVertx.domain.ResponseT;
import com.darian.darianLuceneVertx.repository.CustomerFileRepository;
import com.darian.darianLuceneVertx.repository.LuceneRepository;
import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.ImgUtils;

import java.util.List;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/18  0:03
 */

public class LuceneService {


    public static void createIndex() {
        LuceneRepository.createIndex();
    }

    public static ResponseT doSearch(String param, boolean cache) {
        AssertUtils.assertNotBlank(param, "查询参数不能等于空");
        return doSearch(param);
    }

    private static ResponseT doSearch(String param) {
        AssertUtils.assertNotBlank(param, "查询参数不能等于空");

        List<CustomerFile> luceneResponseList = LuceneRepository.multiFiledQueryParser(param);

        for (CustomerFile luceneCustomerFile : luceneResponseList) {
            String filePathSubDocsFilePath = luceneCustomerFile.getFilePathSubDocsFilePath();

            // 查询内存仓库，补齐
            CustomerFile responseCustomerFile = CustomerFileRepository.findCopyCustomerFile(filePathSubDocsFilePath);
            luceneCustomerFile.setGiteeUrl(responseCustomerFile.getGiteeUrl());
            luceneCustomerFile.setGiteeRawUrl(responseCustomerFile.getGiteeRawUrl());
            luceneCustomerFile.setGitHubUrl(responseCustomerFile.getGitHubUrl());

            // 特殊处理，图片的 contentDetail ，把图片链接放进去
            if (ImgUtils.isImg(responseCustomerFile.getFileSimpleName())) {
                // 图片的话设置它的 图片链接放进去
                String imageShowUrl = responseCustomerFile.getImageShowUrl();
                luceneCustomerFile.setContentDetail("<img src=" + imageShowUrl + " >");
            }
        }

        String notifyMsg = generatorSearchNotifyMsg(luceneResponseList);

        return ResponseT.ok(notifyMsg, luceneResponseList);
    }

    private static String generatorSearchNotifyMsg(List<CustomerFile> luceneResponseList) {
        String notifyMsg;
        if (luceneResponseList.isEmpty()) {
            notifyMsg = "没有找到对应的文章!!! ";
        } else {
            notifyMsg = "本页显示[" + luceneResponseList.size() + "]条";
        }
        return notifyMsg;
    }
}
