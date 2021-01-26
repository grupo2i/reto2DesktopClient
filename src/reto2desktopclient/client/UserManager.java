/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.client;

import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.NoContentException;

/**
 *
 * @author Martin Angulo <martin.angulo at tartanga.eus>
 */
public interface UserManager {
    
    public <T> T getUserByLogin(Class<T> responseType, String login) throws ClientErrorException;

    public <T> T getPrivilege(Class<T> responseType, String login)
            throws InternalServerErrorException, NotAuthorizedException;
    
    public void edit(Object requestEntity) 
            throws InternalServerErrorException;
    
    public <T> T find(Class<T> responseType, String id) 
            throws InternalServerErrorException, NoContentException;
    
    public <T> T signIn(Class<T> responseType, String login, String password) 
            throws InternalServerErrorException, NotAuthorizedException;
    
    public void create(Object requestEntity) 
            throws InternalServerErrorException;
    
    public void remove(String id) 
            throws InternalServerErrorException;

    public void close();
}
