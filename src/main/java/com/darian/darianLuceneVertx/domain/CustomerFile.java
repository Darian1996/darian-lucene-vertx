package com.darian.darianLuceneVertx.domain;


import lombok.Data;


/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a>
 * @date 2020/4/12  21:39
 */
@Data
public class CustomerFile {
    // 文件姓名
    private String fileSimpleName;
    // 文件路路径
    private String fileFullName;

    // 文件内容
    private String contentDetail;

    // 文件大小
    private String fileSize;
    // 计算生成
    private String gitHubUrl;
    // 计算生成
    private String giteeUrl;

    /**
     * gitee 图片 CDN 加速地址
     */
    private String giteeRawUrl;

    private String filePathSubDocsFilePath;

    /**
     * 设置 ownImgUrl
     */
    private String ownImgUrl;

    public void setFilePathSubDocsFilePath(String filePathSubDocsFilePath) {
        this.filePathSubDocsFilePath = filePathSubDocsFilePath;
        this.ownImgUrl = "/getImage?cache=true&filePathSubDocsFilePath=" + this.filePathSubDocsFilePath;
    }

    public String getImageShowUrl() {
        return this.ownImgUrl;
    }
}
