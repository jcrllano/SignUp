package testinput.login.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table (name = "time")
public class Time {
    @Id 
    @Column(name="day_of_week", insertable=true, updatable=true, unique=true, nullable=false)
    private String day_of_week;
    private String month;
    private String time;
    private String time2;
    private String time3;
    private String time4;
    private String time5;
    private String time6;
    private String time7;
    private String time8;
    private String time9;
    private String time10;
}
