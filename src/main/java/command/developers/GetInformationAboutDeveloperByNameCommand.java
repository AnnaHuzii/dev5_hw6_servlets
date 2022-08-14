package command.developers;

import command.settings.Command;
import connection.Storage;
import db.developer.Developer;
import db.developer.DeveloperDaoService;
import db.skill.Skill;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.StringJoiner;

public class GetInformationAboutDeveloperByNameCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {
        PrintWriter out = resp.getWriter();
        Storage storage = Storage.getInstance();
        String fullName = "";
        Date birthDate = null;
        try {
            fullName = req.getParameter("developerFullName");
            birthDate = Date.valueOf(LocalDate.parse(req.getParameter("developerBirthDate")));
        } catch (Exception e) {
            resp.getWriter().write("<h1>You have entered incorrect data, please try again</h1>");
            out.println("<p >" +
                    "<form action=/hw_servlets/developers method=GET>" +
                    "<input type=submit value='Developer menu' />" +
                    "</p>");
            resp.getWriter().close();
        }
        DeveloperDaoService developerDaoService = new DeveloperDaoService(storage.getConnection());
        Context context = new Context();
        context.setVariable("fullName", fullName);

        List<Developer> InfoByFullName = developerDaoService.getInfoByFullName(fullName, birthDate);

        String SkillsByFullName = developerDaoService.getSkillsByFullName(fullName, birthDate);
        String ProjectsByFullName = developerDaoService.getProjectsByFullName(fullName,birthDate);

        if (InfoByFullName.size() != 0) {
            for (Developer info : InfoByFullName) {
                String birthDateInfo = String.valueOf(info.getBirthDate());
                context.setVariable("birthDateInfo", birthDateInfo);
                context.setVariable("Sex", info.getSex());
                context.setVariable("Email", info.getEmail());
                context.setVariable("Skype", info.getSkype());
                context.setVariable("Salary", info.getSalary());
            }
            context.setVariable("Skill", SkillsByFullName);
            context.setVariable("Projects", ProjectsByFullName);

            engine.process("information_developer", context, resp.getWriter());
        } else {
            resp.getWriter().write("<h1>No such developer found, please enter the correct details again</h1>");
            out.println("<p >" +
                    "<form action=/hw_servlets/developers method=GET>" +
                    "<input type=submit value='Developer menu' />" +
                    "</p>");

        }
        resp.getWriter().close();
    }
}
