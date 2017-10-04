/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolsav;

import java.rmi.*;
import java.util.List;

/**
 *
 * @author geova
 */
public interface InterfaceServ extends Remote{
    public void chamar(String nome, InterfaceCli cliente) throws RemoteException;
    public void newStock(InterfaceCli client, Stock stock, int id) throws RemoteException;
    public void updateStock() throws RemoteException;
    public List<StockCli> getStocks() throws RemoteException;
    public void subscribe(InterfaceCli client, Stock stock) throws RemoteException;
}
