package com.management.admin.modules.computer.Controller;

import com.management.admin.common.persistence.Page;
import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.computer.Entity.ConfidentialComputer;
import com.management.admin.modules.computer.Service.ConfidentialComputerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

 /**

      * @Description 涉密计算机
      * @author wh
      * @date 2019/8/18 15:57
      */
@RequestMapping("/api/computer/confidential")
@Controller
public class ConfidentiaComputerController extends BaseApi {
    @Autowired
    private ConfidentialComputerService confidentialComputerService;

    @RequestMapping(value = "/getSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getSub(@RequestParam String param) throws Exception {
        if (param.equals("dept")) {
            return confidentialComputerService.getSubFromDept();
        } else {
            return confidentialComputerService.getSubFromDict(param);
        }
    }

    @RequestMapping(value = "/getDeptSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getDeptSub(@RequestParam String id) throws Exception {
        return confidentialComputerService.getDeptSub(id);
    }

    @RequestMapping(value = "/insertOrUpdateComputer", method = RequestMethod.POST)
    @ResponseBody
    public Object insertStorage(@RequestBody ConfidentialComputer confidentialComputer) throws Exception {
        System.out.println(confidentialComputer);
        if (confidentialComputer.getId() == null || confidentialComputer.getId().equals("")) {
            if (confidentialComputerService.insertComputer(confidentialComputer))
                return retMsg.Set(MsgType.SUCCESS);
            else return retMsg.Set(MsgType.ERROR);
        } else {
            if (confidentialComputerService.updateComputer(confidentialComputer))
                return retMsg.Set(MsgType.SUCCESS);
            else return retMsg.Set(MsgType.ERROR);
        }
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public Object getList(@RequestBody ConfidentialComputer confidentialComputer) throws Exception {
        try {
            Page<ConfidentialComputer> page = new Page<>();
            page.setResultList(confidentialComputerService.selectDictListByPage(confidentialComputer));
            page.setTotal(confidentialComputerService.selectSearchCount(confidentialComputer));
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
            if (confidentialComputerService.deleteListByIds(list)) {
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
    public Object scrap(@RequestBody ConfidentialComputer confidentialComputer) throws Exception {
        if(confidentialComputerService.scrap(confidentialComputer))
            return retMsg.Set(MsgType.SUCCESS);
        return retMsg.Set(MsgType.ERROR);
    }
}
