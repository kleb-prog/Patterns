package money;

public interface Factory<T> {
    T create(MoneyFlowType moneyFlowType);
}
