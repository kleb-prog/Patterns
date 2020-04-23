package money;

public class MoneyFlowFactory implements Factory<MoneyFlow> {

    private static volatile MoneyFlowFactory instance;

    private MoneyFlowFactory() {}

    @Override
    public MoneyFlow create(MoneyFlowType moneyFlowType) {
        switch (moneyFlowType) {
            case IncomeType: return new Income();
            case SpendingType: return new Spending();
            default: throw new IllegalStateException("Error while creation of money flow instance");
        }
    }

    public static MoneyFlowFactory getInstance() {
        MoneyFlowFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (MoneyFlowFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MoneyFlowFactory();
                }
            }
        }
        return localInstance;
    }
}
