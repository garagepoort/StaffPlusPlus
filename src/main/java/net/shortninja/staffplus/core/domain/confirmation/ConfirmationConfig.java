package net.shortninja.staffplus.core.domain.confirmation;

import net.shortninja.staffplus.core.domain.staff.mode.item.ConfirmationType;

public class ConfirmationConfig {

    private final ConfirmationType confirmationType;
    private final String confirmationMessage;

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

}
