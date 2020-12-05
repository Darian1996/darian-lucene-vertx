package com.darian.darianLuceneVertx.config;


import com.darian.darianLuceneVertx.contants.DocumentContants;
import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.properties.SystemAndConf;
import io.vertx.core.AbstractVerticle;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Objects;
import java.util.regex.Pattern;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  22:06
 */
@Data
public final class DarianLuceneConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DarianLuceneConfig.class);

    public static void init(AbstractVerticle abstractVerticle) {
        String pre = "darian.lucene.";
        ramOpen = SystemAndConf.getBoolean(abstractVerticle, pre + "ramOpen");
        useIK = SystemAndConf.getBoolean(abstractVerticle, pre + "useIK");
        contentMaxSize = SystemAndConf.getInteger(abstractVerticle, pre + "contentMaxSize");
        docsFilePath = SystemAndConf.getString(abstractVerticle, pre + "docsFilePath");
        patternString = SystemAndConf.getString(abstractVerticle, pre + "patternString");
        resultMaxSize = SystemAndConf.getInteger(abstractVerticle, pre + "resultMaxSize");
        redHighLightBegin = SystemAndConf.getString(abstractVerticle, pre + "redHighLightBegin");
        redHighLightEnd = SystemAndConf.getString(abstractVerticle, pre + "redHighLightEnd");
    }

    /**
     * 是否使用内存分词器, true , 使用
     */
    public static Boolean ramOpen = true;

    /**
     * 是否使用 IK 分词器
     */
    public static Boolean useIK = true;

    /**
     * 文章最长取多少长度
     */
    public static int contentMaxSize = 800;

    /**
     * 原始文档的路径
     */
    public static String docsFilePath = isWindows() ? "D:\\GitHub_Repositories\\docs" : "/software/docs";

    /**
     * 索引库创建的位置(默认的位置)
     */
    public static String directoryPath = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "main"
            + File.separator + "resources"
            + File.separator + "luncene_directory";

    /**
     * 索引的文件
     */
    public static String patternString = "\\\\*\\.bat"
            + "|\\\\*\\.md"
            + "|\\\\*\\.xmind"
            + "|\\\\*\\.jpg"
            + "|\\\\*\\.png"
            + "|\\\\*\\.JPEG"
            + "|\\\\*\\.svg"
            + "|\\\\*\\.java"
            + "|\\\\*\\.gif"
            + "|\\\\*\\.sh"
            + "|\\\\*\\.JPG";

    /**
     * 正则匹配编译后的 pattern
     */
    public static Pattern pattern;

    /**
     * 结果最多取多少个
     */
    public static Integer resultMaxSize = 30;

    public static String redHighLightBegin = "<b><font color='red'>";

    public static String redHighLightEnd = "</font></b>";


    public static void check() {
        AssertUtils.assertNotNull(ramOpen,
                "darian.lucene.config.ramOpen must not be null");
        AssertUtils.assertNotNull(useIK,
                "darian.lucene.config.useIK must not be null");
        AssertUtils.assertTrue(contentMaxSize != 0,
                "darian.lucene.config.contentMaxSize must not be 0");
        AssertUtils.assertNotBlank(docsFilePath,
                "darian.lucene.config.docsFilePath must not blank");
        AssertUtils.assertNotBlank(patternString,
                "darian.lucene.config.patternString must not blank");

        pattern = Pattern.compile(patternString);
        AssertUtils.assertTrue(Objects.nonNull(pattern), "darian.lucene.config.pattern must not null");
        DocumentContants.DOCS_FILE_PATH = docsFilePath;


    }

    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    }

    @Override
    public String toString() {
        return "\n" + "[DarianLuceneConfig]" + "\n" +
                "{" + "\n" +
                "\t" + "ramOpen = \"" + ramOpen + "\",\n " +
                "\t" + "useIK = \"" + useIK + "\",\n " +
                "\t" + "contentMaxSize = \"" + contentMaxSize + "\",\n " +
                "\t" + "docsFilePath = \"" + docsFilePath + "\",\n " +
                "\t" + "directoryPath = \"" + directoryPath + "\",\n " +
                "\t" + "patternString = \"" + patternString + "\",\n " +
                "\t" + "pattern = \"" + pattern + "\",\n " +
                "\t" + "resultMaxSize = \"" + resultMaxSize + "\",\n " +
                "\t" + "redHighLightBegin = \"" + redHighLightBegin + "\",\n " +
                "\t" + "redHighLightEnd = \"" + redHighLightEnd + "\",\n " +
                '}';
    }

}
