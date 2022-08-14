package db.project;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public void getAllNames() throws SQLException {
        System.out.println("Список всіх  проектів :");
        try (ResultSet resultSet = getAllNames.executeQuery()) {
            while (resultSet.next()) {
                long projectID = resultSet.getLong("id");
                String projectName = resultSet.getString("name");
                System.out.println("\t" + projectID + ". " + projectName);
            }
        }
    }


    public void getInfoByName(String name) throws SQLException {
        getCustomerNameByProjectName.setString(1,  name);

        try (ResultSet rs1 = getCustomerNameByProjectName.executeQuery()) {
            while (rs1.next()) {
                System.out.println("\tЗамовлений замовником:" + rs1.getString("name"));
            }
        }
        System.out.print("\tРозробляється компанією: ");
        getCompanyNameByProjectName.setString(1, name);
        try (ResultSet rs2 = getCompanyNameByProjectName.executeQuery()) {
            while (rs2.next()) {
                System.out.println( rs2.getString("name"));
            }
        }
        getCostDate.setString(1, name);
        try (ResultSet resultSet = getCostDate.executeQuery()) {
            while (resultSet.next()) {
                System.out.println("\tОпис проекту: " + resultSet.getString("description"));
                System.out.println("\tМає б'юджет - " + resultSet.getInt("cost"));
                System.out.println("\tРозпочався: " + LocalDate.parse(resultSet.getString("start_date")));
            }
        }
    }

    public void getListDevelopers (String name) throws SQLException {
        getListDevelopers.setString(1, name);
        try (ResultSet rs1 = getListDevelopers.executeQuery()) {
            while (rs1.next()) {
                System.out.println("\t" + rs1.getString("full_name") + ", Дата народження: " + rs1.getDate("birth_date") );
            }
        }
    }


    public void getQuantityDevelopers (String name) throws SQLException {
        getQuantityDevelopersByProjectName.setString(1, name);
        try (ResultSet rs1 = getQuantityDevelopersByProjectName.executeQuery()) {
            while (rs1.next()) {
                System.out.println("\t\tВ даном проекті задіяно " +  rs1.getInt("COUNT(developer_id)") + " розробник(а)");
            }
        }
    }

    public void getBudgetByProjectName (String name) throws SQLException {
        getBudgetByProjectName.setString(1, name);
        try (ResultSet rs1 = getBudgetByProjectName.executeQuery()) {
            while (rs1.next()) {
                System.out.println("\tБ'юджет даного проекту - " +  rs1.getInt("SUM(salary)"));
            }
        }
    }

    public void getProjectsListInSpecialFormat () throws SQLException {
        try (ResultSet rs = getAllNames.executeQuery()) {
            while (rs.next()) {
                Date projectDate = Date.valueOf(LocalDate.parse(rs.getString("start_date")));
                System.out.print(projectDate);
                String projectName = rs.getString("name");
                System.out.print(", " + projectName);
                getQuantityDevelopersByProjectName.setString(1, projectName);
                try (ResultSet rs1 = getQuantityDevelopersByProjectName.executeQuery()) {
                    while (rs1.next()) {
                        System.out.println(", " + rs1.getInt("COUNT(developer_id)") + " розробник(а)");
                    }
                }
            }
        }
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
