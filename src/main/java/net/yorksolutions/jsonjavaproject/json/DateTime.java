package net.yorksolutions.jsonjavaproject.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateTime {
    @JsonProperty
    final String date;

    @JsonProperty
    final String time;

    Date dateTime;

    public DateTime(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public DateTime() {
        this.dateTime = new Date();
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(this.dateTime);
        this.time = new SimpleDateFormat("HH-mm-ss").format(this.dateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTime dateTime1 = (DateTime) o;
        return Objects.equals(date, dateTime1.date) && Objects.equals(time, dateTime1.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, dateTime);
    }
}
