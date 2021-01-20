/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 *
 * @author Martin Angulo <martin.angulo at tartanga.eus>
 */
public interface ClientManager {
    
    public void edit(Object requestEntity) throws ClientErrorException;
    public <T> T find(Class<T> responseType, String id) throws ClientErrorException;
    public void create(Object requestEntity) throws ClientErrorException;
    public <T> T getEventsByClientId(Class<T> responseType, String id) throws ClientErrorException;
    public <T> T getAllClients(GenericType<T> responseType) throws ClientErrorException;
    public void remove(String id) throws ClientErrorException;
    public void close();
}
