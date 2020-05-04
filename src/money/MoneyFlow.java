package money;

import java.util.Calendar;
import java.util.UUID;

public interface MoneyFlow {
    void setAmount(double amount, String description);
    void setAmount(UUID uuid, double amount, String description, Calendar date);
    double getAmount();
    String getDescription();
    public UUID getUuid();
    public Calendar getDate();
    public String getType();
}
