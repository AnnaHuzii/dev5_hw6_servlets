package command.developers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import command.settings.Command;
import connection.Storage;
import db.developer.DeveloperDaoService;
import org.thymeleaf.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class GetInformationAboutAllDevelopersCommand implements Command {

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

        resp.getWriter().write("<h1>List of all developers</h1>");

        String allFullName = developerDaoService.getAllFullName();
        resp.getWriter().write("<br>" + allFullName + "</br>");

        out.println("<p >" +
                "<form action=/hw_servlets/developers method=GET>" +
                "<input type=submit value='Developer menu' />" +
                "</p>");

        resp.getWriter().close();

    }

}
