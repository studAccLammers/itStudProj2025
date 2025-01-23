package dtos;

import java.util.ArrayList;
import java.util.List;

public class TestDataHolder {
    private static TestDataHolder INSTANCE;
    private final List<Contract> contracts = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();

    public static TestDataHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestDataHolder();
        }

        return INSTANCE;
    }

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
        employees.add(new Employee(1, "Employee 1", 0, 10, 40, List.of(Skill.REPAIR, Skill.HEAVY_DELIVERY)));
        employees.add(new Employee(2, "Employee 2", 0, 10, 40, List.of(Skill.HEAVY_DELIVERY, Skill.ELECTRONICS, Skill.INSTALLATION)));
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
