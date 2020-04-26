package money;

import java.util.Calendar;

public class Income implements MoneyFlow {

    private double amount;
    private Calendar date;
    private String sourceOfIncome;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount, String sourceOfIncome) {
        if (amount < 0) {
            throw new IllegalArgumentException("Income can not be lower than zero!");
        }
        this.amount = amount;
        this.sourceOfIncome = sourceOfIncome;
        this.date = Calendar.getInstance();
    }

    public Calendar getDate() {
        return date;
    }

    public String getSourceOfIncome() {
        return sourceOfIncome;
    }
}
