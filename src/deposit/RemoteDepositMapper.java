package deposit;

import money.MoneyFlow;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public interface RemoteDepositMapper extends Remote {
    double getCurrentAmount() throws RemoteException;
    void insert(MoneyFlow moneyFlow) throws RemoteException;
    void update(MoneyFlow moneyFlow) throws RemoteException;
    void delete(MoneyFlow moneyFlow) throws RemoteException;
    MoneyFlow findByUUID(UUID uuid) throws RemoteException;
    List<MoneyFlow> findAll() throws RemoteException;
    void closeConnection() throws RemoteException;
}
