package command.developers;

import command.settings.Command;
import connection.Storage;
import db.developer.DeveloperDaoService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

public class DeleteDeveloperCommand implements Command {
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
            DeveloperDaoService developerDaoService = new DeveloperDaoService(util.getConnection());
            String deleteDeveloper = developerDaoService.deleteDeveloper(fullName, birthDate);

            context.setVariable("deleteDeveloper", deleteDeveloper);
            context.setVariable("fullName", fullName);
            context.setVariable("fullName", fullName);

            resp.setContentType("text/html; charset=utf-8");

            engine.process("developer_delete", context, resp.getWriter());
            resp.getWriter().close();
        } else {
            engine.process("error_developer_incorrectly", context, resp.getWriter());
            resp.getWriter().close();
        }
    }
}

