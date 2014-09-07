package technology.raeder.yoappserver.webserver;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import technology.raeder.yoappserver.internal.AsyncYoInformer;
import technology.raeder.yoappserver.internal.LoadedYoApp;

public class RequestHandler extends AbstractHandler {
    
    private final Log log = LogFactory.getLog(RequestHandler.class);
    private final ArrayList<LoadedYoApp> apps;
    
    public RequestHandler(ArrayList<LoadedYoApp> apps) {
        this.apps = apps;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //respond to the server
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("");
        
        //get the username and inform all registered apps
        String requestUrl = baseRequest.getRequestURI();
        final String username = baseRequest.getParameter("username");
        if(username != null) {
            if(requestUrl.endsWith("/")) {
                requestUrl = requestUrl.substring(0, requestUrl.length()-1);
            }
            new AsyncYoInformer(apps, requestUrl, username).start();
        } else {
            log.info("Invalid request on URL " + baseRequest.getUri().toString());
        }
    }
    
}
