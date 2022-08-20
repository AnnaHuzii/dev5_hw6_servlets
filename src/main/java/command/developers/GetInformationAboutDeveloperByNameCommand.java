package command.developers;

import command.settings.Command;
import connection.Storage;
import db.developer.Developer;
import db.developer.DeveloperDaoService;
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

public class GetInformationAboutDeveloperByNameCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {
        Storage util = Storage.getInstance();
        Context context = new Context();
        String fullName = req.getParameter("developerFullName");
        Date birthDate = null;
        try {
            birthDate = Date.valueOf(LocalDate.parse(req.getParameter("developerBirthDate")));
        } catch (Exception e) {

            engine.process("error_developer_incorrectly", context, resp.getWriter());
            resp.getWriter().close();
        }
        if (!fullName.equals("")) {
            DeveloperDaoService developerDaoService = new DeveloperDaoService(util.getConnection());

            List<Developer> InfoByFullName = developerDaoService.getInfoByFullName(fullName, birthDate);
            String SkillsByFullName = developerDaoService.getSkillsByFullName(fullName, birthDate);
            String ProjectsByFullName = developerDaoService.getProjectsByFullName(fullName, birthDate);
            context.setVariable("fullName", fullName);
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

                resp.setContentType("text/html, charset=utf-8");
                engine.process("developer_information", context, resp.getWriter());
                resp.getWriter().close();
            } else {
                engine.process("error_developer", context, resp.getWriter());
                resp.getWriter().close();
            }
        } else {
            engine.process("error_developer_incorrectly", context, resp.getWriter());
            resp.getWriter().close();
        }
    }

}
