package command.settings;

import command.companies.AddCompanies;
import command.companies.CompaniesMenuCommand;
import command.companies.GetInformationAboutAllCompanies;
import command.customers.AddCustomers;
import command.customers.CustomersMenuCommand;
import command.customers.GetInformationAboutAllCustomers;
import command.developers.*;
import command.main.ChooseTableCommand;
import command.main.MainMenuCommand;
import command.project.AllInfoAboutProjectCommand;
import command.project.ListAllProjectCommand;
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

        commands.put("GET /", new MainMenuCommand());
        commands.put("POST /", new ChooseTableCommand());

        commands.put("GET /developers", new DevelopersMenuCommand());
        commands.put("POST /developers/all_developers", new GetInformationAboutAllDevelopersCommand());
        commands.put("POST /developers/developer_info", new GetInformationAboutDeveloperByNameCommand());
        commands.put("POST /developers/quantity_java_developers", new QuantityJavaDevelopersCommand());
        commands.put("POST /developers/middle_developers", new ListMiddleDevelopersCommand());
        commands.put("POST /developers/add", new AddDeveloperCommand());
        commands.put("POST /developers/update", new UpdateDeveloperCommand());
        commands.put("POST /developers/delete", new DeleteDeveloperCommand());

        commands.put("GET /companies", new CompaniesMenuCommand());
        commands.put("POST /companies/all_companies", new GetInformationAboutAllCompanies());
        commands.put("POST /companies/add", new AddCompanies());

        commands.put("GET /project", new ProjectMenuCommand());
        commands.put("POST /project/projects_list_of_all", new ListAllProjectCommand());
        commands.put("POST /project/project_all_info_about", new AllInfoAboutProjectCommand());

        commands.put("GET /customers", new CustomersMenuCommand());
        commands.put("POST /customers/all_customers", new GetInformationAboutAllCustomers());
        commands.put("POST /customers/add", new AddCustomers());

    }

    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {

        String requestUri = req.getRequestURI();
        String commandKey = req.getMethod() + " " + requestUri;

        commands.get(commandKey).process(req, resp, engine);
    }

}
