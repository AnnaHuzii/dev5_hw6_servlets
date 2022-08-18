package command.project;

import command.settings.Command;
import connection.Storage;
import db.project.ProjectDaoService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class AllInfoAboutProjectCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(
                "dd.MM.yyyy, HH:mm:ss"
        ));
        resp.getWriter().write("<br>" + currentTime + "</br>");
        Storage util = Storage.getInstance();
        ProjectDaoService projectDaoService = new ProjectDaoService(util.getConnection());

        String nameProject = req.getParameter("projectName");
        if (!nameProject.equals("")) {
            // Отримати розшитені дані по назві проекту
            String getInfoByName = projectDaoService.getInfoByName(nameProject);
            String[] words = getInfoByName.split("<br>");
            ArrayList listInfoByName = new ArrayList();
            Collections.addAll(listInfoByName, words);

            // Отримати список всіх розробників конкретного проекту KUP Agro
            String getProjectsListInSpecialFormat = projectDaoService.getListDevelopers(nameProject);
            String[] wordsName = getProjectsListInSpecialFormat.split("<br>");
            ArrayList listDeveloper = new ArrayList();
            Collections.addAll(listDeveloper, wordsName);

            // Суму зарплат всіх розробників проекту Quarter
            float projectCost = projectDaoService.getBudgetByProjectName(nameProject);

            resp.setContentType("text/html; charset=utf-8");
            Context simpleContext = new Context(
                    req.getLocale(),
                    Map.of("nameProject", nameProject, "listInfoByName", listInfoByName, "listDeveloper", listDeveloper, "projectCost", projectCost)
            );
            engine.process("project_all_info_about", simpleContext, resp.getWriter());
            resp.getWriter().close();
        } else {
            Context incorrectlyContext = new Context();
            engine.process("error_projects_incorrectly", incorrectlyContext, resp.getWriter());
            resp.getWriter().close();
        }
    }
}
