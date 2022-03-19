package deposit;

import DB.DBConnector;
import DB.DBCreator;
import money.MoneyFlow;
import money.MoneyFlowFactory;
import money.MoneyFlowType;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Deposit implements RemoteDepositMapper {

    private static volatile Deposit instance;
    private Connection connection;

    private Map<String, MoneyFlow> moneyFlowIdentity;

    private Deposit() {
        moneyFlowIdentity = new HashMap<>();
        connection = DBConnector.getConnection();
        DBCreator.createTable(connection);
    }

    @Override
    public double getCurrentAmount() throws RemoteException{
        System.out.println("Returned current amount");
        return findAll().stream().mapToDouble(MoneyFlow::getAmount).sum();
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
            RemoteDepositMapper stub = (RemoteDepositMapper) UnicastRemoteObject.exportObject(deposit, 0);

            Registry registry = LocateRegistry.createRegistry(12345);
            registry.bind("depositRemote", stub);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(MoneyFlow moneyFlow) {
        String sql = "INSERT into money_flow values(?, ?, ?, ?, ?);";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1, moneyFlow.getUuid());
            statement.setDouble(2, moneyFlow.getAmount());
            statement.setString(3, moneyFlow.getDescription());
            statement.setDate(4, new Date(moneyFlow.getDate().getTime().getTime()));
            statement.setString(5, moneyFlow.getType());
            statement.executeUpdate();

            moneyFlowIdentity.put(moneyFlow.getUuid().toString(), moneyFlow);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(MoneyFlow moneyFlow) {
        String sql = "UPDATE money_flow SET amount = ?, description = ?, date = ? where uuid = ?;";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, moneyFlow.getAmount());
            statement.setString(2, moneyFlow.getDescription());
            statement.setDate(3, new Date(moneyFlow.getDate().getTime().getTime()));
            statement.setObject(4, moneyFlow.getUuid());
            statement.executeUpdate();

            moneyFlowIdentity.put(moneyFlow.getUuid().toString(), moneyFlow);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(MoneyFlow moneyFlow) {
        String sql = "DELETE from money_flow where uuid = ?;";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1, moneyFlow.getUuid());
            statement.executeUpdate();

            moneyFlowIdentity.remove(moneyFlow.getUuid().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MoneyFlow findByUUID(UUID uuid) throws RemoteException {
        MoneyFlow cached = moneyFlowIdentity.get(uuid.toString());
        if (cached != null) {
            return cached;
        }

        String sql = "select * from money_flow where uuid = ?;";
        PreparedStatement statement = null;
        MoneyFlow moneyFlow = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setObject(1, uuid);
            ResultSet rs = statement.executeQuery();

            if (rs != null) {
                String type = rs.getString("type");
                moneyFlow = MoneyFlowFactory.getInstance().create(MoneyFlowType.valueOf(type));
                if (moneyFlow != null) {
                    Calendar date = Calendar.getInstance();
                    date.setTime(rs.getDate(4));
                    moneyFlow.setAmount(uuid, rs.getDouble(2), rs.getString(3), date);
                }
            }
            return moneyFlow;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<MoneyFlow> findAll() throws RemoteException {
        String sql = "select * from money_flow;";
        PreparedStatement statement = null;
        List<MoneyFlow> moneyFlowList = new ArrayList<>();
        try {
            statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                MoneyFlow moneyFlow = MoneyFlowFactory.getInstance().create(MoneyFlowType.valueOf(type));
                if (moneyFlow != null) {
                    UUID uuid = (UUID) rs.getObject(1);
                    Calendar date = Calendar.getInstance();
                    date.setTime(rs.getDate(4));
                    moneyFlow.setAmount(uuid, rs.getDouble(2), rs.getString(3), date);
                    moneyFlowList.add(moneyFlow);
                    moneyFlowIdentity.put(uuid.toString(), moneyFlow);
                }
            }
            return moneyFlowList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void closeConnection() throws RemoteException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}