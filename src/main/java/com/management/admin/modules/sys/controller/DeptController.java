package com.management.admin.modules.sys.controller;

import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.sys.entity.Dept;
import com.management.admin.modules.sys.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
            for(Dept dept:list){
                System.out.println(dept.getDept_name());
            }
            return retMsg.Set(MsgType.SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return retMsg.Set(MsgType.ERROR);
        }
    }
}
