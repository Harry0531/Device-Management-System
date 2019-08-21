package com.management.admin.modules.computer.Controller;

import com.management.admin.common.persistence.Page;
import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;

import com.management.admin.modules.computer.Entity.NoneConfidentialComputer;
import com.management.admin.modules.computer.Service.NoneConfidentialComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
      * @Description  非涉密计算机
      * @author wh
      * @date 2019/8/21 10:19
      */
@RequestMapping("/api/computer/noneConfidential")
@Controller
public class NoneConfidentialComputerController extends BaseApi {

        @Autowired
        private NoneConfidentialComputerService noneConfidentialComputerService;


        @RequestMapping(value = "/getSub", method = RequestMethod.POST)
        @ResponseBody
        public Object getSub(@RequestParam String param) throws Exception {
            if (param.equals("dept")) {
                return noneConfidentialComputerService.getSubFromDept();
            } else {
                return noneConfidentialComputerService.getSubFromDict(param);
            }
        }

        @RequestMapping(value = "/getDeptSub", method = RequestMethod.POST)
        @ResponseBody
        public Object getDeptSub(@RequestParam String id) throws Exception {
            return noneConfidentialComputerService.getDeptSub(id);
        }

        @RequestMapping(value = "/insertOrUpdateComputer", method = RequestMethod.POST)
        @ResponseBody
        public Object insertStorage(@RequestBody NoneConfidentialComputer noneConfidentialComputer) throws Exception {
            System.out.println(noneConfidentialComputer);
            if (noneConfidentialComputer.getId() == null || noneConfidentialComputer.getId().equals("")) {
                if (noneConfidentialComputerService.insertComputer(noneConfidentialComputer))
                    return retMsg.Set(MsgType.SUCCESS);
                else return retMsg.Set(MsgType.ERROR);
            } else {
                if (noneConfidentialComputerService.updateComputer(noneConfidentialComputer))
                    return retMsg.Set(MsgType.SUCCESS);
                else return retMsg.Set(MsgType.ERROR);
            }
        }

        @RequestMapping(value = "/getList", method = RequestMethod.POST)
        @ResponseBody
        public Object getList(@RequestBody NoneConfidentialComputer noneConfidentialComputer) throws Exception {
            try {
                Page<NoneConfidentialComputer> page = new Page<>();
                page.setResultList(noneConfidentialComputerService.selectDictListByPage(noneConfidentialComputer));
                page.setTotal(noneConfidentialComputerService.selectSearchCount(noneConfidentialComputer));
                return retMsg.Set(MsgType.SUCCESS, page);
            } catch (Exception e) {
                e.printStackTrace();
                return retMsg.Set(MsgType.ERROR);
            }
        }

        @RequestMapping(value = "/deleteListByIds", method = RequestMethod.POST)
        @ResponseBody
        public Object deleteListByIds(@RequestBody List<NoneConfidentialComputer> list) throws Exception {
            try {
                if (noneConfidentialComputerService.deleteListByIds(list)) {
                    return retMsg.Set(MsgType.SUCCESS);
                } else
                    return retMsg.Set(MsgType.ERROR);
            } catch (Exception e) {
                e.printStackTrace();
                return retMsg.Set(MsgType.ERROR);
            }
        }
}

