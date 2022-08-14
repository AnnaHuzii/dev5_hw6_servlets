package command.settings;

import command.companies.CompaniesMenuCommand;
import command.customers.CustomersMenuCommand;
import command.developers.*;
import command.main.ChooseTableCommand;
import command.main.MainMenuCommand;
import command.project.ProjectMenuCommand;
import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CommandService {
    private final Map<String, Command> commands;

    public CommandService() {
        commands = new HashMap<>();

        commands.put("GET /hw_servlets", new MainMenuCommand());
        commands.put("GET /hw_servlets/project", new ProjectMenuCommand());
        commands.put("GET /hw_servlets/companies", new CompaniesMenuCommand());
        commands.put("GET /hw_servlets/customers", new CustomersMenuCommand());
        commands.put("GET /hw_servlets/developers", new DevelopersMenuCommand());
        commands.put("POST /hw_servlets/", new ChooseTableCommand());
        commands.put("POST /hw_servlets/developers/all_developers", new GetInformationAboutAllDevelopersCommand());
        commands.put("POST /hw_servlets/developers/developer_info", new GetInformationAboutDeveloperByNameCommand());
        commands.put("POST /hw_servlets/developers/quantity_java_developers", new QuantityJavaDevelopersCommand());
        commands.put("POST /hw_servlets/developers/middle_developers", new ListMiddleDevelopersCommand());
        commands.put("POST /hw_servlets/developers/add", new AddDeveloperCommand());
        commands.put("POST /hw_servlets/developers/update", new UpdateDeveloperCommand());
        commands.put("POST /hw_servlets/developers/delete", new DeleteDeveloperCommand());
    }

    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {

        String requestUri = req.getRequestURI();
        String commandKey = req.getMethod() + " " + requestUri;

        commands.get(commandKey).process(req, resp, engine);
    }

}
