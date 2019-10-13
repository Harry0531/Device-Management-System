package com.management.admin.modules.tool.controller;

import com.management.admin.common.persistence.Page;
import com.management.admin.common.web.BaseApi;
import com.management.admin.modules.computer.Entity.ConfidentialComputer;
import com.management.admin.modules.computer.Entity.NoneConfidentialComputer;
import com.management.admin.modules.computer.Entity.NoneConfidentialIntermediary;
import com.management.admin.modules.computer.Service.ConfidentialComputerService;
import com.management.admin.modules.computer.Service.NoneConfidentialComputerService;
import com.management.admin.modules.computer.Service.NoneConfidentialIntermediaryService;
import com.management.admin.modules.computer.Service.ScrappedComputerService;
import com.management.admin.modules.infoDevice.entity.InfoDevice;
import com.management.admin.modules.infoDevice.entity.NonConfidentialInfoDevice;
import com.management.admin.modules.infoDevice.service.InfoDeviceService;
import com.management.admin.modules.infoDevice.service.NonConfidentialInfoDeviceService;
import com.management.admin.modules.infoDevice.service.ScrappedInfoDeviceService;
import com.management.admin.modules.securityProduct.entity.SecurityProduct;
import com.management.admin.modules.securityProduct.service.ScrappedProductService;
import com.management.admin.modules.securityProduct.service.SecurityProductService;
import com.management.admin.modules.storage.entity.ConfidentialStorage;
import com.management.admin.modules.storage.entity.NonConfidentialStorage;
import com.management.admin.modules.storage.service.ConfidentialStorageService;
import com.management.admin.modules.storage.service.NonConfidentialStorageService;
import com.management.admin.modules.storage.service.ScrappedStorageService;
import com.management.admin.modules.sys.entity.Dept;
import com.management.admin.modules.tool.dao.ImportDataDao;
import com.management.admin.modules.tool.entity.tiny.DictInfo;
import com.management.admin.modules.tool.entity.tiny.PartInfo;
import com.management.admin.modules.tool.entity.toWordTools.ScrappedStatistics;
import com.management.admin.modules.tool.entity.toWordTools.SecretStatistics;
import com.management.admin.modules.tool.entity.toWordTools.noneSecretStatistics;
import com.management.admin.modules.tool.service.BackUpService;
import com.management.admin.modules.usb.entity.Usb;
import com.management.admin.modules.usb.service.ScrappedUsbService;
import com.management.admin.modules.usb.service.UsbService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("api/tool/toword")
public class ToWordController extends BaseApi {

    @Autowired
    private ConfidentialComputerService confidentialComputerService;

    @Autowired
    private NoneConfidentialIntermediaryService noneConfidentialIntermediaryService;

    @Autowired
    private NoneConfidentialComputerService noneConfidentialComputerService;

    @Autowired
    private ScrappedComputerService scrappedComputerService;

    @Autowired
    private InfoDeviceService infoDeviceService;

    @Autowired
    private NonConfidentialInfoDeviceService nonConfidentialInfoDeviceService;

    @Autowired
    private ScrappedInfoDeviceService scrappedInfoDeviceService;

    @Autowired
    private ConfidentialStorageService confidentialStorageService;

    @Autowired
    private NonConfidentialStorageService nonConfidentialStorageService;

    @Autowired
    private ScrappedStorageService scrappedStorageService;

    @Autowired
    private SecurityProductService securityProductService;

    @Autowired
    private ScrappedProductService scrappedProductService;

    @Autowired
    private ScrappedUsbService scrappedUsbService;

    @Autowired
    private UsbService usbService;

    private String dep;
    private String startDate;
    private String endDate;

    private noneSecretStatistics noneSecretStatisticsCounts;
    private ScrappedStatistics scrappedStatisticsCounts;
    private SecretStatistics secretStatistics;

    @ResponseBody
    @RequestMapping("toword")
    public void toword(
            HttpServletResponse response,
            @RequestParam String department,
            @RequestParam String depName,
            @RequestParam String model
    ){
        dep = department;
        switch (model) {
            case "secret":
                secret(depName, response);
                break;
            case "noneSecret":
                noneSecret(depName, response);
                break;
        }
        return;
    }

    @ResponseBody
    @RequestMapping("towordScrapped")
    public void towordScrapped(
            HttpServletResponse response,
            @RequestParam String department,
            @RequestParam String depName,
            @RequestParam String startTime,
            @RequestParam String endTime
    ){
        dep = department;
        startDate = startTime;
        endDate = endTime;
        scrapped(depName, response);
        return;
    }

