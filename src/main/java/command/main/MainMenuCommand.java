package command.main;

import command.settings.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainMenuCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {

        resp.setContentType("text/html, charset=utf-8");

        Map<String, String[]> parameterMap = req.getParameterMap();

        Map<String, Object> params = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> keyValue : parameterMap.entrySet()) {
            params.put(keyValue.getKey(), keyValue.getValue()[0]);
        }

        Context context = new Context(
                req.getLocale(),
                Map.of("table", params)
        );
        engine.process("main", context, resp.getWriter());
        resp.getWriter().close();
    }
}