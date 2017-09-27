/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolsav;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author geova
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ{

    @Override
    public void chamar(String nome, InterfaceCli cliente) throws RemoteException{
        try{
            System.out.println("cliente invocou esse metodo");
        cliente.echo("Servidor chamando");
        }catch(RemoteException re){
            System.out.println("Deu erro");
        }
    }
    
    public ServImpl() throws RemoteException{
        
    }
    
}
