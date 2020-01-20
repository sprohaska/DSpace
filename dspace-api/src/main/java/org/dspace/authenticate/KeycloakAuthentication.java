package org.dspace.authenticate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dspace.authenticate.factory.AuthenticateServiceFactory;
import org.dspace.authorize.AuthorizeException;

// When importing `org.keycloak`, Tomcat fails to load DSpace webapp due to
// infinite recursion when parsing annotations.
//import org.keycloak.KeycloakSecurityContext;

import org.dspace.content.MetadataField;
import org.dspace.content.MetadataSchema;
import org.dspace.content.NonUniqueMetadataException;
import org.dspace.content.factory.ContentServiceFactory;
import org.dspace.content.service.MetadataFieldService;
import org.dspace.content.service.MetadataSchemaService;
import org.dspace.core.Context;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.Group;
import org.dspace.eperson.factory.EPersonServiceFactory;
import org.dspace.eperson.service.EPersonService;
import org.dspace.eperson.service.GroupService;
import org.dspace.services.ConfigurationService;
import org.dspace.services.factory.DSpaceServicesFactory;

public class KeycloakAuthentication implements AuthenticationMethod
{
    /** log4j category */
    private static Logger log = Logger.getLogger(KeycloakAuthentication.class);

    /** Additional metadata mappings **/
    protected Map<String,String> metadataHeaderMap = null;

    /** Maximum length for eperson metadata fields **/
    protected final int NAME_MAX_SIZE = 64;
    protected final int PHONE_MAX_SIZE = 32;

    /** Maximum length for eperson additional metadata fields **/
    protected final int METADATA_MAX_SIZE = 1024;

    protected EPersonService ePersonService = EPersonServiceFactory.getInstance().getEPersonService();
    protected GroupService groupService = EPersonServiceFactory.getInstance().getGroupService();
    protected MetadataFieldService metadataFieldService = ContentServiceFactory.getInstance().getMetadataFieldService();
    protected MetadataSchemaService metadataSchemaService = ContentServiceFactory.getInstance().getMetadataSchemaService();
    protected ConfigurationService configurationService = DSpaceServicesFactory.getInstance().getConfigurationService();

    @Override
    public int authenticate(
        Context context,
        String username,
        String password,
        String realm,
        HttpServletRequest request
    ) throws SQLException {
        System.out.println("TODO KeycloakAuthentication.authenticate()");
        // KeycloakSecurityContext keycloak = (KeycloakSecurityContext)request.getAttribute(KeycloakSecurityContext.class.getName());
        // if (keycloak != null) {
        //     System.out.println("TODO keycloak:" + keycloak.toString());
        // } else {
        //     System.out.println("TODO no keycloak.");
        // }

        return AuthenticationMethod.NO_SUCH_USER;
    }

    @Override
    public List<Group> getSpecialGroups(
        Context context,
        HttpServletRequest request
    ) {
        List<Group> result = new ArrayList<>();
        return result;
    }

    @Override
    public boolean allowSetPassword(
        Context context,
        HttpServletRequest request,
        String email
    ) throws SQLException {
        // Don't use password at all.
        return false;
    }

    @Override
    public boolean isImplicit() {
        return false;
    }

    @Override
    public boolean canSelfRegister(
        Context context,
        HttpServletRequest request,
        String username
    ) throws SQLException {
        // Disable DSpace registration.  Registration is handled in Keycloak.
        return false;
    }

    @Override
    public void initEPerson(
        Context context,
        HttpServletRequest request,
        EPerson eperson
    ) throws SQLException {
        // Disable DSpace registration.  Registration is handled in Keycloak.
    }

    @Override
    public String loginPageURL(
        Context context,
        HttpServletRequest request,
        HttpServletResponse response
    ) {
        return response.encodeRedirectURL(
            request.getContextPath() + "/keycloak-login"
        );
    }

    @Override
    public String loginPageTitle(Context context) {
        return "org.dspace.authenticate.KeycloakAuthentication.title";
    }
}
