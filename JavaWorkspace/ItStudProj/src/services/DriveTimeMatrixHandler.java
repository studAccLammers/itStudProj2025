package services;

import dtos.Contract;
import dtos.TestDataHolder;

import java.util.HashMap;
import java.util.List;

public class DriveTimeMatrixHandler {

    private static DriveTimeMatrixHandler INSTANCE;

    private final HashMap<String, Double> contractDriveTimeMatrix = new HashMap<>();

    public static DriveTimeMatrixHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DriveTimeMatrixHandler();
            INSTANCE.calculateMatrix(TestDataHolder.getInstance().getContracts());
        }

        return INSTANCE;
    }

    public double getDriveTime(Contract contractA, Contract contractB) {
        return contractDriveTimeMatrix.get(getKeyString(contractA, contractB));
    }

    private void calculateMatrix(List<Contract> contracts) {
        for (Contract cA : contracts) {
            for (Contract cB : contracts) {
                double driveTime = 0.1;
                if (cA.getId() == cB.getId()) {
                    driveTime = 0.0;
                } else {
                    boolean aInLoc1 = cA.getId() >= 1 && cA.getId() <= 5;
                    boolean aInLoc2 = cA.getId() >= 6 && cA.getId() <= 10;
                    boolean aInLoc3 = cA.getId() >= 11 && cA.getId() <= 15;
                    boolean bInLoc1 = cB.getId() >= 1 && cB.getId() <= 5;
                    boolean bInLoc2 = cB.getId() >= 6 && cB.getId() <= 10;
                    boolean bInLoc3 = cB.getId() >= 11 && cB.getId() <= 15;
                    if ((aInLoc1 && bInLoc2) || (aInLoc2 && bInLoc1)) driveTime = 0.7;
                    if ((aInLoc2 && bInLoc3) || (aInLoc3 && bInLoc2)) driveTime = 0.9;
                    if ((aInLoc1 && bInLoc3) || (aInLoc3 && bInLoc1)) driveTime = 4.0;
                }
                contractDriveTimeMatrix.put(getKeyString(cA, cB), driveTime);
            }
        }
    }

    private String getKeyString(Contract contractA, Contract contractB) {
        return contractA.getId() + "-" + contractB.getId();
    }
}
