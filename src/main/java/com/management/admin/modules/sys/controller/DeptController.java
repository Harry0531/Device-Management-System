package com.management.admin.modules.sys.controller;

import com.management.admin.common.persistence.Page;
import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.sys.entity.Dept;
import com.management.admin.modules.sys.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zch
 * @Description 单位管理
 * @date 2019/8/14 10:31
 */
@RequestMapping("/api/sys/dept")
@Controller
public class DeptController extends BaseApi {
    @Autowired
    private DeptService deptService;

    @RequestMapping(value = "/getSchoolList", method = RequestMethod.GET)
    @ResponseBody
    public Object getSchoolList() throws Exception {
        try {
            List<Dept> list = deptService.getSchoolList();
            return retMsg.Set(MsgType.SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return retMsg.Set(MsgType.ERROR);
        }
    }

    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    @ResponseBody
    public Object getList(@RequestBody Dept dept) throws Exception {
        try {
            Page<Dept> page = new Page<>();
            page.setResultList(deptService.selectListByPage(dept));
            page.setTotal(deptService.selectSearchCount(dept));
            return retMsg.Set(MsgType.SUCCESS, page);
        } catch (Exception e) {
            e.printStackTrace();
            return retMsg.Set(MsgType.ERROR);
        }
    }

    @RequestMapping(value = "/insertOrUpdateDept", method = RequestMethod.POST)
    @ResponseBody
    public Object insertOrUpdateDept(@RequestBody Dept dept) throws Exception {
        System.out.println(dept.toString());
        if (dept.getId().equals("") || dept.getId() == null) {
            if (deptService.searchEntry(dept)) {
                if (deptService.insertDept(dept)) {
                    return retMsg.Set(MsgType.SUCCESS);
                } else return retMsg.Set(MsgType.ERROR, "，请稍后重试");
            } else return retMsg.Set(MsgType.ERROR, "，请检查单位代码");
        } else {
            if (deptService.searchEntry(dept)) {
                if (deptService.updateDept(dept))
                    return retMsg.Set(MsgType.SUCCESS);
                else return retMsg.Set(MsgType.ERROR, "，请稍后重试");
            } else return retMsg.Set(MsgType.ERROR, "，请检查单位代码");
        }
    }

    @RequestMapping(value = "/deleteListByIds", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteListByIds(@RequestBody List<Dept> list) throws Exception {
        try {
            if (deptService.deleteListByIds(list)) {
                return retMsg.Set(MsgType.SUCCESS);
            } else
                return retMsg.Set(MsgType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return retMsg.Set(MsgType.ERROR);
        }
    }

    @RequestMapping(value = "/changeDisable", method = RequestMethod.POST)
    @ResponseBody
    public Object changeDisable(
            @RequestParam String id,
            @RequestParam Integer flag
    ) throws Exception {
        if (deptService.changeDisable(id, flag))
            return retMsg.Set(MsgType.SUCCESS);
        else return retMsg.Set(MsgType.ERROR);
    }
}
