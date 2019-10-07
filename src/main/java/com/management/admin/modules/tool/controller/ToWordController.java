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
import com.management.admin.modules.tool.dao.ImportDataDao;
import com.management.admin.modules.tool.entity.tiny.DictInfo;
import com.management.admin.modules.tool.entity.tiny.PartInfo;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ResponseBody
    @RequestMapping("toword")
    public void toword(
            HttpServletResponse response,
            @RequestParam String department
    ){
        try {
            Map<String, Object> dataMap = new HashMap<>();
            //数据导入
            dataMap.put("confidentialComputerList", getConfidentiaComputerList());
            dataMap.put("noneConfidentialIntermediaryList", getNoneConfidentialIntermediaryList());
            dataMap.put("noneConfidentialComputerList", getNoneConfidentialComputerList());
            dataMap.put("scrappedComputerList", getScrappedComputerList());
            dataMap.put("infoDeviceList", getInfoDeviceList());
            dataMap.put("noneConfidentialInfoDeviceList", getNonConfidentialInfoDeviceList());
            dataMap.put("scrappedInfoDeviceList", getScrappedInfoDeviceList());
            dataMap.put("confidentialStorageList", getConfidentialStorageList());
            dataMap.put("noneConfidentialStorageList", getNonConfidentialStorageList());
            dataMap.put("scrappedStorageList", getScrappedStorageList());
            dataMap.put("securityProductList", getSecurityProductList());
            dataMap.put("scrappedProductList", getScrappedProductList());
            dataMap.put("usbList", getUSBList());
            dataMap.put("scrappedUsbList", getScrappedUSBList());

            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("utf-8");
            //指定模板路径的第二种方式,我的路径是D:/      还有其他方式
//            configuration.setDirectoryForTemplateLoading(new File("D:/"));
            configuration.setClassForTemplateLoading(this.getClass(), "/template");

            //以utf-8的编码读取ftl文件
            Template t =  configuration.getTemplate("test.ftl","utf-8");
            String fileName = URLEncoder.encode("word数据", "UTF-8") + ".doc";
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            Writer out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "utf-8"),10240);
            t.process(dataMap, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }
    //涉密计算机获取数据
    public List<ConfidentialComputer> getAllConfidentiaComputerData(){
        ConfidentialComputer confidentialComputer = new ConfidentialComputer();
        Page<ConfidentialComputer> confidentialComputerPage = new Page<>();
        confidentialComputerPage.setPageIndex(1);
        confidentialComputerPage.setPageSize(99999);
        confidentialComputerPage.setPageStart(0);
        confidentialComputer.setPage(confidentialComputerPage);
        List<ConfidentialComputer> list = confidentialComputerService.selectDictListByPage(confidentialComputer);
        return list;
    }
    //涉密计算机数据转换
    public List<Map<String,String>> getConfidentiaComputerList(){
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
        }
        return newlist;
    }
    //非涉密中间机获取数据
    public List<NoneConfidentialIntermediary> getAllNoneConfidentialIntermediaryData(){
        NoneConfidentialIntermediary noneConfidentialIntermediary = new NoneConfidentialIntermediary();
        Page<NoneConfidentialIntermediary> noneConfidentialIntermediaryPage = new Page<>();
        noneConfidentialIntermediaryPage.setPageIndex(1);
        noneConfidentialIntermediaryPage.setPageSize(99999);
        noneConfidentialIntermediaryPage.setPageStart(0);
        noneConfidentialIntermediary.setPage(noneConfidentialIntermediaryPage);
        List<NoneConfidentialIntermediary> list = noneConfidentialIntermediaryService.selectDictListByPage(noneConfidentialIntermediary);
        return list;
    }
    //非涉密中间级数据转换
    public List<Map<String,String>> getNoneConfidentialIntermediaryList(){
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
        }
        return newlist;
    }
    //非涉密计算机获取数据
    public List<NoneConfidentialComputer> getAllNoneConfidentialComputerData(){
        NoneConfidentialComputer noneConfidentialComputer = new NoneConfidentialComputer();
        Page<NoneConfidentialComputer> noneConfidentialComputerPage = new Page<>();
        noneConfidentialComputerPage.setPageIndex(1);
        noneConfidentialComputerPage.setPageSize(99999);
        noneConfidentialComputerPage.setPageStart(0);
        noneConfidentialComputer.setPage(noneConfidentialComputerPage);
        List<NoneConfidentialComputer> list = noneConfidentialComputerService.selectDictListByPage(noneConfidentialComputer);
        return list;
    }
    //非涉密计算机数据转换
    public List<Map<String,String>> getNoneConfidentialComputerList(){
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
        }
        return newlist;
    }
    //报废涉密计算机获取数据
    public List<ConfidentialComputer> getAllScrappedComputerData(){
        ConfidentialComputer confidentialComputer = new ConfidentialComputer();
        Page<ConfidentialComputer> confidentialComputerPage = new Page<>();
        confidentialComputerPage.setPageIndex(1);
        confidentialComputerPage.setPageSize(99999);
        confidentialComputerPage.setPageStart(0);
        confidentialComputer.setPage(confidentialComputerPage);
        List<ConfidentialComputer> list = scrappedComputerService.selectDictListByPage(confidentialComputer);
        return list;
    }
    //报废涉密计算机数据转换
    public List<Map<String,String>> getScrappedComputerList(){
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
        }
        return newlist;
    }
    //涉密信息设备获取数据
    public List<InfoDevice> getAllInfoDeviceData(){
        InfoDevice infoDevice = new InfoDevice();
        Page<InfoDevice> infoDevicePage = new Page<>();
        infoDevicePage.setPageIndex(1);
        infoDevicePage.setPageSize(99999);
        infoDevicePage.setPageStart(0);
        infoDevice.setPage(infoDevicePage);
        List<InfoDevice> list = infoDeviceService.selectDictListByPage(infoDevice);
        return list;
    }
    //涉密信息设备数据转换
    public List<Map<String,String>> getInfoDeviceList(){
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
        }
        return newlist;
    }
    //非涉密信息设备获取数据
    public List<NonConfidentialInfoDevice> getAllNonConfidentialInfoDeviceData(){
        NonConfidentialInfoDevice nonConfidentialInfoDevice = new NonConfidentialInfoDevice();
        Page<NonConfidentialInfoDevice> nonConfidentialInfoDevicePage = new Page<>();
        nonConfidentialInfoDevicePage.setPageIndex(1);
        nonConfidentialInfoDevicePage.setPageSize(99999);
        nonConfidentialInfoDevicePage.setPageStart(0);
        nonConfidentialInfoDevice.setPage(nonConfidentialInfoDevicePage);
        List<NonConfidentialInfoDevice> list = nonConfidentialInfoDeviceService.selectDictListByPage(nonConfidentialInfoDevice);
        return list;
    }
    //非涉密信息设备数据转换
    public List<Map<String,String>> getNonConfidentialInfoDeviceList(){
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
        }
        return newlist;
    }
    //报废涉密信息设备获取数据
    public List<InfoDevice> getAllScrappedInfoDeviceData(){
        InfoDevice infoDevice = new InfoDevice();
        Page<InfoDevice> infoDevicePage = new Page<>();
        infoDevicePage.setPageIndex(1);
        infoDevicePage.setPageSize(99999);
        infoDevicePage.setPageStart(0);
        infoDevice.setPage(infoDevicePage);
        List<InfoDevice> list = scrappedInfoDeviceService.selectDictListByPage(infoDevice);
        return list;
    }
    //报废涉密信息设备数据转换
    public List<Map<String,String>> getScrappedInfoDeviceList(){
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
        }
        return newlist;
    }
    //涉密存储介质获取数据
    public List<ConfidentialStorage> getAllConfidentialStorageData(){
        ConfidentialStorage confidentialStorage = new ConfidentialStorage();
        Page<ConfidentialStorage> confidentialStoragePage = new Page<>();
        confidentialStoragePage.setPageIndex(1);
        confidentialStoragePage.setPageSize(99999);
        confidentialStoragePage.setPageStart(0);
        confidentialStorage.setPage(confidentialStoragePage);
        List<ConfidentialStorage> list = confidentialStorageService.selectDictListByPage(confidentialStorage);
        return list;
    }
    //涉密存储介质数据转换
    public List<Map<String,String>> getConfidentialStorageList(){
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
        }
        return newlist;
    }
    //非涉密存储介质获取数据
    public List<NonConfidentialStorage> getAllNonConfidentialStorageData(){
        NonConfidentialStorage nonConfidentialStorage = new NonConfidentialStorage();
        Page<NonConfidentialStorage> nonConfidentialStoragePage = new Page<>();
        nonConfidentialStoragePage.setPageIndex(1);
        nonConfidentialStoragePage.setPageSize(99999);
        nonConfidentialStoragePage.setPageStart(0);
        nonConfidentialStorage.setPage(nonConfidentialStoragePage);
        List<NonConfidentialStorage> list = nonConfidentialStorageService.selectDictListByPage(nonConfidentialStorage);
        return list;
    }
    //非涉密存储介质数据转换
    public List<Map<String,String>> getNonConfidentialStorageList(){
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
        }
        return newlist;
    }
    //报废涉密存储介质获取数据
    public List<ConfidentialStorage> getAllScrappedStorageData(){
        ConfidentialStorage confidentialStorage = new ConfidentialStorage();
        Page<ConfidentialStorage> confidentialStoragePage = new Page<>();
        confidentialStoragePage.setPageIndex(1);
        confidentialStoragePage.setPageSize(99999);
        confidentialStoragePage.setPageStart(0);
        confidentialStorage.setPage(confidentialStoragePage);
        List<ConfidentialStorage> list = scrappedStorageService.selectDictListByPage(confidentialStorage);
        return list;
    }
    //报废涉密存储介质数据转换
    public List<Map<String,String>> getScrappedStorageList(){
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
        }
        return newlist;
    }
    //保密产品数据获取
    public List<SecurityProduct> getAllSecurityProductData(){
        SecurityProduct securityProduct = new SecurityProduct();
        Page<SecurityProduct> securityProductPage = new Page<>();
        securityProductPage.setPageIndex(1);
        securityProductPage.setPageSize(99999);
        securityProductPage.setPageStart(0);
        securityProduct.setPage(securityProductPage);
        List<SecurityProduct> list = securityProductService.selectDictListByPage(securityProduct);
        return list;
    }
    //保密产品数据转换
    public List<Map<String,String>> getSecurityProductList(){
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
    public List<SecurityProduct> getAllScrappedProductData(){
        SecurityProduct securityProduct = new SecurityProduct();
        Page<SecurityProduct> securityProductPage = new Page<>();
        securityProductPage.setPageIndex(1);
        securityProductPage.setPageSize(99999);
        securityProductPage.setPageStart(0);
        securityProduct.setPage(securityProductPage);
        List<SecurityProduct> list = scrappedProductService.selectDictListByPage(securityProduct);
        return list;
    }
    //报废保密产品数据转换
    public List<Map<String,String>> getScrappedProductList(){
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
    public List<Usb> getAllUSBData(){
        Usb usb = new Usb();
        Page<Usb> usbPage = new Page<>();
        usbPage.setPageIndex(1);
        usbPage.setPageSize(99999);
        usbPage.setPageStart(0);
        usb.setPage(usbPage);
        List<Usb> list = usbService.selectDictListByPage(usb);
        return list;
    }
    //USBKey数据转换
    public List<Map<String,String>> getUSBList(){
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
        }
        return newlist;
    }
    //报废USBKey数据获取
    public List<Usb> getAllScrappedUSBData(){
        Usb usb = new Usb();
        Page<Usb> usbPage = new Page<>();
        usbPage.setPageIndex(1);
        usbPage.setPageSize(99999);
        usbPage.setPageStart(0);
        usb.setPage(usbPage);
        List<Usb> list = scrappedUsbService.selectDictListByPage(usb);
        return list;
    }
    //报废USBKey数据转换
    public List<Map<String,String>> getScrappedUSBList(){
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
        }
        return newlist;
    }
}
