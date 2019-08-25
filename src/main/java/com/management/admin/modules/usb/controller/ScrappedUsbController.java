package com.management.admin.modules.usb.controller;

import com.management.admin.common.persistence.Page;
import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.usb.entity.Usb;
import com.management.admin.modules.usb.service.ScrappedUsbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/sys/usb/scrapped")
@Controller
public class ScrappedUsbController extends BaseApi {
    @Autowired
    private ScrappedUsbService scrappedUsbService;

    @RequestMapping(value = "/getSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getSub(@RequestParam String param) throws Exception {
        System.out.println(param);
        if (param.equals("dept")) {
            return scrappedUsbService.getSubFromDept();
        } else {
            return scrappedUsbService.getSubFromDict(param);
        }
    }

    @RequestMapping(value = "/getDeptSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getDeptSub(@RequestParam String id) throws Exception {
        return scrappedUsbService.getDeptSub(id);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public Object getList(@RequestBody Usb usb) throws Exception {
        try {
            Page<Usb> page = new Page<>();
            page.setResultList(scrappedUsbService.selectDictListByPage(usb));
            page.setTotal(scrappedUsbService.selectSearchCount(usb));
            return retMsg.Set(MsgType.SUCCESS, page);
        } catch (Exception e) {
            e.printStackTrace();
            return retMsg.Set(MsgType.ERROR);
        }
    }

    @RequestMapping(value = "/scrap", method = RequestMethod.POST)
    @ResponseBody
    public Object scrap(@RequestBody Usb usb) throws Exception {
        if(scrappedUsbService.scrap(usb))
            return retMsg.Set(MsgType.SUCCESS);
        return retMsg.Set(MsgType.ERROR);
    }
}
