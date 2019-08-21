package com.management.admin.modules.computer.Controller;

import com.management.admin.common.persistence.Page;
import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;

import com.management.admin.modules.computer.Entity.NoneConfidentialIntermediary;
import com.management.admin.modules.computer.Service.NoneConfidentialIntermediaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

     * @Description 非涉密中间机
     * @author wh
     * @date 2019/8/18 15:57
     */
@RequestMapping("/api/computer/intermediary")
@Controller
public class NoneConfidentialIntermediaryController extends BaseApi {
   @Autowired
   private NoneConfidentialIntermediaryService noneConfidentialIntermediaryService;

   @RequestMapping(value = "/getSub", method = RequestMethod.POST)
   @ResponseBody
   public Object getSub(@RequestParam String param) throws Exception {
       if (param.equals("dept")) {
           return noneConfidentialIntermediaryService.getSubFromDept();
       } else {
           return noneConfidentialIntermediaryService.getSubFromDict(param);
       }
   }

   @RequestMapping(value = "/getDeptSub", method = RequestMethod.POST)
   @ResponseBody
   public Object getDeptSub(@RequestParam String id) throws Exception {
       return noneConfidentialIntermediaryService.getDeptSub(id);
   }

   @RequestMapping(value = "/insertOrUpdateComputer", method = RequestMethod.POST)
   @ResponseBody
   public Object insertStorage(@RequestBody NoneConfidentialIntermediary noneConfidentialIntermediary) throws Exception {
       System.out.println(noneConfidentialIntermediary);
       if (noneConfidentialIntermediary.getId() == null || noneConfidentialIntermediary.getId().equals("")) {
           if (noneConfidentialIntermediaryService.insertComputer(noneConfidentialIntermediary))
               return retMsg.Set(MsgType.SUCCESS);
           else return retMsg.Set(MsgType.ERROR);
       } else {
           if (noneConfidentialIntermediaryService.updateComputer(noneConfidentialIntermediary))
               return retMsg.Set(MsgType.SUCCESS);
           else return retMsg.Set(MsgType.ERROR);
       }
   }

   @RequestMapping(value = "/getList", method = RequestMethod.POST)
   @ResponseBody
   public Object getList(@RequestBody NoneConfidentialIntermediary noneConfidentialIntermediary) throws Exception {
       try {
           Page<NoneConfidentialIntermediary> page = new Page<>();
           page.setResultList(noneConfidentialIntermediaryService.selectDictListByPage(noneConfidentialIntermediary));
           page.setTotal(noneConfidentialIntermediaryService.selectSearchCount(noneConfidentialIntermediary));
           return retMsg.Set(MsgType.SUCCESS, page);
       } catch (Exception e) {
           e.printStackTrace();
           return retMsg.Set(MsgType.ERROR);
       }
   }

   @RequestMapping(value = "/deleteListByIds", method = RequestMethod.POST)
   @ResponseBody
   public Object deleteListByIds(@RequestBody List<NoneConfidentialIntermediary> list) throws Exception {
       try {
           if (noneConfidentialIntermediaryService.deleteListByIds(list)) {
               return retMsg.Set(MsgType.SUCCESS);
           } else
               return retMsg.Set(MsgType.ERROR);
       } catch (Exception e) {
           e.printStackTrace();
           return retMsg.Set(MsgType.ERROR);
       }
   }


}
