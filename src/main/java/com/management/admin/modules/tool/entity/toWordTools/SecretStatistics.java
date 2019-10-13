package com.management.admin.modules.tool.entity.toWordTools;

public class SecretStatistics {
    public int desktop;
    public int laptop;
    public int printer;
    public int copier;
    public int middle;
    public int noneInfoDeviceOther;
    public int udisk;
    public int disk;
    public int noneSecProOther;
    public int usb;

    public SecretStatistics(){
        this.desktop = 0;
        this.laptop = 0;
        this.printer = 0;
        this.copier = 0;
        this.noneInfoDeviceOther = 0;
        this.udisk = 0;
        this.disk = 0;
        this.noneSecProOther = 0;
        this.usb= 0;
        this.middle = 0;
    }
}
