package com.management.admin.modules.computer.Controller;

import com.management.admin.common.persistence.Page;
import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.computer.Entity.ConfidentialComputer;
import com.management.admin.modules.computer.Service.ScrappedComputerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

 /**
      * @Description 报废计算机
      * @author wh
      * @date 2019/8/21 11:46
      */
@RequestMapping("/api/computer/scrapped")
@Controller
public class ScrappedComputerController extends BaseApi {
    @Autowired
    private ScrappedComputerService scrappedComputerService;


    @RequestMapping(value = "/getSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getSub(@RequestParam String param) throws Exception {
        if (param.equals("dept")) {
            return scrappedComputerService.getSubFromDept();
        } else {
            return scrappedComputerService.getSubFromDict(param);
        }
    }

    @RequestMapping(value = "/getDeptSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getDeptSub(@RequestParam String id) throws Exception {
        return scrappedComputerService.getDeptSub(id);
    }

    @RequestMapping(value = "/insertOrUpdateComputer", method = RequestMethod.POST)
    @ResponseBody
    public Object insertStorage(@RequestBody ConfidentialComputer confidentialComputer) throws Exception {
        System.out.println(confidentialComputer);
        if (confidentialComputer.getId() == null || confidentialComputer.getId().equals("")) {
            if (scrappedComputerService.insertComputer(confidentialComputer))
                return retMsg.Set(MsgType.SUCCESS);
            else return retMsg.Set(MsgType.ERROR);
        } else {
            if (scrappedComputerService.updateComputer(confidentialComputer))
                return retMsg.Set(MsgType.SUCCESS);
            else return retMsg.Set(MsgType.ERROR);
        }
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public Object getList(@RequestBody ConfidentialComputer confidentialComputer) throws Exception {
        try {
            Page<ConfidentialComputer> page = new Page<>();
            page.setResultList(scrappedComputerService.selectDictListByPage(confidentialComputer));
            page.setTotal(scrappedComputerService.selectSearchCount(confidentialComputer));
            return retMsg.Set(MsgType.SUCCESS, page);
        } catch (Exception e) {
            e.printStackTrace();
            return retMsg.Set(MsgType.ERROR);
        }
    }

    @RequestMapping(value = "/deleteListByIds", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteListByIds(@RequestBody List<ConfidentialComputer> list) throws Exception {
        try {
            if (scrappedComputerService.deleteListByIds(list)) {
                return retMsg.Set(MsgType.SUCCESS);
            } else
                return retMsg.Set(MsgType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return retMsg.Set(MsgType.ERROR);
        }
    }

     @RequestMapping(value = "/scrap", method = RequestMethod.POST)
     @ResponseBody
     public Object scrap(@RequestParam("id") String id,
                         @RequestParam("scrap_time") String scrapTime,
                         @RequestParam("remarks") String remarks
     ) throws Exception {
         if(scrappedComputerService.scrap(id, scrapTime,remarks))
             return retMsg.Set(MsgType.SUCCESS);
         return retMsg.Set(MsgType.ERROR);
     }
}
