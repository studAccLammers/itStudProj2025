package org.application.services.testdata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.application.dtos.Contract;
import org.application.dtos.Employee;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TestDataLoader {
    private static TestDataLoader INSTANCE;

    private final List<Contract> contracts = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();

    public static TestDataLoader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestDataLoader();
        }
        return INSTANCE;
    }

    public void initialize(String directoryPath) throws IOException {
        loadFromDirectory(directoryPath);
    }


    private TestDataLoader() {
    }

    public void loadFromDirectory(String directoryPath) throws IOException {
        File dir = new File(directoryPath);
        if (!dir.isDirectory()) {
            throw new IOException("Pfad ist kein Verzeichnis: " + directoryPath);
        }

        File employeesFile = new File(dir, "employees.json");
        File contractsFile = new File(dir, "contracts.json");

        if (!employeesFile.exists()) {
            throw new IOException("employees.json nicht gefunden im Ordner: " + directoryPath);
        }
        if (!contractsFile.exists()) {
            throw new IOException("contracts.json nicht gefunden im Ordner: " + directoryPath);
        }

        Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

        try (FileReader reader = new FileReader(employeesFile)) {

            Type employeeListType = new TypeToken<List<Employee>>() {
            }.getType();
            List<Employee> loadedEmployees = gson.fromJson(reader, employeeListType);

            this.employees.clear();
            if (loadedEmployees != null) {
                this.employees.addAll(loadedEmployees);
            }
        }

        try (FileReader reader = new FileReader(contractsFile)) {
            Type contractListType = new TypeToken<List<Contract>>() {
            }.getType();
            List<Contract> loadedContracts = gson.fromJson(reader, contractListType);

            this.contracts.clear();
            if (loadedContracts != null) {
                this.contracts.addAll(loadedContracts);
            }
        }
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
