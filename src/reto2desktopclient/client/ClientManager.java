/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.client;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.NoContentException;

/**
 *
 * @author Martin Angulo <martin.angulo at tartanga.eus>
 */
public interface ClientManager {
    
    public void edit(Object requestEntity) throws InternalServerErrorException;
    public <T> T find(Class<T> responseType, String id) throws InternalServerErrorException, NoContentException;
    public void create(Object requestEntity) throws InternalServerErrorException;
    public <T> T getEventsByClientId(Class<T> responseType, String id) throws InternalServerErrorException;
    public <T> T getAllClients(GenericType<T> responseType) throws InternalServerErrorException;
    public void remove(String id) throws InternalServerErrorException;
    public void close();
}
