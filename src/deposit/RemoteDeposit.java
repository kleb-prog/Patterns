package deposit;

import money.MoneyFlow;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteDeposit extends Remote {
    boolean addMoneyFlow(MoneyFlow moneyFlow) throws RemoteException;
    double getCurrentAmount() throws RemoteException;
}
