package com.management.admin.modules.sys.controller;

import com.management.admin.common.web.BaseApi;
import com.management.admin.common.web.MsgType;
import com.management.admin.modules.sys.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/sys/user")
public class UserController extends BaseApi {
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Object login(@RequestParam("data") String password) {
        if (userDao.queryPassword("admin").equals(password))
            return retMsg.Set(MsgType.SUCCESS);
        else return retMsg.Set(MsgType.ERROR);
    }

    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    @ResponseBody
    public Object changePassword(@RequestParam("data") String password) {
        if (userDao.changePassword("admin", password) == 1)
            return retMsg.Set(MsgType.SUCCESS);
        else return retMsg.Set(MsgType.ERROR);
    }
}


