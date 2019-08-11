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
     * @return excel导入模板存储路径
     */
    public static String getExcelTemplatePath(){
        return "/src/main/webapp/template";
    }
}
