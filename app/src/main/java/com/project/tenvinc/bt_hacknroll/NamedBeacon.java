package com.project.tenvinc.bt_hacknroll;

import org.altbeacon.beacon.Beacon;

import java.util.jar.Attributes;

public class NamedBeacon {

    private String name;
    private Beacon beacon;

    public NamedBeacon(String name, Beacon beacon) {
        this.name = name;
        this.beacon = beacon;
    }

    public String getName() {
        return name;
    }

    public Beacon getBeacon() {
        return beacon;
    }
}
