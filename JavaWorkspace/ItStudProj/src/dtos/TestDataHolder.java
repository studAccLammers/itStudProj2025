package dtos;

import java.util.ArrayList;
import java.util.List;

public class TestDataHolder {
    private static TestDataHolder INSTANCE = new TestDataHolder();
    private final List<Contract> contracts = new ArrayList<>();
    private final List<ContractToContract> contractDriveTimeMatrix = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();

    private TestDataHolder() {
        contracts.add(new Contract(1, 2, List.of(Skill.HEAVY_DELIVERY, Skill.ELECTRONICS), Priority.HIGH, 0.23));
        contracts.add(new Contract(2, 1, List.of(Skill.REPAIR), Priority.HIGH, 0.14));
        contracts.add(new Contract(3, 3, List.of(Skill.INSTALLATION), Priority.HIGHEST, 0.36));
        contracts.add(new Contract(4, 2, List.of(Skill.HEAVY_DELIVERY), Priority.HIGHEST, 0.25));
        contracts.add(new Contract(5, 4, List.of(), Priority.NORMAL, 0.44));
        contracts.add(new Contract(6, 1, List.of(), Priority.HIGH, 0.53));
        contracts.add(new Contract(7, 4, List.of(Skill.ELECTRONICS), Priority.NORMAL, 0.42));
        contracts.add(new Contract(8, 1, List.of(Skill.INSTALLATION), Priority.HIGHEST, 0.46));
        contracts.add(new Contract(9, 8, List.of(Skill.HEAVY_DELIVERY), Priority.NORMAL, 0.65));
        contracts.add(new Contract(10, 9, List.of(Skill.UNINSTALLATION), Priority.HIGH, 0.92));
        contracts.add(new Contract(11, 2, List.of(Skill.REPAIR), Priority.HIGH, 0.87));
        contracts.add(new Contract(12, 9, List.of(), Priority.HIGHEST, 0.73));
        contracts.add(new Contract(13, 4, List.of(Skill.INSTALLATION), Priority.HIGH, 0.49));
        contracts.add(new Contract(14, 1, List.of(Skill.HEAVY_DELIVERY), Priority.NORMAL, 0.91));
        contracts.add(new Contract(15, 2, List.of(Skill.UNINSTALLATION), Priority.HIGH, 0.75));
        fillDriveTimeMatrix();
        employees.add(new Employee(1, "Employee 1", 0, 10, 40, List.of(Skill.REPAIR, Skill.HEAVY_DELIVERY)));
        employees.add(new Employee(2, "Employee 2", 0, 10, 40, List.of(Skill.HEAVY_DELIVERY, Skill.ELECTRONICS, Skill.INSTALLATION)));
    }

    public static TestDataHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestDataHolder();
        }

        return INSTANCE;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public List<ContractToContract> getContractDriveTimeMatrix() {
        return contractDriveTimeMatrix;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    private void fillDriveTimeMatrix() {
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
                contractDriveTimeMatrix.add(new ContractToContract(cA, cB, driveTime));
            }
        }
    }
}
