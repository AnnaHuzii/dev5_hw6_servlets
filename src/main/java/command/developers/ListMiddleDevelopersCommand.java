package command.developers;

import command.settings.Command;
import connection.Storage;
import db.developer.DeveloperDaoService;
import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ListMiddleDevelopersCommand  implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException {
        PrintWriter out = resp.getWriter();
        Storage storage = Storage.getInstance();

        DeveloperDaoService developerDaoService = new DeveloperDaoService(storage.getConnection());

        resp.setContentType("text/html; charset=utf-8");

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                "dd.MM.yyyy, HH:mm:ss"
        ));
        resp.getWriter().write("<br>" + currentTime + "</br>");

        resp.getWriter().write("<h1>List of middle developers </h1>");
        String ListMiddleDevelopers = developerDaoService.getListMiddleDevelopers();

        resp.getWriter().write("<br>" + ListMiddleDevelopers + "</br>");

        out.println("<p >" +
                "<form action=/hw_servlets/developers method=GET>" +
                "<input type=submit value='Developer menu' />" +
                "</p>");

        resp.getWriter().close();

    }
}
