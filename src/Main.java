
import deposit.RemoteDepositMapper;
import money.MoneyFlow;
import money.MoneyFlowFactory;
import money.MoneyFlowType;

import java.rmi.NotBoundException;
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

        RemoteDepositMapper deposit = getRemoteDeposit();
        if (deposit != null) {
            deposit.insert(income);
            deposit.insert(spending);

            spending.setAmount(-6000, "Testing!!");
            deposit.update(spending);

            MoneyFlow income2 = deposit.findByUUID(income.getUuid());
            System.out.println(income.equals(income2));
            deposit.delete(income);

            System.out.println(deposit.getCurrentAmount());
        }
    }

    private static RemoteDepositMapper getRemoteDeposit() {
        RemoteDepositMapper deposit = null;
        try {
            Registry registry = LocateRegistry.getRegistry(null, 12345);
            deposit = (RemoteDepositMapper) registry.lookup("depositRemote");
        } catch (RemoteException | NotBoundException e) {
            System.out.println("Error while getting \"depositRemote\"");
        }

        return deposit;
    }
}
