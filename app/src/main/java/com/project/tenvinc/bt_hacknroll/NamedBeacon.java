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

    public Beacon getBeacon() {
        return beacon;
    }

    public int getTxPower() {
        return beacon.getTxPower();
    }


    //Approximation references: https://stackoverflow.com/questions/20416218/understanding-ibeacon-distancing/20434019#20434019
    public double getApproxDist(int txCalibratedPower, double rssi) {
        int ratiodB = txCalibratedPower - rssi;
        double linearRatio = java.lang.Math.pow(10.0, ((double)ratiodB)/10.0);
        double approxDist = java.lang.Math.sqrt(linearRatio);

        return approxDist;
    }

    public double getAltApproxDist(int txCalibratedPower, double rssi) {
        if (rssi == 0) {
            return -1;
        }

        double ratio = ((double)rssi)/txCalibratedPower;
        if (ratio < 1.0) {
            return Math.pow(ratio,10);
        }
        else {
            double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;
            return accuracy;
        }
    }
}
