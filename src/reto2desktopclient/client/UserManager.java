/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reto2desktopclient.client;

import javax.ws.rs.ClientErrorException;

/**
 *
 * @author Martin Angulo <martin.angulo at tartanga.eus>
 */
public interface UserManager {
    
    public <T> T getPrivilege(Class<T> responseType, String login) throws ClientErrorException;
    public void edit(Object requestEntity, String id) throws ClientErrorException;
    public <T> T find(Class<T> responseType, String id) throws ClientErrorException;
    public <T> T signIn(Class<T> responseType, String login, String password) throws ClientErrorException;
    public void create(Object requestEntity) throws ClientErrorException;
    public void remove(String id) throws ClientErrorException;
    public void close();
}
