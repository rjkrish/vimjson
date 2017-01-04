package com.github.rjkrish.vimjson.util;

import com.vmware.vim25.*;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;


public class Connection {
    private VimService vimService;
    private VimPortType vimPort;
    private ServiceContent serviceContent;
    private UserSession userSession;
    private ManagedObjectReference svcInstRef;

    private URL url;
    private String username;
    private String password = ""; // default password is empty since on rare occasion passwords are not set
    @SuppressWarnings("rawtypes")
    private Map headers;

    public Connection setUrl(String url) {
        try {
            this.url = new URL(url);
            return this;
        } catch (MalformedURLException e) {
            throw new RuntimeException("malformed URL argument: '" + url + "'", e);
        }
    }

    public String getUrl() {
        return url.toString();
    }

    public String getHost() {
        return url.getHost();
    }

    public Integer getPort() {
        int port = url.getPort();
        return port;
    }

    public Connection setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Connection setPassword(String password)
    {
        this.password = password;
        return this;
    }

    public String getPassword() {
        return this.password;
    }

    public VimService getVimService() {
        return vimService;
    }

    public VimPortType getVimPort() {
        return vimPort;
    }

    public ServiceContent getServiceContent() {
        return serviceContent;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public String getServiceInstanceName() {
        return "ServiceInstance"; // Theoretically this could change but it never does in these samples.
    }

    @SuppressWarnings("rawtypes")
    public Map getHeaders() {
        return headers;
    }

    public ManagedObjectReference getServiceInstanceReference() {
        if (svcInstRef == null) {
            ManagedObjectReference ref = new ManagedObjectReference();
            ref.setType(this.getServiceInstanceName());
            ref.setValue(this.getServiceInstanceName());
            svcInstRef = ref;
        }
        return svcInstRef;
    }

    public Connection connect() {
        if (!isConnected()) {
            try {
                _connect();
            } catch (Exception e) {
                Throwable cause = (e.getCause() != null)?e.getCause():e;
                throw new RuntimeException(
                        "failed to connect: " + e.getMessage() + " : " + cause.getMessage(),
                        cause);
            }
        }
        return this;
    }

    @SuppressWarnings("rawtypes")
    private void _connect() throws RuntimeFaultFaultMsg, InvalidLocaleFaultMsg, InvalidLoginFaultMsg {
        vimService = new VimService();
        vimPort = vimService.getVimPort();
        Map<String, Object> ctxt =
                ((BindingProvider) vimPort).getRequestContext();

        ctxt.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url.toString());
        ctxt.put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

        serviceContent = vimPort.retrieveServiceContent(this.getServiceInstanceReference());

        userSession = vimPort.login(
                serviceContent.getSessionManager(),
                username,
                password,
                null);

        headers =
                (Map) ((BindingProvider) vimPort).getResponseContext().get(
                        MessageContext.HTTP_RESPONSE_HEADERS);
    }

    public boolean isConnected() {
        if (userSession == null) {
            return false;
        }
        long startTime = userSession.getLastActiveTime().toGregorianCalendar().getTime().getTime();

        // 30 minutes in milliseconds = 30 minutes * 60 seconds * 1000 milliseconds
        return new Date().getTime() < startTime + 30 * 60 * 1000;
    }

    public Connection disconnect() {
        if (this.isConnected()) {
            try {
                vimPort.logout(serviceContent.getSessionManager());
            } catch (Exception e) {
                Throwable cause = e.getCause();
                throw new RuntimeException(
                        "failed to disconnect properly: " + e.getMessage() + " : " + cause.getMessage(),
                        cause
                );
            } finally {
                // A connection is very memory intensive, I'm helping the garbage collector here
                userSession = null;
                serviceContent = null;
                vimPort = null;
                vimService = null;
            }
        }
        return this;
    }

    public URL getURL() {
        return this.url;
    }

}

