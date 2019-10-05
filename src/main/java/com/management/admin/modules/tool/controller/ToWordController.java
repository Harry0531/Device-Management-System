package com.management.admin.modules.tool.controller;

import com.management.admin.common.web.BaseApi;
import com.management.admin.modules.tool.service.BackUpService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("api/tool/toword")
public class ToWordController extends BaseApi {

    @Autowired
    BackUpService backUpService;

    @ResponseBody
    @RequestMapping("toword")
    public void toword(
            HttpServletResponse response,
            @RequestParam String department
    ){
        try {
            List<Map<String,String>> newlist = new ArrayList<>();
//            newlist.add(map1);
//            newlist.add(map2);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("confidentialComputerList", newlist);

            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            //指定模板路径的第二种方式,我的路径是D:/      还有其他方式
            configuration.setDirectoryForTemplateLoading(new File("D:/"));

            // 输出文档路径及名称
            File outFile = new File("D:/test.doc");
            //以utf-8的编码读取ftl文件
            Template t =  configuration.getTemplate("模板.ftl","utf-8");
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"),10240);
            t.process(dataMap, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
