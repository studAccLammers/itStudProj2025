package org.application.services;

import org.application.dtos.Contract;
import org.application.dtos.TestDataHolder;

import java.util.HashMap;
import java.util.List;

public class DriveTimeMatrixHandler {

    private static DriveTimeMatrixHandler INSTANCE;
    private final HashMap<String, Double> contractDriveTimeMatrix = new HashMap<>();
    private DriveTimeCalculationService driveTimeCalculationService;


    public static DriveTimeMatrixHandler getInstance() throws DriveTimeCalculationException {
        if (INSTANCE == null) {
            INSTANCE = new DriveTimeMatrixHandler();
            INSTANCE.driveTimeCalculationService = ServiceManager.getInstance().getDriveTimeCalculationService();
            INSTANCE.calculateMatrix(TestDataHolder.getInstance().getContracts());
        }

        return INSTANCE;
    }

    public double getDriveTime(Contract contractA, Contract contractB) {
        return contractDriveTimeMatrix.get(getKeyString(contractA, contractB));
    }

    private void calculateMatrix(List<Contract> contracts) throws DriveTimeCalculationException {
        for (Contract cA : contracts) {
            for (Contract cB : contracts) {
                if (cA.equals(cB)) {
                    continue;
                }

                contractDriveTimeMatrix.put(getKeyString(cA, cB),
                    driveTimeCalculationService.calculateDriveTime(
                        cA.getLatitude(), cA.getLongitude(), cB.getLatitude(), cB.getLongitude())
                );
            }
        }
    }

    private String getKeyString(Contract contractA, Contract contractB) {
        return contractA.getId() + "-" + contractB.getId();
    }
}
