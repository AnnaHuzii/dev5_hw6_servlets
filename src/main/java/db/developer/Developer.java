package db.developer;

import lombok.Data;

import java.sql.Date;


@Data
public class Developer {
    private long developerId;
    private String fullName;
    private Date birthDate;
    private Sex sex;
    private  String email;
    private  String skype;
    private  float salary;
}