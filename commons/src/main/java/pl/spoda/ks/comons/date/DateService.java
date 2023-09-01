package pl.spoda.ks.comons.date;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class DateService {
    public static final ZoneId POLAND_TIMEZONE = ZoneId.of( "Europe/Warsaw" );

    public LocalDate getCurrentDate() {
        return LocalDate.now( POLAND_TIMEZONE );
    }

    public LocalDate dateOf(LocalDate date) {
        ZonedDateTime zonedDateTime = date.atStartOfDay(POLAND_TIMEZONE);
        return zonedDateTime.toLocalDate();
    }
}
