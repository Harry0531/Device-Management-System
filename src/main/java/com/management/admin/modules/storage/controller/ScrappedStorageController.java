package com.management.admin.modules.storage.controller;

import com.management.admin.common.persistence.Page;
import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.storage.entity.ConfidentialStorage;
import com.management.admin.modules.storage.service.ScrappedStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 报废涉密计算机
 * @author zch
 * @date 2019/8/18 22:28
 */
@RequestMapping("/api/sys/storage/scrapped")
@Controller
public class ScrappedStorageController extends BaseApi {
    @Autowired
    private ScrappedStorageService scrappedStorageService;

    @RequestMapping(value = "/getSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getSub(@RequestParam String param) throws Exception {
        System.out.println(param);
        if (param.equals("dept")) {
            return scrappedStorageService.getSubFromDept();
        } else {
            return scrappedStorageService.getSubFromDict(param);
        }
    }

    @RequestMapping(value = "/getDeptSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getDeptSub(@RequestParam String id) throws Exception {
        return scrappedStorageService.getDeptSub(id);
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public Object getList(@RequestBody ConfidentialStorage confidentialStorage) throws Exception {
        try {
            Page<ConfidentialStorage> page = new Page<>();
            page.setResultList(scrappedStorageService.selectDictListByPage(confidentialStorage));
            page.setTotal(scrappedStorageService.selectSearchCount(confidentialStorage));
            return retMsg.Set(MsgType.SUCCESS, page);
        } catch (Exception e) {
            e.printStackTrace();
            return retMsg.Set(MsgType.ERROR);
        }
    }

    @RequestMapping(value = "/deleteListByIds", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteListByIds(@RequestBody List<ConfidentialStorage> list) throws Exception {
        try {
            if (scrappedStorageService.deleteListByIds(list)) {
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
        if(scrappedStorageService.scrap(id, scrapTime, remarks))
            return retMsg.Set(MsgType.SUCCESS);
        return retMsg.Set(MsgType.ERROR);
    }
}
