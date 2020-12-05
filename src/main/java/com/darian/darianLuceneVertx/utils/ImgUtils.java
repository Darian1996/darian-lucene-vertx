package com.darian.darianLuceneVertx.utils;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/17  23:04
 */
public class ImgUtils {
    /**
     * isImg -> true
     * notImg -> false
     *
     * @param fileName
     * @return
     */
    public static boolean isImg(String fileName) {
        return fileName.endsWith(".jpg")
                || fileName.endsWith(".JPG")
                || fileName.endsWith(".png")
                || fileName.endsWith(".svg")
                || fileName.endsWith(".JPEG")
                || fileName.endsWith(".gif");
    }
}
