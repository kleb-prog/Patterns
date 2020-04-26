package deposit;

import money.MoneyFlow;

import java.util.ArrayList;
import java.util.List;

public class Deposit {

    private static volatile Deposit instance;
    private List<MoneyFlow> moneyFlowList;

    private Deposit() {
        moneyFlowList = new ArrayList<>();
    }

    public boolean addMoneyFlow(MoneyFlow moneyFlow) {
        return moneyFlowList.add(moneyFlow);
    }

    public double getCurrentAmount() {
        return moneyFlowList.stream().mapToDouble(MoneyFlow::getAmount).sum();
    }

    public static Deposit getInstance() {
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
}
