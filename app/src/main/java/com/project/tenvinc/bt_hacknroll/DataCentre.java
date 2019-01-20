package com.project.tenvinc.bt_hacknroll;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class DataCentre {
    private static final DataCentre ourInstance = new DataCentre();
    public List<NamedBeacon> trackedBeacons = new ArrayList<>();

    public static DataCentre getInstance() {
        return ourInstance;
    }

    private DataCentre() {
    }
}
