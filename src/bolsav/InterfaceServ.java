/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolsav;

import java.rmi.*;

/**
 *
 * @author geova
 */
public interface InterfaceServ extends Remote{
    public void chamar(String nome, InterfaceCli cliente) throws RemoteException;
}
