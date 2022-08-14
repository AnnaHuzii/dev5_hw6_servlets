package thymeleaf;

import command.settings.CommandService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@WebServlet("/*")
public class ThymeleafController extends HttpServlet {

private TemplateEngine engine;
    private CommandService commandService;
    private ServletConfig config;

    @Override
    public void init(ServletConfig config) throws ServletException  {
        this.config = config;
        ServletContext sc = config.getServletContext();

        engine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(sc.getRealPath("templates") + "/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
        commandService = new CommandService();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            commandService.process(req, resp, engine);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
