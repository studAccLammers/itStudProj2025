package org.application.dtos;

import java.time.LocalDate;

public class ContractConfirmation {
    private final Contract contract;
    private final LocalDate date;

    public ContractConfirmation(Contract contract, LocalDate date) {
        this.contract = contract;
        this.date = date;
    }

    public Contract getContract() {
        return contract;
    }

    public LocalDate getDate() {
        return date;
    }
}
