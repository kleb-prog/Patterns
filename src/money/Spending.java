package money;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

public class Spending implements MoneyFlow, Serializable {

    private UUID uuid;
    private double amount;
    private Calendar date;
    private String description;
    private String type;

    public Spending() {
        uuid = UUID.randomUUID();
        type = MoneyFlowType.SpendingType.toString();
    }

    public UUID getUuid() {
        return uuid;
    }

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
        this.date = Calendar.getInstance();
    }

    @Override
    public void setAmount(UUID uuid, double amount, String description, Calendar date) {
        this.uuid = uuid;
        if (amount > 0) {
            throw new IllegalArgumentException("Spendings can not have positive amount!");
        }
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spending spending = (Spending) o;
        return uuid.equals(spending.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
