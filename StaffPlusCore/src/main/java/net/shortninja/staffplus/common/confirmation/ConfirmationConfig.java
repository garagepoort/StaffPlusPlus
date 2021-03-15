package net.shortninja.staffplus.common.confirmation;

import net.shortninja.staffplus.staff.mode.item.ConfirmationType;

import java.util.Map;

public class ConfirmationConfig {

    private ConfirmationType confirmationType;
    private String confirmationMessage;

    public ConfirmationConfig(ConfirmationType confirmationType, String confirmationMessage) {
        this.confirmationType = confirmationType;
        this.confirmationMessage = confirmationMessage;
    }

    public ConfirmationType getConfirmationType() {
        return confirmationType;
    }

    public String getConfirmationMessage() {
        return confirmationMessage;
    }

    public void setPlaceholders(Map<String, String> placeholders) {
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            confirmationMessage = confirmationMessage.replace(entry.getKey(), entry.getValue());
        }
    }
}
