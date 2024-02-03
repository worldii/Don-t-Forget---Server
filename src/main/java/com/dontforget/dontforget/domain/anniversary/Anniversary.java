package com.dontforget.dontforget.domain.anniversary;

import com.dontforget.dontforget.common.CalendarType;
import com.dontforget.dontforget.common.CardType;
import com.dontforget.dontforget.domain.anniversary.service.CalendarCalculator;
import com.dontforget.dontforget.domain.notice.Notice;
import com.dontforget.dontforget.domain.notice.NoticeType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Anniversary {

    private Long id;

    private String title;

    private String content;

    private String deviceUuid;

    private LocalDate lunarDate;

    private LocalDate solarDate;

    private List<Notice> notices = new ArrayList<>();

    private CardType cardType;

    public Anniversary(Long id, String title, String content, String deviceUuid,
        LocalDate lunarDate, LocalDate solarDate, List<Notice> notices, CardType cardType
    ) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.deviceUuid = deviceUuid;
        this.lunarDate = lunarDate;
        this.solarDate = solarDate;
        this.notices = notices;
        this.cardType = cardType;
    }

    public Anniversary(
        String title, String content, String deviceUuid,
        LocalDate lunarDate, LocalDate solarDate,
        List<Notice> notices, CardType cardType
    ) {
        this(null, title, content, deviceUuid, lunarDate, solarDate, notices, cardType);
    }

    public static Anniversary create(
        String deviceUuid, String title, LocalDate date,
        String content, CalendarType type, List<NoticeType> alarmSchedule,
        CardType cardType,
        CalendarCalculator calendarCalculator
    ) {
        return new Anniversary(
            title,
            content,
            deviceUuid,
            calendarCalculator.calculateLunarDate(date, type),
            calendarCalculator.calculateSolarDate(date, type),
            calculateNotice(alarmSchedule),
            cardType
        );
    }

    private static List<Notice> calculateNotice(final List<NoticeType> alarmSchedule) {
        return alarmSchedule.stream()
            .map(it -> new Notice(null, it))
            .toList();
    }

    public void update(String title, LocalDate date, CalendarType type,
        List<NoticeType> noticeTypes, String content, CalendarCalculator calendarCalculator
    ) {
        this.title = title;
        this.lunarDate = calendarCalculator.calculateLunarDate(date, type);
        this.solarDate = calendarCalculator.calculateSolarDate(date, type);
        this.notices = calculateNotice(noticeTypes);
        this.content = content;
    }
}
