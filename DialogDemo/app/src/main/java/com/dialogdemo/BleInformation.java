package com.dialogdemo;

/**
 * Created by jiajiewang on 16/6/14.
 */
public class BleInformation {
    public String MacAddress;
    public String UUID;
    public String RSSI;

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String macAddress) {
        MacAddress = macAddress;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getRSSI() {
        return RSSI;
    }

    public void setRSSI(String RSSI) {
        this.RSSI = RSSI;
    }
}
