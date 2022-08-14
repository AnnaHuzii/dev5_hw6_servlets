package db.developer;

import db.project.ProjectDaoService;
import db.skill.Industry;
import db.skill.Level;
import connection.Storage;
import db.skill.Skill;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class DeveloperDaoService {
    public static List<Developer> developers = new ArrayList<>();

    private final PreparedStatement getInfoByFullName;
    private final PreparedStatement getSkillsByFullName;
    private final PreparedStatement getProjectsByFullName;
    private final PreparedStatement getQuantityJavaDevelopers;
    private final PreparedStatement getListMiddleDevelopers;
    private final PreparedStatement selectMaxId;
    private final PreparedStatement addDeveloper;
    private final PreparedStatement addProjectDeveloper;
    private final PreparedStatement getIdSkillByIndustryAndSkillLevel;
    private final PreparedStatement addDeveloperSkill;
    private final PreparedStatement existsByIdSt;
    private final PreparedStatement getIdByFullName;
    private final PreparedStatement deleteDeveloperFromDevelopersById;

    public DeveloperDaoService(Connection connection) throws SQLException {
        PreparedStatement getAllInfoSt = connection.prepareStatement(
                "SELECT * FROM developers"
        );
        try (ResultSet resultSet = getAllInfoSt.executeQuery()) {

            while (resultSet.next()) {
                Developer developer = new Developer();
                developer.setDeveloperId(resultSet.getLong("id"));
                developer.setFullName(resultSet.getString("full_name"));
                developer.setBirthDate(resultSet.getDate("birth_date"));
                String sex = resultSet.getString("sex");
                Sex sexName = null;
                if (sex.equals(Sex.MALE.getSexName())) {
                    sexName = Sex.MALE;
                } else if (sex.equals(Sex.FEMALE.getSexName())) {
                    sexName = Sex.FEMALE;
                } else if (sex.equals(Sex.UNKNOWN.getSexName())) {
                    sexName = Sex.UNKNOWN;
                }
                developer.setSex(sexName);
                developer.setEmail(resultSet.getString("email"));
                developer.setSkype(resultSet.getString("skype"));
                developer.setSalary(resultSet.getFloat("salary"));
                developers.add(developer);
            }
        }

        getInfoByFullName = connection.prepareStatement(
                "SELECT *" +
                        "FROM developers WHERE full_name LIKE ? AND birth_date LIKE ?"
        );

        getSkillsByFullName = connection.prepareStatement(
                "SELECT industry, skill_level " +
                        "FROM developers " +
                        "JOIN developers_skills ON developers.id = developers_skills.developer_id " +
                        "JOIN skills ON developers_skills.skill_id = skills.id " +
                        "WHERE full_name LIKE ? AND birth_date LIKE ?"
        );

        getProjectsByFullName = connection.prepareStatement(
                "SELECT  name " +
                        "FROM developers " +
                        "JOIN projects_developers ON developers.id = projects_developers.developer_id " +
                        "JOIN project ON projects_developers.project_id = project.id " +
                        "WHERE full_name LIKE ? AND birth_date LIKE ?"
        );

        getQuantityJavaDevelopers = connection.prepareStatement(
                "SELECT COUNT(industry) AS quantityIndustryDevelopers " +
                        "FROM developers " +
                        "JOIN developers_skills ON developers.id = developers_skills.developer_id " +
                        "JOIN skills ON developers_skills.skill_id = skills.id " +
                        "WHERE industry = 'Java'"
        );

        getListMiddleDevelopers = connection.prepareStatement(
                "SELECT full_name, industry " +
                        "FROM developers " +
                        "JOIN developers_skills ON developers.id = developers_skills.developer_id " +
                        "JOIN skills ON developers_skills.skill_id = skills.id " +
                        "WHERE skill_level = 'middle'"
        );

        selectMaxId = connection.prepareStatement(
                "SELECT max(id) AS maxId FROM developers"
        );

        addDeveloper = connection.prepareStatement(
                "INSERT INTO developers  VALUES ( ?, ?, ?, ?, ?, ?, ?)");

        addProjectDeveloper = connection.prepareStatement(
                "INSERT INTO projects_developers  VALUES ( ?, ?)");

        getIdSkillByIndustryAndSkillLevel = connection.prepareStatement(
                "SELECT id FROM skills WHERE industry LIKE ? AND skill_level LIKE ?");

        addDeveloperSkill = connection.prepareStatement(
                "INSERT INTO developers_skills  VALUES ( ?, ?)");

        existsByIdSt = connection.prepareStatement(
                "SELECT count(*) > 0 AS developerExists FROM developers WHERE id = ?"
        );

        getIdByFullName = connection.prepareStatement(
                "SELECT id FROM developers WHERE full_name LIKE ? AND birth_date LIKE ?"
        );

        deleteDeveloperFromDevelopersById = connection.prepareStatement(
                "DELETE FROM developers WHERE id = ?"
        );

    }

    public String getAllFullName() {
        StringJoiner result = new StringJoiner("<br>");
        for (Developer developer : developers) {
            result.add(developer.getDeveloperId() + ". " + developer.getFullName() + " - " + developer.getBirthDate());
        }
        developers.clear();
        return result.toString();
    }

    public List<Developer> getInfoByFullName(String fullName, Date birthDate) throws SQLException {
        getInfoByFullName.setString(1, "%" + fullName + "%");
        getInfoByFullName.setDate(2, Date.valueOf(String.valueOf(birthDate)));

        List<Developer> result = new ArrayList<>();
        try (ResultSet resultSet = getInfoByFullName.executeQuery()) {
            while (resultSet.next()) {
                Developer developer = new Developer();
                developer.setFullName(resultSet.getString("full_name"));
                developer.setBirthDate(resultSet.getDate("birth_date"));
                String sex = resultSet.getString("sex");
                Sex sexName = null;
                if (sex.equals(Sex.MALE.getSexName())) {
                    sexName = Sex.MALE;
                } else if (sex.equals(Sex.FEMALE.getSexName())) {
                    sexName = Sex.FEMALE;
                } else if (sex.equals(Sex.UNKNOWN.getSexName())) {
                    sexName = Sex.UNKNOWN;
                }
                developer.setSex(sexName);
                developer.setEmail(resultSet.getString("email"));
                developer.setSkype(resultSet.getString("skype"));
                developer.setSalary(resultSet.getFloat("salary"));
                result.add(developer);
            }
        }
        return result;
    }

    public String getSkillsByFullName(String fullName, Date birthDate) throws SQLException {
        getSkillsByFullName.setString(1, "%" + fullName + "%");
        getSkillsByFullName.setDate(2, Date.valueOf(String.valueOf(birthDate)));

        StringJoiner result = new StringJoiner("; ");

        try (ResultSet resultSet = getSkillsByFullName.executeQuery()) {
            while (resultSet.next()) {
                Skill skill = new Skill();
                String industry = resultSet.getString("industry");
                Industry industryName = null;
                if (industry.equals(Industry.C_PLUS_PLUS.getIndustryName())) {
                    industryName = Industry.C_PLUS_PLUS;
                } else if (industry.equals(Industry.C_SHARP.getIndustryName())) {
                    industryName = Industry.C_SHARP;
                } else if (industry.equals(Industry.JS.getIndustryName())) {
                    industryName = Industry.JS;
                } else if (industry.equals(Industry.JAVA.getIndustryName())) {
                    industryName = Industry.JAVA;
                }
                String level = resultSet.getString("skill_level");
                Level levelName = null;
                if (level.equals(Level.JUNIOR.getLevelName())) {
                    levelName = Level.JUNIOR;
                } else if (level.equals(Level.MIDDLE.getLevelName())) {
                    levelName = Level.MIDDLE;
                } else if (level.equals(Level.SENIOR.getLevelName())) {
                    levelName = Level.SENIOR;
            }
            result.add(industryName + " - " + levelName);
        }
    }
        return result.toString();
}

    public String getProjectsByFullName(String fullName, Date birthDate) throws SQLException {
        getProjectsByFullName.setString(1, "%" + fullName + "%");
        getProjectsByFullName.setDate(2, Date.valueOf(String.valueOf(birthDate)));
        StringJoiner result = new StringJoiner("; ");
        try (ResultSet rs = getProjectsByFullName.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("name"));
            }
        }
        return result.toString();
    }

    public int getQuantityJavaDevelopers() throws SQLException {
        int count;
        try (ResultSet resultSet = getQuantityJavaDevelopers.executeQuery()) {
            resultSet.next();
            count = resultSet.getInt("quantityIndustryDevelopers");
        }
        return count;
    }

    public String getListMiddleDevelopers() throws SQLException {
        StringJoiner result = new StringJoiner("<br>");
        try (ResultSet resultSet = getListMiddleDevelopers.executeQuery()) {
            while (resultSet.next()) {
                result.add(resultSet.getString("full_name") + ",  programming language - " + resultSet.getString("industry"));
            }
        }
        return result.toString();
    }

    public long getIdByFullName(String fullName, Date birthDate) throws SQLException {
        long id;
        getIdByFullName.setString(1, "%" + fullName + "%");
        getIdByFullName.setDate(2, Date.valueOf(String.valueOf(birthDate)));
        try (ResultSet resultSet = getIdByFullName.executeQuery()) {
            resultSet.next();
            id = resultSet.getInt("id");
        }catch (Exception e){
            id = 0;
        }
        return id;
    }

    public String addDeveloper(String fullName, Date birthDate, Sex sex, String email, String skype, float salary,
                               String project, Industry industry, Level level) throws SQLException {

        long newDeveloperId;
        try (ResultSet rs = selectMaxId.executeQuery()) {
            rs.next();
            newDeveloperId = rs.getLong("maxId");
        }
        newDeveloperId++;
        addDeveloper.setLong(1, newDeveloperId);
        addDeveloper.setString(2, fullName);
        addDeveloper.setDate(3, birthDate);
        addDeveloper.setString(4, String.valueOf(sex));
        addDeveloper.setString(5, email);
        addDeveloper.setString(6, skype);
        addDeveloper.setFloat(7, salary);

        long projectId = new ProjectDaoService(Storage.getInstance().getConnection()).getIdProjectByName(project);
        addProjectDeveloper.setLong(1, projectId);
        addProjectDeveloper.setLong(2, newDeveloperId);

        Developer developer = new Developer();
        developer.setDeveloperId(newDeveloperId);
        developer.setFullName(fullName);
        developer.setBirthDate(birthDate);
        developer.setSex(sex);
        developer.setEmail(email);
        developer.setSkype(skype);
        developer.setSalary(salary);
        developers.add(developer);
        developers.clear();
        String industryName = industry.getIndustryName();

        String lenelName = level.getLevelName();
        getIdSkillByIndustryAndSkillLevel.setString(1, "%" + industryName + "%");
        getIdSkillByIndustryAndSkillLevel.setString(2, "%" + lenelName + "%");
        long skillId;
        try (ResultSet rs = getIdSkillByIndustryAndSkillLevel.executeQuery()) {
            rs.next();
            skillId = rs.getLong("id");
        }

        addDeveloperSkill.setLong(1, newDeveloperId);
        addDeveloperSkill.setLong(2, skillId);

        addDeveloper.executeUpdate();
        addProjectDeveloper.executeUpdate();
        addDeveloperSkill.executeUpdate();

        String result;
        if (existsDeveloper(newDeveloperId)) {
            result = "Added a developer:";
        } else {
            result = "An error occurred. The developer is not added to the database";
        }
        return result;
    }

    public boolean existsDeveloper(long id) throws SQLException {
        existsByIdSt.setLong(1, id);
        try (ResultSet rs = existsByIdSt.executeQuery()) {
            rs.next();
            return rs.getBoolean("developerExists");
        }
    }

    public String deleteDeveloper(String fullName, Date birthDate) throws SQLException {
        long idToDelete = getIdByFullName(fullName, birthDate);

        deleteDeveloperFromDevelopersById.setLong(1, idToDelete);
        deleteDeveloperFromDevelopersById.executeUpdate();
        developers.clear();
        developers.removeIf(nextDeveloper -> nextDeveloper.getDeveloperId() == idToDelete);

        String result;
        if (idToDelete != 0) {
            result = "The developer has been successfully removed";
        } else
            result = "An error occurred. The developer has not been removed from the database";
        return result;
    }

    public String editDeveloper(String fullName, Date birthDate, Sex sex, String email, String skype, float salary, String project,
                             Industry industry, Level level) throws SQLException {

        long newDeveloperId;
        try (ResultSet rs = selectMaxId.executeQuery()) {
            rs.next();
            newDeveloperId = rs.getLong("maxId");
        }
        newDeveloperId++;
        addDeveloper.setLong(1, newDeveloperId);
        addDeveloper.setString(2, fullName);
        addDeveloper.setDate(3, birthDate);
        addDeveloper.setString(4, String.valueOf(sex));
        addDeveloper.setString(5, email);
        addDeveloper.setString(6, skype);

        long projectId = new ProjectDaoService(Storage.getInstance().getConnection()).getIdProjectByName(project);
        addProjectDeveloper.setLong(1, projectId);
        addProjectDeveloper.setLong(2, newDeveloperId);

        addDeveloper.setFloat(7, salary);
        Developer developer = new Developer();
        developer.setDeveloperId(newDeveloperId);
        developer.setFullName(fullName);
        developer.setBirthDate(birthDate);
        developer.setSex(sex);
        developer.setEmail(email);
        developer.setSkype(skype);
        developer.setSalary(salary);
        developers.add(developer);
        developers.clear();

        String industryName = industry.getIndustryName();
        String lenelName = level.getLevelName();

        getIdSkillByIndustryAndSkillLevel.setString(1, "%" + industryName + "%");
        getIdSkillByIndustryAndSkillLevel.setString(2, "%" + lenelName + "%");
        long skillId;
        try (ResultSet rs = getIdSkillByIndustryAndSkillLevel.executeQuery()) {
            rs.next();
            skillId = rs.getLong("id");
        }

        addDeveloperSkill.setLong(1, newDeveloperId);
        addDeveloperSkill.setLong(2, skillId);

        addDeveloper.executeUpdate();
        addProjectDeveloper.executeUpdate();
        addDeveloperSkill.executeUpdate();

        String result;
        if (existsDeveloper(newDeveloperId)) {
            result = "Developer successfully updated:";
        } else {
            result = "An error occurred. The developer is not added to the database";
        }
        return result;
    }

    public void editDeveloperVod(long id) throws SQLException {
        deleteDeveloperFromDevelopersById.setLong(1, id);
        deleteDeveloperFromDevelopersById.executeUpdate();

        developers.removeIf(nextDeveloper -> nextDeveloper.getDeveloperId() == id);
    }

}
