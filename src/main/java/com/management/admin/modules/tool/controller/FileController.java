//package com.management.admin.modules.tool.controller;
//
//import com.management.admin.common.utils.FileUtils;
//import com.management.admin.common.utils.SystemPath;
//import com.management.admin.common.web.BaseApi;
//import com.management.admin.common.web.MsgType;
//import com.management.admin.modules.tool.service.ExcelService;
//import com.sun.org.apache.xpath.internal.operations.Mult;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
///**
//      * @Description 臨時文件上傳
//      * @author wh
//      * @date 2019/8/11 20:18
//      */
//@Controller
//@RequestMapping("/api/tool/file")
//public class FileController extends BaseApi {
//
//    @RequestMapping(value ="uploadFile",method = RequestMethod.POST)
//    @ResponseBody
//    public Object uploadTemFile(
//            @RequestParam MultipartFile file,
//            HttpServletRequest request
//            )throws  Exception{
//       try{
//            String newName = FileUtils.saveUploadFileToTempDir(file);
//            return retMsg.Set(MsgType.SUCCESS,newName);
//       }catch (IOException e){
//           return retMsg.Set(MsgType.ERROR);
//       }
//    }
//
//}
