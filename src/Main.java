
import deposit.Deposit;
import deposit.RemoteDeposit;
import money.MoneyFlow;
import money.MoneyFlowFactory;
import money.MoneyFlowType;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) throws RemoteException {
        MoneyFlowFactory factory = MoneyFlowFactory.getInstance();
        MoneyFlow income = factory.create(MoneyFlowType.IncomeType);
        income.setAmount(5000, "Salary");

        MoneyFlow spending = factory.create(MoneyFlowType.SpendingType);
        spending.setAmount(-3000, "Tools");

        RemoteDeposit deposit = getRemoteDeposit();
        deposit.addMoneyFlow(income);
        deposit.addMoneyFlow(spending);

        System.out.println(deposit.getCurrentAmount());
    }

    private static RemoteDeposit getRemoteDeposit() {
        RemoteDeposit deposit = null;
        try {
            Registry registry = LocateRegistry.getRegistry(null, 12345);
            deposit = (RemoteDeposit) registry.lookup("depositRemote");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        return deposit;
    }
}
