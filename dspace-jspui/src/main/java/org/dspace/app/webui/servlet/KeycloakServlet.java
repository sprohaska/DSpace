package org.dspace.app.webui.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dspace.app.webui.util.Authenticate;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.authenticate.AuthenticationMethod;
import org.dspace.authenticate.factory.AuthenticateServiceFactory;
import org.dspace.authenticate.service.AuthenticationService;
import org.dspace.authorize.AuthorizeException;
import org.dspace.core.Context;
import org.dspace.core.LogManager;

public class KeycloakServlet extends DSpaceServlet {
    /** log4j logger */
    private static final Logger log = Logger.getLogger(KeycloakServlet.class);

    private final transient AuthenticationService authenticationService =
        AuthenticateServiceFactory .getInstance().getAuthenticationService();

    @Override
    protected void doDSGet(
        Context context,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException, SQLException, AuthorizeException {
        String jsp = null;

        log.warn("TODO KeycloakServlet");
        System.out.println("TODO KeycloakServlet");

        // Get the eperson from the request.
        int status = authenticationService.authenticate(
            context, null, null, null, request
        );

        if (status == AuthenticationMethod.SUCCESS) {
            Authenticate.loggedIn(context, request, context.getCurrentUser());
            log.info(LogManager.getHeader(context, "login", "type=keycloak"));
            Authenticate.resumeInterruptedRequest(request, response);
            return;
        }

        if (status == AuthenticationMethod.CERT_REQUIRED) {
            jsp = "/error/require-certificate.jsp";
        } else if(status == AuthenticationMethod.NO_SUCH_USER) {
            jsp = "/login/no-single-sign-out.jsp";
        } else if(status == AuthenticationMethod.BAD_ARGS) {
            jsp = "/login/no-email.jsp";
        }
        log.info("Keycloak login failed.");
        JSPManager.showJSP(request, response, jsp);
    }
}
