package reto2desktopclient.client;

import java.util.ResourceBundle;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for REST resource:ArtistFacadeREST
 * [entity.artist]<br>
 * USAGE:
 * <pre>
 *        ArtistRESTClient client = new ArtistRESTClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Martin Angulo <martin.angulo at tartanga.eus>
 */
public class ArtistRESTClient implements ArtistManager {

    private WebTarget webTarget;
    private Client client;
    //TODO: Leer la URL de un archivo de propiedades
    private static final String BASE_URI = ResourceBundle
            .getBundle("reto2desktopclient.properties.properties").getString("BASE_URI");

    ;

    public ArtistRESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entity.artist");
    }

    /**
     *
     * @param requestEntity
     * @throws ClientErrorException
     */
    @Override
    public void edit(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    /**
     *
     * @param <T>
     * @param responseType
     * @param id
     * @return
     * @throws ClientErrorException
     */
    @Override
    public <T> T find(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public void create(Object requestEntity) throws ClientErrorException {
        webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_XML).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
    }

    public <T> T getAllArtists(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllArtists");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    @Override
    public void remove(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    @Override
    public void close() {
        client.close();
    }

    
    @Override
    public <T> T getAllArtists(GenericType<T> responseType) throws ClientErrorException {
      WebTarget resource = webTarget;
        resource = resource.path("getAllArtists");
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
      }

}
