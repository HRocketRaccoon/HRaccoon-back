package org.finalpjt.hraccoon.domain.attendance.data.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttendacneWeekPercentResponseDTO {
    
    private double formattedPercent;
    private int totalWorkHours;

    public void of(int totalWorkHours, double formattedPercent) {
        this.totalWorkHours = totalWorkHours;
        this.formattedPercent = formattedPercent;
    }


}