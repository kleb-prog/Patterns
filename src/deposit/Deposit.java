package deposit;

import money.MoneyFlow;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Deposit implements RemoteDeposit {

    private static volatile Deposit instance;
    private List<MoneyFlow> moneyFlowList;

    private Deposit() {
        moneyFlowList = new ArrayList<>();
    }

    public boolean addMoneyFlow(MoneyFlow moneyFlow) {
        System.out.println("MoneyFlow added " + moneyFlow.getDescription());
        return moneyFlowList.add(moneyFlow);
    }

    public double getCurrentAmount() {
        System.out.println("Returned current amount");
        return moneyFlowList.stream().mapToDouble(MoneyFlow::getAmount).sum();
    }

    private static Deposit getInstance() {
        Deposit localInstance = instance;
        if (localInstance == null) {
            synchronized (Deposit.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Deposit();
                }
            }
        }
        return localInstance;
    }

    public static void main(String[] args) {
        Deposit deposit = new Deposit();

        try {
            RemoteDeposit stub = (RemoteDeposit)UnicastRemoteObject.exportObject(deposit, 0);

            Registry registry = LocateRegistry.createRegistry(12345);
            registry.bind("depositRemote", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
