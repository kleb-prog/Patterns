package money;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

public class Income implements MoneyFlow, Serializable {

    private UUID uuid;
    private double amount;
    private Calendar date;
    private String description;
    private String type;

    public Income() {
        uuid = UUID.randomUUID();
        type = MoneyFlowType.IncomeType.toString();
    }

    public UUID getUuid() {
        return uuid;
    }

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

    @Override
    public void setAmount(UUID uuid, double amount, String description, Calendar date) {
        this.uuid = uuid;
        if (amount < 0) {
            throw new IllegalArgumentException("Income can not be lower than zero!");
        }
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Calendar getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }
}
