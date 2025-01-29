package org.application.services;

import org.application.services.contractassignment.BaseContractAssignmentService;
import org.application.services.contractassignment.ContractAssignmentService;
import org.application.services.drivetime.BaseDriveTimeCalculationService;
import org.application.services.drivetime.DriveTimeCalculationService;

public class ServiceManager {
    private static ServiceManager INSTANCE;
    private final ContractAssignmentService contractAssignmentService;
    private final DriveTimeCalculationService driveTimeCalculationService;

    public static ServiceManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServiceManager();
        }
        return INSTANCE;
    }

    private ServiceManager() {
        this.contractAssignmentService = new BaseContractAssignmentService();
        this.driveTimeCalculationService = new BaseDriveTimeCalculationService();
    }

    public ContractAssignmentService getContractAssignmentService() {
        return contractAssignmentService;
    }

    public DriveTimeCalculationService getDriveTimeCalculationService() {
        return driveTimeCalculationService;
    }
}
