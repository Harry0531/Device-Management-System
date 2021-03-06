package com.management.admin.modules.infoDevice.controller;

import com.management.admin.common.persistence.Page;
import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.infoDevice.dao.InfoDeviceDao;
import com.management.admin.modules.infoDevice.entity.InfoDevice;
import com.management.admin.modules.infoDevice.service.ScrappedInfoDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/sys/info/scrapped")
@Controller
public class ScrappedInfoDeviceController extends BaseApi {
    @Autowired
    private ScrappedInfoDeviceService scrappedInfoDeviceService;

    @RequestMapping(value = "/getSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getSub(@RequestParam String param) throws Exception {
        System.out.println(param);
        if (param.equals("dept")) {
            return scrappedInfoDeviceService.getSubFromDept();
        } else {
            return scrappedInfoDeviceService.getSubFromDict(param);
        }
    }

    @RequestMapping(value = "/getDeptSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getDeptSub(@RequestParam String id) throws Exception {
        return scrappedInfoDeviceService.getDeptSub(id);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public Object getList(@RequestBody InfoDevice infoDevice) throws Exception {
        try {
            Page<InfoDevice> page = new Page<>();
            page.setResultList(scrappedInfoDeviceService.selectDictListByPage(infoDevice));
            page.setTotal(scrappedInfoDeviceService.selectSearchCount(infoDevice));
            return retMsg.Set(MsgType.SUCCESS, page);
        } catch (Exception e) {
            e.printStackTrace();
            return retMsg.Set(MsgType.ERROR);
        }
    }

    @RequestMapping(value = "/deleteListByIds", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteListByIds(@RequestBody List<InfoDevice> list) throws Exception {
        try {
            if (scrappedInfoDeviceService.deleteListByIds(list)) {
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
        if (scrappedInfoDeviceService.scrap(id, scrapTime, remarks))
            return retMsg.Set(MsgType.SUCCESS);
        return retMsg.Set(MsgType.ERROR);
    }

    @RequestMapping(value = "/insertOrUpdateInfo", method = RequestMethod.POST)
    @ResponseBody
    public Object insertStorage(@RequestBody InfoDevice infoDevice) throws Exception {
        if (infoDevice.getId() == null || infoDevice.getId().equals("")) {
            if (scrappedInfoDeviceService.insertInfo(infoDevice))
                return retMsg.Set(MsgType.SUCCESS);
            else return retMsg.Set(MsgType.ERROR);
        } else {
            return retMsg.Set(MsgType.ERROR);
        }
    }
}