    private void secret(String depName, HttpServletResponse response){
        try {
            Map<String, Object> dataMap = new HashMap<>();
            Template t;
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            configuration.setClassForTemplateLoading(this.getClass(), "/template");
            if(!dep.equals("")){
                dataMap = getSecretData(dep, depName);
                //以utf-8的编码读取ftl文件
                t =  configuration.getTemplate("secret.ftl","utf-8");
            } else {
                List<Dept> dept = infoDeviceService.getSubFromDept();
                List<Map<String,Object>> newlist = new ArrayList<>();
                for(Dept dep : dept){
                    newlist.add(getSecretData(dep.getId(), dep.getDept_name()));
                }
                dataMap.put("list", newlist);
                depName = "北京理工大学";
                String[] strNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().split("-");
                dataMap.put("year", strNow[0]);
                dataMap.put("month", strNow[1]);
                dataMap.put("day", strNow[2]);
                //以utf-8的编码读取ftl文件
                t =  configuration.getTemplate("secretAll.ftl","utf-8");
            }
            String fileName = URLEncoder.encode(depName + "涉密信息设备和存储设备台账", "UTF-8") + ".doc";
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            Writer out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "utf-8"),10240);
            t.process(dataMap, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private void noneSecret(String depName, HttpServletResponse response){
        try {
            Map<String, Object> dataMap = new HashMap<>();
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            configuration.setClassForTemplateLoading(this.getClass(), "/template");
            Template t;
            if(!dep.equals("")){
                dataMap = getNoneSecretData(dep, depName);
                t =  configuration.getTemplate("noneSecret.ftl","utf-8");
            } else {
                List<Dept> dept = infoDeviceService.getSubFromDept();
                List<Map<String,Object>> newlist = new ArrayList<>();
                for(Dept dep : dept){
                    newlist.add(getNoneSecretData(dep.getId(), dep.getDept_name()));
                }
                t =  configuration.getTemplate("noneSecretAll.ftl","utf-8");
                depName = "北京理工大学";
                String[] strNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().split("-");
                dataMap.put("year", strNow[0]);
                dataMap.put("month", strNow[1]);
                dataMap.put("day", strNow[2]);
                dataMap.put("list", newlist);
            }
            String fileName = URLEncoder.encode(depName + "非涉密信息设备和存储设备台账", "UTF-8") + ".doc";
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            Writer out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "utf-8"),10240);
            t.process(dataMap, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private void scrapped(String depName, HttpServletResponse response){
        try {
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            configuration.setClassForTemplateLoading(this.getClass(), "/template");
            Template t;
            Map<String, Object> dataMap = new HashMap<>();
            if(!dep.equals("")){
                dataMap = getScrappedData(dep, depName);
                t = configuration.getTemplate("scrapped.ftl","utf-8");
            } else {
                List<Dept> dept = infoDeviceService.getSubFromDept();
                List<Map<String,Object>> newlist = new ArrayList<>();
                for(Dept dep : dept){
                    newlist.add(getScrappedData(dep.getId(), dep.getDept_name()));
                }
                dataMap.put("list", newlist);
                String[] strNow = startDate.split("-");
                dataMap.put("syear", strNow[0]);
                dataMap.put("smonth", strNow[1]);
                dataMap.put("sday", strNow[2]);
                strNow = endDate.split("-");
                dataMap.put("eyear", strNow[0]);
                dataMap.put("emonth", strNow[1]);
                dataMap.put("eday", strNow[2]);
                t = configuration.getTemplate("scrappedAll.ftl","utf-8");
                depName = "北京理工大学";
            }
            //以utf-8的编码读取ftl文件
            String fileName = URLEncoder.encode(depName + "报废涉密信息设备和存储设备台账", "UTF-8") + ".doc";
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            Writer out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "utf-8"),10240);
            t.process(dataMap, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    private Map<String, Object> getSecretData(String depCode, String depName){
        Map<String, Object> dataMap = new HashMap<>();
        dep = depCode;
        secretStatistics = new SecretStatistics();
        dataMap.put("confidentialComputerList", getConfidentiaComputerList());
        dataMap.put("noneConfidentialIntermediaryList", getNoneConfidentialIntermediaryList());
        dataMap.put("infoDeviceList", getInfoDeviceList());
        dataMap.put("confidentialStorageList", getConfidentialStorageList());
        dataMap.put("securityProductList", getSecurityProductList());
        dataMap.put("usbList", getUSBList());
        dataMap.put("department", depName);
        String[] strNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().split("-");
        dataMap.put("year", strNow[0]);
        dataMap.put("month", strNow[1]);
        dataMap.put("day", strNow[2]);
        dataMap.put("statisticsInfo", getSecretStatistics());
        return dataMap;
    }

    private Map<String, Object> getNoneSecretData(String depCode, String depName){
        Map<String, Object> dataMap = new HashMap<>();
        noneSecretStatisticsCounts = new noneSecretStatistics();
        dep = depCode;
        dataMap.put("noneConfidentialComputerList", getNoneConfidentialComputerList());
        dataMap.put("noneConfidentialInfoDeviceList", getNonConfidentialInfoDeviceList());
        dataMap.put("noneConfidentialStorageList", getNonConfidentialStorageList());
        dataMap.put("department", depName);
        String[] strNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().split("-");
        dataMap.put("year", strNow[0]);
        dataMap.put("month", strNow[1]);
        dataMap.put("day", strNow[2]);
        dataMap.put("statisticsInfo", getNoneSecretStatistics());
        return dataMap;
    }

    private Map<String, Object> getScrappedData(String depCode, String depName){
        Map<String, Object> dataMap = new HashMap<>();
        scrappedStatisticsCounts = new ScrappedStatistics();
        dep = depCode;
        dataMap.put("scrappedComputerList", getScrappedComputerList());
        dataMap.put("scrappedInfoDeviceList", getScrappedInfoDeviceList());
        dataMap.put("scrappedStorageList", getScrappedStorageList());
        dataMap.put("scrappedProductList", getScrappedProductList());
        dataMap.put("scrappedUsbList", getScrappedUSBList());
        dataMap.put("department", depName);
        String[] strNow = startDate.split("-");
        dataMap.put("syear", strNow[0]);
        dataMap.put("smonth", strNow[1]);
        dataMap.put("sday", strNow[2]);
        strNow = endDate.split("-");
        dataMap.put("eyear", strNow[0]);
        dataMap.put("emonth", strNow[1]);
        dataMap.put("eday", strNow[2]);
        dataMap.put("statisticsInfo", getScrappedStatistics());
        return dataMap;
    }

    private Map<String, String> getSecretStatistics(){
        Map<String, String> map = new HashMap<>();
        map.put("laptop", String.valueOf(secretStatistics.laptop));
        map.put("desktop", String.valueOf(secretStatistics.desktop));
        map.put("allComputer", String.valueOf(secretStatistics.laptop + secretStatistics.desktop));
        map.put("printer", String.valueOf(secretStatistics.printer));
        map.put("copier", String.valueOf(secretStatistics.copier));
        map.put("otherInfoDevice", String.valueOf(secretStatistics.noneInfoDeviceOther));
        map.put("allInfoDevice", String.valueOf(secretStatistics.printer + secretStatistics.copier + secretStatistics.noneInfoDeviceOther));
        map.put("udisk", String.valueOf(secretStatistics.udisk));
        map.put("disk", String.valueOf(secretStatistics.disk));
        map.put("otherSecProduct", String.valueOf(secretStatistics.noneSecProOther));
        map.put("allSecProduct", String.valueOf(secretStatistics.udisk + secretStatistics.disk + secretStatistics.noneSecProOther));
        map.put("middle", String.valueOf(secretStatistics.middle));
        map.put("usb", String.valueOf(secretStatistics.usb));
        return map;
    }

    private Map<String, String> getNoneSecretStatistics(){
        Map<String, String> map = new HashMap<>();
        map.put("laptop", String.valueOf(noneSecretStatisticsCounts.laptop));
        map.put("desktop", String.valueOf(noneSecretStatisticsCounts.desktop));
        map.put("allComputer", String.valueOf(noneSecretStatisticsCounts.laptop + noneSecretStatisticsCounts.desktop));
        map.put("printer", String.valueOf(noneSecretStatisticsCounts.printer));
        map.put("copier", String.valueOf(noneSecretStatisticsCounts.copier));
        map.put("otherInfoDevice", String.valueOf(noneSecretStatisticsCounts.noneInfoDeviceOther));
        map.put("allInfoDevice", String.valueOf(noneSecretStatisticsCounts.printer + noneSecretStatisticsCounts.copier + noneSecretStatisticsCounts.noneInfoDeviceOther));
        map.put("udisk", String.valueOf(noneSecretStatisticsCounts.udisk));
        map.put("disk", String.valueOf(noneSecretStatisticsCounts.disk));
        map.put("otherSecProduct", String.valueOf(noneSecretStatisticsCounts.noneSecProOther));
        map.put("allSecProduct", String.valueOf(noneSecretStatisticsCounts.udisk + noneSecretStatisticsCounts.disk + noneSecretStatisticsCounts.noneSecProOther));
        return map;
    }

    private Map<String, String> getScrappedStatistics(){
        Map<String, String> map = new HashMap<>();
        map.put("laptop", String.valueOf(scrappedStatisticsCounts.laptop));
        map.put("desktop", String.valueOf(scrappedStatisticsCounts.desktop));
        map.put("allComputer", String.valueOf(scrappedStatisticsCounts.laptop + scrappedStatisticsCounts.desktop));
        map.put("printer", String.valueOf(scrappedStatisticsCounts.printer));
        map.put("copier", String.valueOf(scrappedStatisticsCounts.copier));
        map.put("otherInfoDevice", String.valueOf(scrappedStatisticsCounts.noneInfoDeviceOther));
        map.put("allInfoDevice", String.valueOf(scrappedStatisticsCounts.printer + scrappedStatisticsCounts.copier + scrappedStatisticsCounts.noneInfoDeviceOther));
        map.put("udisk", String.valueOf(scrappedStatisticsCounts.udisk));
        map.put("disk", String.valueOf(scrappedStatisticsCounts.disk));
        map.put("otherSecProduct", String.valueOf(scrappedStatisticsCounts.noneSecProOther));
        map.put("allSecProduct", String.valueOf(scrappedStatisticsCounts.udisk + scrappedStatisticsCounts.disk + scrappedStatisticsCounts.noneSecProOther));
        map.put("usb", String.valueOf(scrappedStatisticsCounts.usb));
        return map;
    }

    //涉密计算机获取数据
    private List<ConfidentialComputer> getAllConfidentiaComputerData(){
        ConfidentialComputer confidentialComputer = new ConfidentialComputer();
        Page<ConfidentialComputer> confidentialComputerPage = new Page<>();
        confidentialComputerPage.setPageIndex(1);
        confidentialComputerPage.setPageSize(99999);
        confidentialComputerPage.setPageStart(0);
        confidentialComputer.setPage(confidentialComputerPage);
        confidentialComputer.setDepartment_code(dep);
        List<ConfidentialComputer> list = confidentialComputerService.selectDictListByPage(confidentialComputer);
        return list;
    }
    //涉密计算机数据转换
    private List<Map<String,String>> getConfidentiaComputerList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<ConfidentialComputer> list = getAllConfidentiaComputerData();
        int i = 1;
        for(ConfidentialComputer computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("type", computer.get_type());
            map.put("secretNumber", computer.getSecret_number());
            map.put("assetNumber", computer.getAsset_number());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("model", computer.getModel());
            map.put("osVersion", computer.get_os_version());
            map.put("osInstallTime", computer.getOs_install_time());
            map.put("serialNumber", computer.getSerial_number());
            map.put("macAddress", computer.getMac_address());
            map.put("cdDrive", computer.get_cd_drive());
            map.put("usage", computer.get_usage());
            map.put("placeLocation", computer.getPlace_location());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            if(computer.get_type().equals("台式机")){
                secretStatistics.desktop++;
            }else {
                secretStatistics.laptop++;
            }
        }
        return newlist;
    }
    //非涉密中间机获取数据
    private List<NoneConfidentialIntermediary> getAllNoneConfidentialIntermediaryData(){
        NoneConfidentialIntermediary noneConfidentialIntermediary = new NoneConfidentialIntermediary();
        Page<NoneConfidentialIntermediary> noneConfidentialIntermediaryPage = new Page<>();
        noneConfidentialIntermediaryPage.setPageIndex(1);
        noneConfidentialIntermediaryPage.setPageSize(99999);
        noneConfidentialIntermediaryPage.setPageStart(0);
        noneConfidentialIntermediary.setPage(noneConfidentialIntermediaryPage);
        noneConfidentialIntermediary.setDepartment_code(dep);
        List<NoneConfidentialIntermediary> list = noneConfidentialIntermediaryService.selectDictListByPage(noneConfidentialIntermediary);
        return list;
    }
    //非涉密中间级数据转换
    private List<Map<String,String>> getNoneConfidentialIntermediaryList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<NoneConfidentialIntermediary> list = getAllNoneConfidentialIntermediaryData();
        int i = 1;
        for(NoneConfidentialIntermediary computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("type", computer.get_type());
            map.put("number", computer.getNumber());
            map.put("assetNumber", computer.getAsset_number());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("model", computer.getModel());
            map.put("osVersion", computer.get_os_version());
            map.put("osInstallTime", computer.getOs_install_time());
            map.put("serialNumber", computer.getSerial_number());
            map.put("macAddress", computer.getMac_address());
            map.put("cdDrive", computer.get_cd_drive());
            map.put("usage", computer.get_usage());
            map.put("placeLocation", computer.getPlace_location());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            secretStatistics.middle++;
        }
        return newlist;
    }
    //非涉密计算机获取数据
    private List<NoneConfidentialComputer> getAllNoneConfidentialComputerData(){
        NoneConfidentialComputer noneConfidentialComputer = new NoneConfidentialComputer();
        Page<NoneConfidentialComputer> noneConfidentialComputerPage = new Page<>();
        noneConfidentialComputerPage.setPageIndex(1);
        noneConfidentialComputerPage.setPageSize(99999);
        noneConfidentialComputerPage.setPageStart(0);
        noneConfidentialComputer.setPage(noneConfidentialComputerPage);
        noneConfidentialComputer.setDepartment_code(dep);
        List<NoneConfidentialComputer> list = noneConfidentialComputerService.selectDictListByPage(noneConfidentialComputer);
        return list;
    }
    //非涉密计算机数据转换
    private List<Map<String,String>> getNoneConfidentialComputerList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<NoneConfidentialComputer> list = getAllNoneConfidentialComputerData();
        int i = 1;
        for(NoneConfidentialComputer computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("type", computer.get_type());
            map.put("number", computer.getNumber());
            map.put("assetNumber", computer.getAsset_number());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("model", computer.getModel());
            map.put("osVersion", computer.get_os_version());
            map.put("osInstallTime", computer.getOs_install_time());
            map.put("serialNumber", computer.getSerial_number());
            map.put("ipAddress", computer.getIp_address());
            map.put("macAddress", computer.getMac_address());
            map.put("cdDrive", computer.get_cd_drive());
            map.put("usage", computer.get_usage());
            map.put("placeLocation", computer.getPlace_location());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            if(computer.get_type().equals("台式机")){
                noneSecretStatisticsCounts.desktop++;
            }else {
                noneSecretStatisticsCounts.laptop++;
            }
        }
        return newlist;
    }
    //报废涉密计算机获取数据
    private List<ConfidentialComputer> getAllScrappedComputerData(){
        ConfidentialComputer confidentialComputer = new ConfidentialComputer();
        Page<ConfidentialComputer> confidentialComputerPage = new Page<>();
        confidentialComputerPage.setPageIndex(1);
        confidentialComputerPage.setPageSize(99999);
        confidentialComputerPage.setPageStart(0);
        confidentialComputer.setPage(confidentialComputerPage);
        confidentialComputer.setDepartment_code(dep);
        confidentialComputer.setStartTime(startDate);
        confidentialComputer.setEndTime(endDate);
        List<ConfidentialComputer> list = scrappedComputerService.selectDictListByPage(confidentialComputer);
        return list;
    }
    //报废涉密计算机数据转换
    private List<Map<String,String>> getScrappedComputerList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<ConfidentialComputer> list = getAllScrappedComputerData();
        int i = 1;
        for(ConfidentialComputer computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("type", computer.get_type());
            map.put("secretNumber", computer.getSecret_number());
            map.put("assetNumber", computer.getAsset_number());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("model", computer.getModel());
            map.put("osVersion", computer.get_os_version());
            map.put("osInstallTime", computer.getOs_install_time());
            map.put("serialNumber", computer.getSerial_number());
            map.put("macAddress", computer.getMac_address());
            map.put("cdDrive", computer.get_cd_drive());
            map.put("usage", computer.get_usage());
            map.put("placeLocation", computer.getPlace_location());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd" );
            map.put("scrappedTime", sdf.format(computer.getScrap_time()));
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            if(computer.get_type().equals("台式机")){
                scrappedStatisticsCounts.desktop++;
            }else {
                scrappedStatisticsCounts.laptop++;
            }
        }
        return newlist;
    }
    //涉密信息设备获取数据
    private List<InfoDevice> getAllInfoDeviceData(){
        InfoDevice infoDevice = new InfoDevice();
        Page<InfoDevice> infoDevicePage = new Page<>();
        infoDevicePage.setPageIndex(1);
        infoDevicePage.setPageSize(99999);
        infoDevicePage.setPageStart(0);
        infoDevice.setPage(infoDevicePage);
        infoDevice.setDepartment_code(dep);
        List<InfoDevice> list = infoDeviceService.selectDictListByPage(infoDevice);
        return list;
    }
    //涉密信息设备数据转换
    private List<Map<String,String>> getInfoDeviceList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<InfoDevice> list = getAllInfoDeviceData();
        int i = 1;
        for(InfoDevice computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("type", computer.get_type());
            map.put("secretNumber", computer.getSecret_number());
            map.put("assetNumber", computer.getAsset_number());
            map.put("deviceName", computer.get_device_name());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("connectNumber", computer.getConnect_number());
            map.put("model", computer.getModel());
            map.put("deviceNumber", computer.getDevice_number());
            map.put("diskNumber", computer.getDisk_number());
            map.put("usage", computer.get_usage());
            map.put("placeLocation", computer.getPlace_location());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            if(computer.get_device_name().equals("打印机")){
                secretStatistics.printer++;
            } else if(computer.get_device_name().equals("复印机")){
                secretStatistics.copier++;
            } else {
                secretStatistics.noneInfoDeviceOther++;
            }
        }
        return newlist;
    }
    //非涉密信息设备获取数据
    private List<NonConfidentialInfoDevice> getAllNonConfidentialInfoDeviceData(){
        NonConfidentialInfoDevice nonConfidentialInfoDevice = new NonConfidentialInfoDevice();
        Page<NonConfidentialInfoDevice> nonConfidentialInfoDevicePage = new Page<>();
        nonConfidentialInfoDevicePage.setPageIndex(1);
        nonConfidentialInfoDevicePage.setPageSize(99999);
        nonConfidentialInfoDevicePage.setPageStart(0);
        nonConfidentialInfoDevice.setPage(nonConfidentialInfoDevicePage);
        nonConfidentialInfoDevice.setDepartment_code(dep);
        List<NonConfidentialInfoDevice> list = nonConfidentialInfoDeviceService.selectDictListByPage(nonConfidentialInfoDevice);
        return list;
    }
    //非涉密信息设备数据转换
    private List<Map<String,String>> getNonConfidentialInfoDeviceList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<NonConfidentialInfoDevice> list = getAllNonConfidentialInfoDeviceData();
        int i = 1;
        for(NonConfidentialInfoDevice computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("type", computer.get_type());
            map.put("number", computer.getNumber());
            map.put("assetNumber", computer.getAsset_number());
            map.put("deviceName", computer.get_device_name());
            map.put("person", computer.getPerson());
//            map.put("secretLevel", computer.get_secret_level());
            map.put("secretLevel", "非密");
            map.put("model", computer.getModel());
            map.put("deviceNumber", computer.getDevice_number());
            map.put("diskNumber", computer.getDisk_number());
//            map.put("usage", computer.get_usage());
            map.put("usage", "办公/科研");
            map.put("placeLocation", computer.getPlace_location());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            if(computer.get_device_name().equals("打印机")){
                noneSecretStatisticsCounts.printer++;
            } else if(computer.get_device_name().equals("复印机")){
                noneSecretStatisticsCounts.copier++;
            } else {
                noneSecretStatisticsCounts.noneInfoDeviceOther++;
            }
        }
        return newlist;
    }
    //报废涉密信息设备获取数据
    private List<InfoDevice> getAllScrappedInfoDeviceData(){
        InfoDevice infoDevice = new InfoDevice();
        Page<InfoDevice> infoDevicePage = new Page<>();
        infoDevicePage.setPageIndex(1);
        infoDevicePage.setPageSize(99999);
        infoDevicePage.setPageStart(0);
        infoDevice.setPage(infoDevicePage);
        infoDevice.setDepartment_code(dep);
        infoDevice.setStartTime(startDate);
        infoDevice.setEndTime(endDate);
        List<InfoDevice> list = scrappedInfoDeviceService.selectDictListByPage(infoDevice);
        return list;
    }
    //报废涉密信息设备数据转换
    private List<Map<String,String>> getScrappedInfoDeviceList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<InfoDevice> list = getAllScrappedInfoDeviceData();
        int i = 1;
        for(InfoDevice computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("type", computer.get_type());
            map.put("secretNumber", computer.getSecret_number());
            map.put("assetNumber", computer.getAsset_number());
            map.put("deviceName", computer.get_device_name());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("connectNumber", computer.getConnect_number());
            map.put("model", computer.getModel());
            map.put("deviceNumber", computer.getDevice_number());
            map.put("diskNumber", computer.getDisk_number());
            map.put("usage", computer.get_usage());
            map.put("placeLocation", computer.getPlace_location());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd" );
            map.put("scrappedTime", sdf.format(computer.getScrap_time()));
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            if(computer.get_device_name().equals("打印机")){
                scrappedStatisticsCounts.printer++;
            } else if(computer.get_device_name().equals("复印机")){
                scrappedStatisticsCounts.copier++;
            } else {
                scrappedStatisticsCounts.noneInfoDeviceOther++;
            }
        }
        return newlist;
    }
    //涉密存储介质获取数据
    private List<ConfidentialStorage> getAllConfidentialStorageData(){
        ConfidentialStorage confidentialStorage = new ConfidentialStorage();
        Page<ConfidentialStorage> confidentialStoragePage = new Page<>();
        confidentialStoragePage.setPageIndex(1);
        confidentialStoragePage.setPageSize(99999);
        confidentialStoragePage.setPageStart(0);
        confidentialStorage.setPage(confidentialStoragePage);
        confidentialStorage.setDepartment_code(dep);
        List<ConfidentialStorage> list = confidentialStorageService.selectDictListByPage(confidentialStorage);
        return list;
    }
    //涉密存储介质数据转换
    private List<Map<String,String>> getConfidentialStorageList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<ConfidentialStorage> list = getAllConfidentialStorageData();
        int i = 1;
        for(ConfidentialStorage computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("secretNumber", computer.getSecret_number());
            map.put("type", computer.get_type());
            map.put("model", computer.getModel());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("serialNumber", computer.getSerial_number());
            map.put("usage", computer.get_usage());
            map.put("placeLocation", computer.getPlace_location());
            map.put("scope", computer.get_scope());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            if(computer.get_type().equals("U盘")){
                secretStatistics.udisk++;
            } else if(computer.get_type().equals("硬盘")){
                secretStatistics.disk++;
            } else {
                secretStatistics.noneSecProOther++;
            }
        }
        return newlist;
    }
    //非涉密存储介质获取数据
    private List<NonConfidentialStorage> getAllNonConfidentialStorageData(){
        NonConfidentialStorage nonConfidentialStorage = new NonConfidentialStorage();
        Page<NonConfidentialStorage> nonConfidentialStoragePage = new Page<>();
        nonConfidentialStoragePage.setPageIndex(1);
        nonConfidentialStoragePage.setPageSize(99999);
        nonConfidentialStoragePage.setPageStart(0);
        nonConfidentialStorage.setPage(nonConfidentialStoragePage);
        nonConfidentialStorage.setDepartment_code(dep);
        List<NonConfidentialStorage> list = nonConfidentialStorageService.selectDictListByPage(nonConfidentialStorage);
        return list;
    }
    //非涉密存储介质数据转换
    private List<Map<String,String>> getNonConfidentialStorageList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<NonConfidentialStorage> list = getAllNonConfidentialStorageData();
        int i = 1;
        for(NonConfidentialStorage computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("number", computer.getNumber());
            map.put("type", computer.get_type());
            map.put("model", computer.getModel());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("serialNumber", computer.getSerial_number());
            map.put("usage", computer.get_usage());
            map.put("placeLocation", computer.getPlace_location());
            map.put("scope", computer.get_scope());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            if(computer.get_type().equals("U盘")){
                noneSecretStatisticsCounts.udisk++;
            } else if(computer.get_type().equals("硬盘")){
                noneSecretStatisticsCounts.disk++;
            } else {
                noneSecretStatisticsCounts.noneSecProOther++;
            }
        }
        return newlist;
    }
    //报废涉密存储介质获取数据
    private List<ConfidentialStorage> getAllScrappedStorageData(){
        ConfidentialStorage confidentialStorage = new ConfidentialStorage();
        Page<ConfidentialStorage> confidentialStoragePage = new Page<>();
        confidentialStoragePage.setPageIndex(1);
        confidentialStoragePage.setPageSize(99999);
        confidentialStoragePage.setPageStart(0);
        confidentialStorage.setPage(confidentialStoragePage);
        confidentialStorage.setDepartment_code(dep);
        confidentialStorage.setStartTime(startDate);
        confidentialStorage.setEndTime(endDate);
        List<ConfidentialStorage> list = scrappedStorageService.selectDictListByPage(confidentialStorage);
        return list;
    }
    //报废涉密存储介质数据转换
    private List<Map<String,String>> getScrappedStorageList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<ConfidentialStorage> list = getAllScrappedStorageData();
        int i = 1;
        for(ConfidentialStorage computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("secretNumber", computer.getSecret_number());
            map.put("type", computer.get_type());
            map.put("model", computer.getModel());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("serialNumber", computer.getSerial_number());
            map.put("usage", computer.get_usage());
            map.put("placeLocation", computer.getPlace_location());
            map.put("scope", computer.get_scope());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd" );
            map.put("scrappedTime", sdf.format(computer.getScrap_time()));
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            if(computer.get_type().equals("U盘")){
                scrappedStatisticsCounts.udisk++;
            } else if(computer.get_type().equals("硬盘")){
                scrappedStatisticsCounts.disk++;
            } else {
                scrappedStatisticsCounts.noneSecProOther++;
            }
        }
        return newlist;
    }
    //保密产品数据获取
    private List<SecurityProduct> getAllSecurityProductData(){
        SecurityProduct securityProduct = new SecurityProduct();
        Page<SecurityProduct> securityProductPage = new Page<>();
        securityProductPage.setPageIndex(1);
        securityProductPage.setPageSize(99999);
        securityProductPage.setPageStart(0);
        securityProduct.setPage(securityProductPage);
        securityProduct.setDepartment_code(dep);
        List<SecurityProduct> list = securityProductService.selectDictListByPage(securityProduct);
        return list;
    }
    //保密产品数据转换
    private List<Map<String,String>> getSecurityProductList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<SecurityProduct> list = getAllSecurityProductData();
        int i = 1;
        for(SecurityProduct computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("secretNumber", computer.getSecret_number());
            map.put("assetNumber", computer.getAsset_number());
            map.put("type", computer.get_type());
            map.put("productVersion", computer.getProduct_version());
            map.put("manufacturer", computer.getManufacturer());
            map.put("certificateNumber", computer.getCertificate_number());
            map.put("certificateValidity", computer.getCertificate_validity());
            map.put("serialNumber", computer.getSerial_number());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("placeLocation", computer.getPlace_location());
            map.put("scope", computer.get_scope());
            map.put("buyTime", computer.getBuy_time());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            map.put("remark", computer.getRemarks());
            newlist.add(map);
        }
        return newlist;
    }
    //报废保密产品数据获取
    private List<SecurityProduct> getAllScrappedProductData(){
        SecurityProduct securityProduct = new SecurityProduct();
        Page<SecurityProduct> securityProductPage = new Page<>();
        securityProductPage.setPageIndex(1);
        securityProductPage.setPageSize(99999);
        securityProductPage.setPageStart(0);
        securityProduct.setPage(securityProductPage);
        securityProduct.setDepartment_code(dep);
        securityProduct.setStartTime(startDate);
        securityProduct.setEndTime(endDate);
        List<SecurityProduct> list = scrappedProductService.selectDictListByPage(securityProduct);
        return list;
    }
    //报废保密产品数据转换
    private List<Map<String,String>> getScrappedProductList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<SecurityProduct> list = getAllScrappedProductData();
        int i = 1;
        for(SecurityProduct computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("secretNumber", computer.getSecret_number());
            map.put("assetNumber", computer.getAsset_number());
            map.put("type", computer.get_type());
            map.put("productVersion", computer.getProduct_version());
            map.put("manufacturer", computer.getManufacturer());
            map.put("certificateNumber", computer.getCertificate_number());
            map.put("certificateValidity", computer.getCertificate_validity());
            map.put("serialNumber", computer.getSerial_number());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("placeLocation", computer.getPlace_location());
            map.put("scope", computer.get_scope());
            map.put("buyTime", computer.getBuy_time());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd" );
            map.put("scrappedTime", sdf.format(computer.getScrap_time()));
            map.put("remark", computer.getRemarks());
            newlist.add(map);
        }
        return newlist;
    }
    //USBKey数据获取
    private List<Usb> getAllUSBData(){
        Usb usb = new Usb();
        Page<Usb> usbPage = new Page<>();
        usbPage.setPageIndex(1);
        usbPage.setPageSize(99999);
        usbPage.setPageStart(0);
        usb.setPage(usbPage);
        usb.setDepartment_code(dep);
        List<Usb> list = usbService.selectDictListByPage(usb);
        return list;
    }
    //USBKey数据转换
    private List<Map<String,String>> getUSBList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<Usb> list = getAllUSBData();
        int i = 1;
        for(Usb computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("secretNumber", computer.getSecret_number());
            map.put("type", computer.get_type());
            map.put("model", computer.getModel());
            map.put("serialNumber", computer.getSerial_number());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("placeLocation", computer.getPlace_location());
            map.put("usage", computer.get_usage());
            map.put("scope", computer.get_scope());
            map.put("connectNumber", computer.getConnect_number());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            secretStatistics.usb++;
        }
        return newlist;
    }
    //报废USBKey数据获取
    private List<Usb> getAllScrappedUSBData(){
        Usb usb = new Usb();
        Page<Usb> usbPage = new Page<>();
        usbPage.setPageIndex(1);
        usbPage.setPageSize(99999);
        usbPage.setPageStart(0);
        usb.setPage(usbPage);
        usb.setDepartment_code(dep);
        usb.setStartTime(startDate);
        usb.setEndTime(endDate);
        List<Usb> list = scrappedUsbService.selectDictListByPage(usb);
        return list;
    }
    //报废USBKey数据转换
    private List<Map<String,String>> getScrappedUSBList(){
        List<Map<String,String>> newlist = new ArrayList<>();
        List<Usb> list = getAllScrappedUSBData();
        int i = 1;
        for(Usb computer : list){
            Map<String,String> map = new HashMap<String,String>();
            map.put("index", String.valueOf(i++));
            map.put("department", computer.getDepartment_name());
            map.put("subject", computer.getSubject_name());
            map.put("secretNumber", computer.getSecret_number());
            map.put("type", computer.get_type());
            map.put("model", computer.getModel());
            map.put("serialNumber", computer.getSerial_number());
            map.put("person", computer.getPerson());
            map.put("secretLevel", computer.get_secret_level());
            map.put("placeLocation", computer.getPlace_location());
            map.put("usage", computer.get_usage());
            map.put("scope", computer.get_scope());
            map.put("connectNumber", computer.getConnect_number());
            map.put("enablationTime", computer.getEnablation_time());
            map.put("useSituation", computer.get_use_situation());
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd" );
            map.put("scrappedTime", sdf.format(computer.getScrap_time()));
            map.put("remark", computer.getRemarks());
            newlist.add(map);
            scrappedStatisticsCounts.usb++;
        }
        return newlist;
    }
}
