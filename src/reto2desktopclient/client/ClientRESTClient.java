package reto2desktopclient.client;

import java.util.ResourceBundle;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.NoContentException;

/**
 * Jersey REST client generated for REST resource:ClientFacadeREST
 * [entity.client]<br>
 * USAGE:
 * <pre>
 *        ClientRESTClient client = new ClientRESTClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Martin Angulo <martin.angulo at tartanga.eus>
 */
public class ClientRESTClient implements ClientManager {

    private WebTarget webTarget;
    private Client client;
    //TODO: Leer la URL de un archivo de propiedades
    private static final String BASE_URI = ResourceBundle
            .getBundle("reto2desktopclient.properties.properties").getString("BASE_URI");;

    public ClientRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entity.client");
    }

    public void edit(Object requestEntity) throws InternalServerErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public <T> T find(Class<T> responseType, String id) throws InternalServerErrorException, NoContentException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void create(Object requestEntity) throws InternalServerErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public <T> T getEventsByClientId(Class<T> responseType, String id) throws InternalServerErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getEventsByClient/{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T getAllClients(GenericType<T> responseType) throws InternalServerErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllClients");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public void remove(String id) throws InternalServerErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    public void close() {
        client.close();
    }
    
}
