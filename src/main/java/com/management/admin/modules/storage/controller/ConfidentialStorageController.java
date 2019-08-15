package com.management.admin.modules.storage.controller;

import com.management.admin.common.persistence.Page;
import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.storage.entity.ConfidentialStorage;
import com.management.admin.modules.storage.service.ConfidentialStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zch
 * @Description 涉密存储
 * @date 2019/8/12 20:39
 */
@RequestMapping("/api/sys/storage/confidential")
@Controller
public class ConfidentialStorageController extends BaseApi {
    @Autowired
    private ConfidentialStorageService confidentialStorageService;

    @RequestMapping(value = "/getSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getSub(@RequestParam String param) throws Exception {
        if (param.equals("dept")) {
            return confidentialStorageService.getSubFromDept();
        } else {
            return confidentialStorageService.getSubFromDict(param);
        }
    }

    @RequestMapping(value = "/getDeptSub", method = RequestMethod.POST)
    @ResponseBody
    public Object getDeptSub(@RequestParam String id) throws Exception {
        return confidentialStorageService.getDeptSub(id);
    }

    @RequestMapping(value = "/insertOrUpdateStorage", method = RequestMethod.POST)
    @ResponseBody
    public Object insertStorage(@RequestBody ConfidentialStorage confidentialStorage) throws Exception {
        System.out.println(confidentialStorage);
        if (confidentialStorage.getId() == null || confidentialStorage.getId().equals("")) {
            if (confidentialStorageService.insertStorage(confidentialStorage))
                return retMsg.Set(MsgType.SUCCESS);
            else return retMsg.Set(MsgType.ERROR);
        } else {
            if (confidentialStorageService.updateStorage(confidentialStorage))
                return retMsg.Set(MsgType.SUCCESS);
            else return retMsg.Set(MsgType.ERROR);
        }
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public Object getList(@RequestBody ConfidentialStorage confidentialStorage) throws Exception {
        try {
            Page<ConfidentialStorage> page = new Page<>();
            page.setResultList(confidentialStorageService.selectDictListByPage(confidentialStorage));
            page.setTotal(confidentialStorageService.selectSearchCount(confidentialStorage));
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
            if (confidentialStorageService.deleteListByIds(list)) {
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
    public Object scrap(@RequestBody ConfidentialStorage confidentialStorage) throws Exception {
        if(confidentialStorageService.scrap(confidentialStorage))
            return retMsg.Set(MsgType.SUCCESS);
        return retMsg.Set(MsgType.ERROR);
    }
}
