package deposit;

import money.MoneyFlow;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Deposit implements RemoteDeposit, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String FILE_NAME_TO_SAVE = "localSave.txt";

    private static volatile Deposit instance;
    private List<MoneyFlow> moneyFlowList;

    private Memento memento;

    private Deposit() {
        moneyFlowList = new ArrayList<>();
        memento = new Memento();
        deserialize();
    }

    @Override
    public boolean addMoneyFlow(MoneyFlow moneyFlow) {
        System.out.println("MoneyFlow added: " + moneyFlow.getDescription());
        memento.saveState();
        return moneyFlowList.add(moneyFlow);
    }

    @Override
    public double getCurrentAmount() {
        System.out.println("Returned current amount");
        return moneyFlowList.stream().mapToDouble(MoneyFlow::getAmount).sum();
    }

    @Override
    public void undoLastChange() {
        memento.restore();
    }

    @Override
    public void serialSave() {
        serialize();
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
        Deposit deposit = getInstance();

        try {
            RemoteDeposit stub = (RemoteDeposit) UnicastRemoteObject.exportObject(deposit, 0);

            Registry registry = LocateRegistry.createRegistry(12345);
            registry.bind("depositRemote", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    private class Memento implements Serializable {
        private static final long serialVersionUID = 1L;

        private List<MoneyFlow> historicalMoneyFlowList;

        private Memento() {
            saveState();
        }

        private void saveState() {
            historicalMoneyFlowList = new ArrayList<>(moneyFlowList);
        }

        private void restore() {
            List<MoneyFlow> temp = moneyFlowList;
            moneyFlowList = historicalMoneyFlowList;
            historicalMoneyFlowList = temp;
            System.out.println("State restored to previous");
        }
    }

    private void serialize() {
        Path pathToFile = Paths.get(FILE_NAME_TO_SAVE);
        try {
            if (Files.exists(pathToFile)) {
                Files.delete(pathToFile);
            }
            Files.createFile(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (FileOutputStream fileOutputStream = new FileOutputStream(pathToFile.toString());
             ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream)) {
            oos.writeObject(getInstance());
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deserialize() {
        Path pathToFile = Paths.get(FILE_NAME_TO_SAVE);
        if (!Files.exists(pathToFile)) {
            return;
        }

        try (FileInputStream fileInputStream = new FileInputStream(pathToFile.toString());
             ObjectInputStream ois = new ObjectInputStream(fileInputStream)) {
            Deposit des = (Deposit) ois.readObject();
            this.moneyFlowList = des.moneyFlowList;
            memento.saveState();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}