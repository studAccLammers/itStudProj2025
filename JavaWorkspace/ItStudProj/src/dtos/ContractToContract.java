package dtos;

public class ContractToContract {
    private final Contract contractA;
    private final Contract ContractB;
    private final double driveTimeInHours;

    public ContractToContract(Contract contractA, Contract contractB, double driveTimeInHours) {
        this.contractA = contractA;
        ContractB = contractB;
        this.driveTimeInHours = driveTimeInHours;
    }

    public Contract getContractA() {
        return contractA;
    }

    public Contract getContractB() {
        return ContractB;
    }

    public double getDriveTimeInHours() {
        return driveTimeInHours;
    }
}
