package money;

import java.util.Calendar;

public class Spending implements MoneyFlow {
    private double amount;
    private Calendar date;
    private String purposeOfSpending;

    public Calendar getDate() {
        return date;
    }

    public String getPurposeOfSpending() {
        return purposeOfSpending;
    }

    public double getAmount() {
        return amount;

    }

    public void setAmount(double amount, String purposeOfSpending) {
        if (amount > 0) {
            throw new IllegalArgumentException("Spendings can not have positive amount!");
        }
        this.amount = amount;
        this.purposeOfSpending = purposeOfSpending;
        date = Calendar.getInstance();
    }
}
