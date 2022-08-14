package db.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoService {
    public static List<Company> companies = new ArrayList<>();

    private final PreparedStatement getQuantityEmployee;
    private final PreparedStatement getIdCompanyByName;
    private final PreparedStatement getCompanyProjects;
    private final PreparedStatement selectMaxId;
    private final PreparedStatement addCompany;
    private final PreparedStatement existsById;


    public CompanyDaoService(Connection connection) throws SQLException {
        PreparedStatement getAllInfoSt = connection.prepareStatement(
                "SELECT * FROM companies"
        );
        try (ResultSet resultSet = getAllInfoSt.executeQuery()) {
            while (resultSet.next()) {
                Company company = new Company();
                company.setCompanyId(resultSet.getLong("id"));
                if (resultSet.getString("name") != null) {
                    company.setCompanyName(resultSet.getString("name"));
                }
                company.setDescription(resultSet.getString("description"));
                companies.add(company);
            }
        }

        getQuantityEmployee = connection.prepareStatement(
                "SELECT COUNT(companies.id) FROM companies " +
                    "WHERE companies.name LIKE ?"
        );


        getIdCompanyByName = connection.prepareStatement(
                "SELECT id FROM companies " +
                    "WHERE companies.name LIKE ?"
        );

        getCompanyProjects = connection.prepareStatement(
               "SELECT project.name FROM project " +
                   "INNER JOIN companies ON project.company_id = companies.id " +
                   "WHERE companies.name LIKE ?"
        );

        selectMaxId = connection.prepareStatement(
                "SELECT max(id) AS maxId FROM companies"
        );

        addCompany = connection.prepareStatement(
                "INSERT INTO companies  VALUES ( ?, ?, ?)");

        existsById = connection.prepareStatement(
                "SELECT COUNT(*) > 0 AS companyExists FROM companies WHERE id = ?"
        );
    }

    public void getAllNames() throws SQLException {
        System.out.println("Список всіх IT компаній:");
        for (Company company : companies) {
            getQuantityEmployee.setString(1, "%" + company.getCompanyName() + "%");
            System.out.println("\t" + company.getCompanyId() + ". " + company.getCompanyName() + "; Опис - " + company.getDescription());
        }
    }

    public long getIdCompanyByName(String name) throws SQLException {
        getIdCompanyByName.setString(1, "%" + name + "%");
        int result = 0;
        try (ResultSet resultSet = getIdCompanyByName.executeQuery()) {
            while (resultSet.next()) {
                result = resultSet.getInt("id");
            }
        }
        return result;
    }

    public ArrayList<String> getCompanyProjects (String name) throws SQLException {
        ArrayList<String> projectsList = new ArrayList<>();
        getCompanyProjects.setString(1, "%" + name + "%");
        try (ResultSet resultSet = getCompanyProjects.executeQuery()) {
            while (resultSet.next()) {
                projectsList.add(resultSet.getString("name"));
            }
        }
        return projectsList;
    }

    public void addCompany() throws SQLException {
        long newCompanyId;
        try(ResultSet resultSet = selectMaxId.executeQuery()) {
            resultSet.next();
            newCompanyId = resultSet.getLong("maxId");
        }
        newCompanyId++;

        String newCompanyName = "BI-DON";
        System.out.println("Назва компанії, що створюється: " + newCompanyName);
        String newCompanyDescription = "Development of computer games and mobile applications";
        System.out.println("Опис компанії " + newCompanyName + " - " + newCompanyDescription);
        addCompany.setLong(1, newCompanyId);
        addCompany.setString(2, newCompanyName);
        addCompany.setString(3, newCompanyDescription);
        Company company = new Company();

        company.setCompanyId(newCompanyId);
        company.setCompanyName(newCompanyName);
        company.setDescription(newCompanyDescription);

        addCompany.executeUpdate();

        if (existsCompany(newCompanyId)) {System.out.println("Компанія успішно добавленна");}
        else System.out.println("Винекла помилка. компанія не було добавлена");
    }

    public boolean existsCompany(long id) throws SQLException {
        existsById.setLong(1, id);
        try (ResultSet rs = existsById.executeQuery()) {
            rs.next();
            return rs.getBoolean("companyExists");
        }
    }


}
