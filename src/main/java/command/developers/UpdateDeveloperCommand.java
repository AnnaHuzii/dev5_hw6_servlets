package command.developers;

import command.settings.Command;
import connection.Storage;
import db.developer.DeveloperDaoService;
import db.developer.Sex;
import db.skill.Industry;
import db.skill.Level;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class UpdateDeveloperCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException {

        Storage util = Storage.getInstance();
        Context context = new Context();
        String fullName = req.getParameter("developerFullName");

        Date birthDate = null;
        try {
            birthDate = Date.valueOf(LocalDate.parse(req.getParameter("developerBirthDate")));
        }catch (Exception e){

            engine.process("error_developer_incorrectly", context, resp.getWriter());
            resp.getWriter().close();
        }
        if (!fullName.equals("")) {
            String email = req.getParameter("developerEmail");
        String sex = req.getParameter("developerSex");
        Sex sexName = null;
        if (sex.equals(Sex.MALE.getSexName())) {
            sexName = Sex.MALE;
        } else if (sex.equals(Sex.FEMALE.getSexName())) {
            sexName = Sex.FEMALE;
        } else if (sex.equals(Sex.UNKNOWN.getSexName())) {
            sexName = Sex.UNKNOWN;
        }
        String skype = req.getParameter("developerSkype");
        String project = req.getParameter("developerProject");
        float salary = Float.parseFloat(req.getParameter("developerSalary"));
        String industry = req.getParameter("developerLanguage");
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
        String languageLevel = req.getParameter("developerLanguageLevel");
        Level levelName = null;
        if (languageLevel.equals(Level.JUNIOR.getLevelName())) {
            levelName = Level.JUNIOR;
        } else if (languageLevel.equals(Level.MIDDLE.getLevelName())) {
            levelName = Level.MIDDLE;
        } else if (languageLevel.equals(Level.SENIOR.getLevelName())) {
            levelName = Level.SENIOR;
        }

        DeveloperDaoService developerDaoService = new DeveloperDaoService(util.getConnection());
        long idToDelete = 0;

        try {
            idToDelete = developerDaoService.getIdByFullName(fullName, birthDate);
        } catch (SQLException e) {
            resp.getWriter().write("<h1>" + "There is no such developer in the database. Enter the correct data.");
        }
        developerDaoService.editDeveloperVod(idToDelete);

        String editDeveloper = developerDaoService.editDeveloper(fullName, birthDate, sexName, email, skype, salary,project,
                                                                    industryName, levelName);
            context.setVariable("addDeveloper", editDeveloper);
            context.setVariable("fullName", fullName);
            context.setVariable("birthDate", birthDate);
            context.setVariable("project", project);
            context.setVariable("salary", salary);
            context.setVariable("industryName", industryName);
            context.setVariable("levelName", levelName);

            resp.setContentType("text/html; charset=utf-8");

            engine.process("developer_update", context, resp.getWriter());

        resp.getWriter().close();
        } else {
            engine.process("error_developer_incorrectly", context, resp.getWriter());
            resp.getWriter().close();
        }
    }
}

