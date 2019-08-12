package com.management.admin.common.utils;

/**
 * 存放各种系统路径
 */
public class SystemPath {

    /**
     * @return 项目根路径
     */
    public static String getRootPath() {
        String root = Thread.currentThread().getContextClassLoader().getResource("").toString();
        return root.substring(6, root.length() - 16);
    }

    /**
     * @return 臨時文件路徑
     */
    public static String getTemporaryPath(){
        return "/src/main/webapp/file/template/";
    }

    /**
     * @return excel导入模板存储路径
     */
    public static String getExcelTemplatePath(){
        return "/src/main/webapp/file/template/";
    }
}
