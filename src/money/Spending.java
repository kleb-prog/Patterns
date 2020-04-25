package money;

import java.io.Serializable;
import java.util.Calendar;

public class Spending implements MoneyFlow, Serializable {
    private double amount;
    private Calendar date;
    private String description;

    public Calendar getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;

    }

    public void setAmount(double amount, String description) {
        if (amount > 0) {
            throw new IllegalArgumentException("Spendings can not have positive amount!");
        }
        this.amount = amount;
        this.description = description;
        date = Calendar.getInstance();
    }
}
