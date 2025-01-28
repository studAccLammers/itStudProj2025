package org.application.dtos;

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
        contracts.add(new Contract(1, 2, List.of(Skill.HEAVY_DELIVERY, Skill.ELECTRONICS), Priority.HIGH, 52.3744, 9.7386)); // Hannover
        contracts.add(new Contract(2, 1, List.of(Skill.REPAIR), Priority.HIGH, 52.1100, 9.8100)); // Alfeld
        contracts.add(new Contract(3, 3, List.of(Skill.INSTALLATION), Priority.HIGHEST, 52.2639, 10.5268)); // Braunschweig
        contracts.add(new Contract(4, 2, List.of(Skill.HEAVY_DELIVERY), Priority.HIGHEST, 52.3759, 10.0986)); // Wolfsburg
        contracts.add(new Contract(5, 4, List.of(), Priority.NORMAL, 52.0393, 9.7537)); // Bad Gandersheim
        contracts.add(new Contract(6, 1, List.of(), Priority.HIGH, 52.3208, 9.8097)); // Lehrte
        contracts.add(new Contract(7, 4, List.of(Skill.ELECTRONICS), Priority.NORMAL, 52.3753, 9.7332)); // Garbsen
        contracts.add(new Contract(8, 1, List.of(Skill.INSTALLATION), Priority.HIGHEST, 52.4828, 9.7332)); // Langenhagen
        contracts.add(new Contract(9, 8, List.of(Skill.HEAVY_DELIVERY), Priority.NORMAL, 52.2773, 9.7414)); // Pattensen
        contracts.add(new Contract(10, 9, List.of(Skill.UNINSTALLATION), Priority.HIGH, 52.1500, 10.2000)); // Peine
        contracts.add(new Contract(11, 2, List.of(Skill.REPAIR), Priority.HIGH, 52.0833, 9.9167)); // Sarstedt
        contracts.add(new Contract(12, 9, List.of(), Priority.HIGHEST, 52.3837, 9.5511)); // Wunstorf
        contracts.add(new Contract(13, 4, List.of(Skill.INSTALLATION), Priority.HIGH, 52.2667, 10.5167)); // Gifhorn
        contracts.add(new Contract(14, 1, List.of(Skill.HEAVY_DELIVERY), Priority.NORMAL, 52.3667, 9.7167)); // Sehnde
        contracts.add(new Contract(15, 2, List.of(Skill.UNINSTALLATION), Priority.HIGH, 52.3186, 9.6967)); // Barsinghausen

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
