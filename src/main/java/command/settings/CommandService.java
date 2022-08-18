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

        commands.put("GET /hw_servlets", new MainMenuCommand());
        commands.put("POST /hw_servlets/", new ChooseTableCommand());

        commands.put("GET /hw_servlets/developers", new DevelopersMenuCommand());
        commands.put("POST /hw_servlets/developers/all_developers", new GetInformationAboutAllDevelopersCommand());
        commands.put("POST /hw_servlets/developers/developer_info", new GetInformationAboutDeveloperByNameCommand());
        commands.put("POST /hw_servlets/developers/quantity_java_developers", new QuantityJavaDevelopersCommand());
        commands.put("POST /hw_servlets/developers/middle_developers", new ListMiddleDevelopersCommand());
        commands.put("POST /hw_servlets/developers/add", new AddDeveloperCommand());
        commands.put("POST /hw_servlets/developers/update", new UpdateDeveloperCommand());
        commands.put("POST /hw_servlets/developers/delete", new DeleteDeveloperCommand());

        commands.put("GET /hw_servlets/companies", new CompaniesMenuCommand());
        commands.put("POST /hw_servlets/companies/all_companies", new GetInformationAboutAllCompanies());
        commands.put("POST /hw_servlets/companies/add", new AddCompanies());

        commands.put("GET /hw_servlets/project", new ProjectMenuCommand());
        commands.put("POST /hw_servlets/project/projects_list_of_all", new ListAllProjectCommand());
        commands.put("POST /hw_servlets/project/project_all_info_about", new AllInfoAboutProjectCommand());

        commands.put("GET /hw_servlets/customers", new CustomersMenuCommand());
        commands.put("POST /hw_servlets/customers/all_customers", new GetInformationAboutAllCustomers());
        commands.put("POST /hw_servlets/customers/add", new AddCustomers());

    }

    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException, SQLException, ParseException {

        String requestUri = req.getRequestURI();
        String commandKey = req.getMethod() + " " + requestUri;

        commands.get(commandKey).process(req, resp, engine);
    }

}
