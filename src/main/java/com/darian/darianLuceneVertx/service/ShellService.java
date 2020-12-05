package com.darian.darianLuceneVertx.service;


import com.darian.darianLuceneVertx.utils.ShellUtils;

/***
 *
 *
 * @author <a href="mailto:1934849492@qq.com">Darian</a> 
 * @date 2020/4/18  0:06
 */
public class ShellService {

    public static String doRestart() {
       return ShellUtils.thisAppplicationReStartSh();
    }
}
