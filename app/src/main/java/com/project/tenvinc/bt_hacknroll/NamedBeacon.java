package com.project.tenvinc.bt_hacknroll;

import org.altbeacon.beacon.Beacon;

import java.util.jar.Attributes;

public class NamedBeacon {

    private String name;
    private String macAddress;
    private String uuid;
    private double RSSI;
    private String major;
    private String minor;
    private Beacon beacon;

    public NamedBeacon(String name, String macAddress, String uuid, String major, String minor, double RSSI) {
        this.name = name;
        this.macAddress = macAddress;
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.RSSI = RSSI;
    }

    public NamedBeacon(String name, Beacon beacon) {
        this.name = name;
        this.beacon = beacon;
    }

    public String getName() {
        return name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getUuid() {
        return uuid;
    }

    public double getRSSI() {
        return RSSI;
    }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }

    public Beacon getBeacon() { return beacon; }
}
