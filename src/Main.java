
import deposit.Deposit;
import money.MoneyFlow;
import money.MoneyFlowFactory;
import money.MoneyFlowType;

public class Main {

    public static void main(String[] args) {
        MoneyFlowFactory factory = MoneyFlowFactory.getInstance();
        MoneyFlow income = factory.create(MoneyFlowType.IncomeType);
        income.setAmount(5000, "Salary");

        MoneyFlow spending = factory.create(MoneyFlowType.SpendingType);
        spending.setAmount(-3000, "Tools");

        Deposit deposit = Deposit.getInstance();
        deposit.addMoneyFlow(income);
        deposit.addMoneyFlow(spending);

        System.out.println(deposit.getCurrentAmount());
    }
}
