package org.finalpjt.hraccoon.domain.attendance.data.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttendacneMonthPercentResponseDTO {

    private int totalWorkHours;
    private double formattedPercent;

    public void of(int totalWorkHours, double formattedPercent) {
        this.totalWorkHours = totalWorkHours;
        this.formattedPercent = formattedPercent;
    }

}
