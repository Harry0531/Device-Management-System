package com.management.admin.common.utils;

import java.io.File;

/**
 * 存放各种系统路径
 */
public class SystemPath {


    /**
     * @return 项目根路径
     */
    public static String getRootPath() {
        String root = Thread.currentThread().getContextClassLoader().getResource("").toString();
        String substring = root.substring(6, root.length() - 16);
        System.out.println("路径获取："+substring);
//        substring = java.net.URLDecoder.decode(substring, "UTF-8");
        int firstIndex = substring.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = substring.lastIndexOf(File.separator) + 1;
        substring = substring.substring(firstIndex, lastIndex);
        System.out.println("jar包所在路径："+substring);
        return substring;
    }

    /**
     * @return 臨時文件路徑
     */
    public static String getTemporaryPath(){
        return "/templateFile/";
       // return "/src/main/webapp/file/template/";
    }

    /**
     * @return excel导入模板存储路径
     */
    public static String getExcelTemplatePath(){
        return "/temporaryFile/";
      // return "/src/main/webapp/file/temporary/";
    }

}
