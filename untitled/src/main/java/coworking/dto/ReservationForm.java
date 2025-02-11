package coworking.dto;

import java.time.LocalDate;

public class ReservationForm {
    private int workspaceId;
    private LocalDate startDate;
    private LocalDate endDate;

    public int getWorkspaceId() {
        return workspaceId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

}

