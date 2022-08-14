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
import java.time.LocalDate;
import java.util.List;

public class DeleteDeveloperCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException {

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

        String deleteDeveloper = developerDaoService.deleteDeveloper(fullName, birthDate);

        resp.getWriter().write("<h1>" + deleteDeveloper + "</h1>");
        resp.getWriter().write("<h3>" + fullName + " - Birth date: " + birthDate + "</h3>");

        out.println("<p >" +
                "<form action=/hw_servlets/developers method=GET>" +
                "<input type=submit value='Developer menu' />" +
                "</p>");
        resp.getWriter().close();

        resp.getWriter().close();
    }
}

