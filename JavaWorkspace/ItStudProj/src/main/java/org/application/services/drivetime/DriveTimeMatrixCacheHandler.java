package org.application.services.drivetime;

import org.application.dtos.Contract;
import org.application.services.ServiceManager;

import java.util.HashMap;

public class DriveTimeMatrixCacheHandler {

    private static DriveTimeMatrixCacheHandler INSTANCE;
    private final HashMap<String, Double> contractDriveTimeCache = new HashMap<>();
    private DriveTimeCalculationService driveTimeCalculationService;


    public static DriveTimeMatrixCacheHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DriveTimeMatrixCacheHandler();
            INSTANCE.driveTimeCalculationService = ServiceManager.getInstance().getDriveTimeCalculationService();
        }

        return INSTANCE;
    }

    public double getDriveTime(Contract contractA, Contract contractB) throws DriveTimeCalculationException {
        Double driveTimeCache = contractDriveTimeCache.get(getKeyString(contractA, contractB));

        if (driveTimeCache != null) {
            return driveTimeCache;
        }

        double driveTime = driveTimeCalculationService.calculateDriveTime(contractA.getLatitude(), contractA.getLongitude(), contractB.getLatitude(), contractB.getLongitude());
        contractDriveTimeCache.put(getKeyString(contractA, contractB), driveTime);
        return driveTime;
    }

    private String getKeyString(Contract contractA, Contract contractB) {
        return contractA.getId() + "-" + contractB.getId();
    }
}
