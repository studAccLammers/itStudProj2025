package dtos;

import java.util.ArrayList;
import java.util.List;

public class TestDataHolder {

    private TestDataHolder() {
    }

    public static List<Contract> getContracts() {
        List<Contract> contracts = new ArrayList<>();

        contracts.add(new Contract(1, 2, List.of(Skill.HEAVY_DELIVERY, Skill.ELECTRONICS), Priority.HIGH));
        contracts.add(new Contract(2, 9, List.of(Skill.REPAIR), Priority.HIGH));
        contracts.add(new Contract(3, 3, List.of(Skill.INSTALLATION), Priority.HIGHEST));
        contracts.add(new Contract(4, 5, List.of(Skill.HEAVY_DELIVERY), Priority.HIGHEST));
        contracts.add(new Contract(5, 4, new ArrayList<>(), Priority.NORMAL));
        contracts.add(new Contract(6, 7, new ArrayList<>(), Priority.HIGH));
        contracts.add(new Contract(7, 6, List.of(Skill.ELECTRONICS), Priority.NORMAL));
        contracts.add(new Contract(8, 5, List.of(Skill.INSTALLATION), Priority.HIGHEST));
        contracts.add(new Contract(9, 8, List.of(Skill.HEAVY_DELIVERY), Priority.NORMAL));
        contracts.add(new Contract(10, 9, List.of(Skill.HEAVY_DELIVERY), Priority.HIGH));
        contracts.add(new Contract(11, 6, List.of(Skill.REPAIR), Priority.HIGH));
        contracts.add(new Contract(12, 9, new ArrayList<>(), Priority.HIGHEST));
        contracts.add(new Contract(13, 5, List.of(Skill.INSTALLATION), Priority.HIGH));
        contracts.add(new Contract(14, 7, List.of(Skill.HEAVY_DELIVERY), Priority.NORMAL));
        contracts.add(new Contract(15, 6, List.of(Skill.ELECTRONICS), Priority.HIGH));

        return contracts;
    }

    public static List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee(1, "Employee 1", 4, 10, 40, List.of(Skill.REPAIR, Skill.HEAVY_DELIVERY)));
        employees.add(new Employee(2, "Employee 2", 0, 10, 40, List.of(Skill.HEAVY_DELIVERY, Skill.ELECTRONICS, Skill.INSTALLATION)));
        employees.add(new Employee(3, "Employee 3", 0, 10, 40, List.of()));

        return employees;
    }
}
