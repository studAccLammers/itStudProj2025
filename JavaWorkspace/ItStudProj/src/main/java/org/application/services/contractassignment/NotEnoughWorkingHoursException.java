package org.application.services.contractassignment;

import org.application.dtos.ContractConfirmation;

import java.util.List;

public class NotEnoughWorkingHoursException extends IllegalStateException {
    private List<ContractConfirmation> contractConfirmations;

    public NotEnoughWorkingHoursException(List<ContractConfirmation> contractConfirmation) {
        super("Not enough contracts available to ensure minimum employee working hours");
        this.contractConfirmations = contractConfirmation;
    }

    public List<ContractConfirmation> getContractConfirmations() {
        return contractConfirmations;
    }
}
