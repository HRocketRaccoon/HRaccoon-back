package org.finalpjt.hraccoon.domain.attendance.data.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttendacneMonthPercentResponseDTO {

    private Integer totalWorkHours;
    private double formattedPercent;

    public void of(Integer totalWorkHours, double formattedPercent) {
        this.totalWorkHours = totalWorkHours;
        this.formattedPercent = formattedPercent;
    }

}
