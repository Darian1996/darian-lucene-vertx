package com.darian.darianLuceneVertx.config;

import com.darian.darianLuceneVertx.utils.AssertUtils;
import com.darian.darianLuceneVertx.utils.StringUtils;
import com.darian.darianLuceneVertx.utils.properties.SystemAndConf;
import io.vertx.core.AbstractVerticle;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/13  2:34
 */
@Data
public class DarianGitRepConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(DarianGitRepConfig.class);

    public static void init(AbstractVerticle abstractVerticle) {
        String pre = "darian.my.git.config.";
        gitAuthorName = SystemAndConf.getString(abstractVerticle, pre + "gitAuthorName");
    }

    /**
     * Git 个人仓库对应的路径，（authorName）
     */
    public static String gitAuthorName = "Darian1996";

    /**
     * githubAuthorName
     */
    public static String githubAuthorName = null;

    /**
     * giteeAuthorName
     */
    public static String giteeAuthorName = null;

    public static String gitBranch = "master";

    /**
     * git 根路径，必存在 .git 文件
     */
    public static String gitPathHasFileName = ".git";

    public static String GITHUB_URL_PRE = null;

    public static String GITEE_URL_PRE = null;

    private static String DOCS_FILE_PATH = null;

    public static void check() {
       
        AssertUtils.assertNotBlank(gitAuthorName,
                "[darian.my.git.config.gitAuthorName] must not be Blank");

        githubAuthorName = StringUtils.hasText(githubAuthorName) ? githubAuthorName : gitAuthorName;
        giteeAuthorName = StringUtils.hasText(giteeAuthorName) ? giteeAuthorName : gitAuthorName;

        AssertUtils.assertNotBlank(githubAuthorName,
                "[darian.mygit.config.githubAuthorName] must not be Blank");
        AssertUtils.assertNotBlank(giteeAuthorName,
                "[darian.mygit.config.giteeAuthorName] must not be Blank");
        AssertUtils.assertNotBlank(gitBranch,
                "[darian.mygit.config.gitBranch] must not be Blank");
        AssertUtils.assertNotBlank(gitPathHasFileName,
                "[darian.mygit.config.gitPathHasFileName] must not be Blank");

        String docsFilePath = DarianLuceneConfig.docsFilePath;
        GITHUB_URL_PRE = initGitHubUrlPre(docsFilePath);
        GITEE_URL_PRE = initGiteeUrlPre(docsFilePath);

        DOCS_FILE_PATH = docsFilePath;

        AssertUtils.assertNotBlank(GITHUB_URL_PRE,
                "[darian.mygit.config.GITHUB_URL_PRE] must not be Blank");
        AssertUtils.assertNotBlank(GITEE_URL_PRE,
                "[darian.mygit.config.GITEE_URL_PRE] must not be Blank");


    }

    public static String getGitHubUrlFromFullName(String fileFullName) {
        String pathPreAndFile = fileFullName.substring(new File(DOCS_FILE_PATH).toString().length())
                .replaceAll("\\\\", "/").replaceAll(" ", "%20");

        if (StringUtils.hasText(GITHUB_URL_PRE)) {
            return GITHUB_URL_PRE + pathPreAndFile;
        }
        return "";
    }

    /**
     * 文件全路径 - 仓库路径作为文件的唯一Id
     * / 和 \ 全部转化为 '_'
     *
     * @param fileFullName
     * @return
     */
    public static String getFilePathSubDocsPath(String fileFullName) {
        return replaceTo_(fileFullName.substring(new File(DOCS_FILE_PATH).toString().length()));
    }

    public static String replaceTo_(String str) {
        return str.replaceAll("\\\\", "_")
                .replaceAll("/", "_")
                .replaceAll("\\+", "_")
                .replaceAll("&", "_")
                .replaceAll("\\[", "_")
                .replaceAll("]", "_")
                .replaceAll("\\{", "_")
                .replaceAll("}", "_")
                .replaceAll("<", "_")
                .replaceAll(">", "_")
                .replaceAll("＜", "_")
                .replaceAll("＞", "_")
                .replaceAll("「", "_")
                .replaceAll("」", "_")
                .replaceAll("：", "_")
                .replaceAll("；", "_")
                .replaceAll("、", "_")
                .replaceAll("•", "_")
                .replaceAll("\\^", "_")
                .replaceAll("'", "_")
                .replaceAll(" ", "_")
                ;
    }


    public static String getGiteeUrlFromFullName(String fileFullName) {
        String pathPreAndFile = fileFullName.substring(new File(DOCS_FILE_PATH).toString().length())
                .replaceAll("\\\\", "/").replaceAll(" ", "%20");

        if (StringUtils.hasText(GITEE_URL_PRE)) {
            return GITEE_URL_PRE + pathPreAndFile;
        }
        return "";

    }

    /**
     * 初始化仓库的前缀，整个仓库的前缀(GitHub)
     *
     * @param filePath
     * @return
     */
    private static String initGitHubUrlPre(String filePath) {
        String findGitRepositoryName = findGitRepositoryName(filePath);

        AssertUtils.assertNotBlank(findGitRepositoryName, "[" + filePath + "]下边没有找到Git仓库");

        String git_file_pre = filePath.substring(
                filePath.indexOf(File.separator + findGitRepositoryName) + 1 + findGitRepositoryName.length())
                .replaceAll("\\\\", "/");

        String github_repository_Url = "https://github.com/" + githubAuthorName + "/" + findGitRepositoryName + "/tree/" + gitBranch;
        return (github_repository_Url + git_file_pre)
                .replaceAll("\\\\", "/")
                .replaceAll(" ", "%20");
    }

    /**
     * 初始化仓库的前缀，整个仓库的前缀(Gitee)
     *
     * @param filePath
     * @return
     */
    private static String initGiteeUrlPre(String filePath) {
        String findGitRepositoryName = findGitRepositoryName(filePath);
        AssertUtils.assertNotBlank(findGitRepositoryName, "[" + filePath + "]下边没有找到Git仓库");

        String git_file_pre = filePath.substring(
                filePath.indexOf(File.separator + findGitRepositoryName) + 1 + findGitRepositoryName.length())
                .replaceAll("\\\\", "/");

        String gitee_repository_Url = "https://gitee.com/" + giteeAuthorName + "/" + findGitRepositoryName + "/tree/" + gitBranch;

        return (gitee_repository_Url + git_file_pre)
                .replaceAll("\\\\", "/")
                .replaceAll(" ", "%20");

    }

    private static String findGitRepositoryName(String filePath) {
        LOGGER.debug("寻找 Git 仓库[filePath]： " + filePath);
        String findGitRepositoryName = null;

        // windows 的 结束和 linux 的结束
        if (filePath.endsWith(":\\") || "/".equals(filePath)) {
            LOGGER.error("已经找到了盘符：" + filePath);
            return null;
        }
        File file = new File(filePath);
        AssertUtils.assertTrue(file.exists(), "[" + filePath + "]git 不仓库存在!!!");
        AssertUtils.assertTrue(file.listFiles().length > 0, "Git[" + filePath + "] 仓库下的文件 > 0");
        for (File fileItem : file.listFiles()) {
            if (fileItem.isDirectory()) {
                if (fileItem.getName().equals(gitPathHasFileName)) {
                    findGitRepositoryName = file.getName();
                    break;
                }
            }
        }
        if (findGitRepositoryName == null) {
            return findGitRepositoryName(file.getParent());
        }
        return findGitRepositoryName;
    }

    @Override
    public String toString() {
        return "\n" + "[DarianGitRepConfig]" +
                "\n" + "{" + "\n" +
                "\t" + "gitAuthorName = \"" + gitAuthorName + "\",\n " +
                "\t" + "githubAuthorName = \"" + githubAuthorName + "\",\n " +
                "\t" + "giteeAuthorName = \"" + giteeAuthorName + "\",\n " +
                "\t" + "gitBranch = \"" + gitBranch + "\",\n " +
                "\t" + "githubAuthorName = \"" + githubAuthorName + "\",\n " +
                "\t" + "GITHUB_URL_PRE = \"" + GITHUB_URL_PRE + "\",\n " +
                "\t" + "GITEE_URL_PRE = \"" + GITEE_URL_PRE + "\"\n " +
                '}';
    }
}
