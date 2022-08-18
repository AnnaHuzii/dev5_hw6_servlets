package db.project;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ProjectDaoService {
    private final PreparedStatement getAllNames;
    private final PreparedStatement getCompanyNameByProjectName;
    private final PreparedStatement getCustomerNameByProjectName;
    private final PreparedStatement getCostDate;
    private final PreparedStatement getListDevelopers;
    private final PreparedStatement getQuantityDevelopersByProjectName;
    private final PreparedStatement getBudgetByProjectName;
    private final PreparedStatement getIdProjectByName;


    public ProjectDaoService(Connection connection) throws SQLException {
        PreparedStatement getAllInfoSt = connection.prepareStatement(
                "SELECT * FROM project"
        );
        try (ResultSet resultSet = getAllInfoSt.executeQuery()) {
            while (resultSet.next()) {
                Project project = new Project();
                project.setProjectId(resultSet.getLong("id"));
                project.setStartDate(resultSet.getDate("start_date"));
                project.setProjectName(resultSet.getString("name"));
                project.setDescription(resultSet.getString("description"));
                project.setCompanyId(resultSet.getLong("company_id"));
                project.setCustomerId(resultSet.getLong("customer_id"));
                project.setCost(resultSet.getInt("cost"));

                List<Project> project1 = new ArrayList<>();
                project1.add(project);
            }
        }

        getAllNames = connection.prepareStatement(
                " SELECT id, name, description, start_date FROM project"
        );


        getCompanyNameByProjectName = connection.prepareStatement(
                "SELECT companies.name FROM project " +
                    "JOIN companies ON project.company_id = companies.id " +
                    "WHERE  project.name LIKE ?"
        );

        getCustomerNameByProjectName = connection.prepareStatement(
                "SELECT customers.name FROM project " +
                    "JOIN customers ON project.customer_id = customers.id " +
                    "WHERE  project.name LIKE ?"
        );

        getCostDate = connection.prepareStatement(
                " SELECT cost, start_date, description FROM project " +
                    "WHERE  project.name LIKE ?"
        );

        getListDevelopers = connection.prepareStatement(
                "SELECT full_name, birth_date FROM project " +
                "JOIN projects_developers ON project.id = projects_developers.project_id " +
                "JOIN developers ON projects_developers.developer_id = developers.id " +
                "WHERE project.name LIKE ?"
        );

        getQuantityDevelopersByProjectName = connection.prepareStatement(
                "SELECT COUNT(developer_id) FROM project " +
                    "JOIN projects_developers ON project.id = projects_developers.project_id " +
                    "WHERE project.name  LIKE  ?"
        );

        getBudgetByProjectName = connection.prepareStatement(
                "SELECT SUM(salary) FROM project " +
                     "JOIN projects_developers ON project.id = projects_developers.project_id " +
                     "JOIN developers ON projects_developers.developer_id = developers.id " +
                     "WHERE project.name LIKE ?"
        );

        getIdProjectByName = connection.prepareStatement(
                "SELECT id FROM project " +
                    "WHERE project.name LIKE ?"
        );
    }

    public String getAllNames() throws SQLException {
        StringJoiner result = new StringJoiner("<br>");
        try (ResultSet resultSet = getAllNames.executeQuery()) {
            while (resultSet.next()) {
                long projectID = resultSet.getLong("id");
                String projectName = resultSet.getString("name");
                result.add(projectID + ". " + projectName);
            }
        }
        return result.toString();
    }

    public String getInfoByName(String name) throws SQLException {
        StringJoiner description = new StringJoiner("<br>");
        getCustomerNameByProjectName.setString(1,  name);

        try (ResultSet rs1 = getCustomerNameByProjectName.executeQuery()) {
            while (rs1.next()) {
                description.add("Ordered by the customer: " + rs1.getString("name"));
            }
        }
        getCompanyNameByProjectName.setString(1, name);
        try (ResultSet rs2 = getCompanyNameByProjectName.executeQuery()) {
            while (rs2.next()) {
                description.add("Developed by: " + rs2.getString("name"));
            }
        }
        getCostDate.setString(1, name);
        try (ResultSet resultSet = getCostDate.executeQuery()) {
            while (resultSet.next()) {
                description.add("Project description: " + resultSet.getString("description"));
                description.add("Has a budget - " + resultSet.getInt("cost"));
                description.add("Started by: " + LocalDate.parse(resultSet.getString("start_date")));
            }
        }

        return description.toString();
    }

    public String getListDevelopers (String name) throws SQLException {
        StringJoiner result = new StringJoiner("<br>");
        getListDevelopers.setString(1, name);
        try (ResultSet rs1 = getListDevelopers.executeQuery()) {
            while (rs1.next()) {
               result.add(rs1.getString("full_name") + ", Birth date: " + rs1.getDate("birth_date") );
            }
        }
        return result.toString();
    }


    public void getQuantityDevelopers (String name) throws SQLException {
        getQuantityDevelopersByProjectName.setString(1, name);
        try (ResultSet rs1 = getQuantityDevelopersByProjectName.executeQuery()) {
            while (rs1.next()) {
                System.out.println("\t\tВ даном проекті задіяно " +  rs1.getInt("COUNT(developer_id)") + " розробник(а)");
            }
        }
    }

    public float getBudgetByProjectName (String name) throws SQLException {
        float projectCost = 0f;
        getBudgetByProjectName.setString(1, name);
        try (ResultSet rs1 = getBudgetByProjectName.executeQuery()) {
            while (rs1.next()) {
                projectCost = rs1.getFloat("SUM(salary)");
            }
        }
return projectCost;
    }
    public String getProjectsListInSpecialFormat () throws SQLException {
        StringJoiner result = new StringJoiner("<br>");
        try (ResultSet rs = getAllNames.executeQuery()) {

            while (rs.next()) {
                StringJoiner list = null;
                list = new StringJoiner(", ");
                Date projectDate = Date.valueOf(LocalDate.parse(rs.getString("start_date")));
                list.add("Project start date: " + projectDate);
                String projectName = rs.getString("name");
                list.add("Project name: " + projectName);
                getQuantityDevelopersByProjectName.setString(1, projectName);
                try (ResultSet resultSet1 = getQuantityDevelopersByProjectName.executeQuery()) {
                    int quantityDevelopers = 0;
                    while (resultSet1.next()) {
                        quantityDevelopers = resultSet1.getInt(1);
                    }
                    list.add("quantity developers = " + quantityDevelopers);
                }
                result.add(list.toString());
            }
        }
        return result.toString();
    }

    public long getIdProjectByName(String name) throws SQLException {
        getIdProjectByName.setString(1, "%" + name + "%");
        int result;
        try (ResultSet rs = getIdProjectByName.executeQuery()) {
            rs.next();
            result = rs.getInt("id");
        }
        return result;
    }
}
