package org.application.services;

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
        this.contractAssignmentService = new BaseContractAssignmentAssignmentService();
        this.driveTimeCalculationService = new BaseDriveTimeCalculationService();
    }

    public ContractAssignmentService getContractAssignmentService() {
        return contractAssignmentService;
    }

    public DriveTimeCalculationService getDriveTimeCalculationService() {
        return driveTimeCalculationService;
    }
}
