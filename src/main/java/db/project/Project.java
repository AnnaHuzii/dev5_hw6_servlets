package db.project;

import lombok.Data;

import java.sql.Date;

@Data
public class Project {
    private long projectId;
    private Date startDate;
    private String projectName;
    private String description;
    private long companyId;
    private long customerId;
    public int cost;
}
