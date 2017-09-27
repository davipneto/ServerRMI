/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolsav;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 *
 * @author geova
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ{
    
    private List<StockCli> stocks;

    
    public ServImpl() throws RemoteException{
        stocks = new ArrayList();
        Registry referenciaServicoNomes = LocateRegistry.createRegistry(1099);
        referenciaServicoNomes.rebind("RefServidor", this);
        System.out.println("Servidor funcionando");
    }
    
    @Override
    public void chamar(String nome, InterfaceCli cliente) throws RemoteException{
        try{
            System.out.println("cliente invocou esse metodo");
            cliente.echo("Servidor chamando");
        }catch(RemoteException re){
            System.out.println("Deu erro");
        }
    }
    
    public void newStock(InterfaceCli client, Stock stock) throws RemoteException {
        StockCli sc = new StockCli(stock, client); 
        stocks.add(sc);
    }
    
}
