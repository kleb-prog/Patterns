package money;

import java.io.Serializable;
import java.util.Calendar;

public class Income implements MoneyFlow, Serializable {

    private double amount;
    private Calendar date;
    private String description;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount, String description) {
        if (amount < 0) {
            throw new IllegalArgumentException("Income can not be lower than zero!");
        }
        this.amount = amount;
        this.description = description;
        this.date = Calendar.getInstance();
    }

    public Calendar getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
