package org.finalpjt.hraccoon.domain.attendance.data.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttendacneMonthPercentResponseDTO {

    private int totalWorkHours;
    private double formattedPercent;

    // 얘는 그냥 함수(타입x, 클래스명과 같아야 기본생성자)
    public void of(int totalWorkHours, double formattedPercent) {
        this.totalWorkHours = totalWorkHours;
        this.formattedPercent = formattedPercent;
    }

}
