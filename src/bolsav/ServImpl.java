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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author geova
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ{
    
    public List<StockCli> stocks;

    
    public ServImpl() throws RemoteException{
        stocks = new ArrayList();
        Registry referenciaServicoNomes = LocateRegistry.createRegistry(1099);
        referenciaServicoNomes.rebind("RefServidor", this);
        TimerTask update = new TimerTask() {
            @Override
            public void run() {
                updatePrices();
            }
        };
        Timer t = new Timer();
        t.schedule(update, 5000, 5000);
        System.out.println("Servidor funcionando");
    }
    
    public void updatePrices() {
        Random randomGenerator = new Random();
        for (StockCli st: stocks) {
            boolean op = randomGenerator.nextBoolean();
            int range = randomGenerator.nextInt(5);
            double old = st.stock.actualPrice;
            if (op && range > 0) { //adicao
                st.stock.actualPrice += range;
                notifyClients(st, "rise", old, st.stock.actualPrice);
            } else if (st.stock.actualPrice >= (range + st.stock.minPrice) && range > 0) { //subtracao
                st.stock.actualPrice -= range;
                notifyClients(st, "drop", old, st.stock.actualPrice);
            }
        }
    }
    
    public void notifyClients(StockCli sc, String event, double oldPrice, double newPrice) {
        for (InterfaceCli subscriber: sc.subscribers) {
            try {
                subscriber.notify(event + sc.stock.company + String.valueOf(oldPrice) + String.valueOf(newPrice));
            } catch (RemoteException ex) {
                Logger.getLogger(ServImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
    
    @Override
    public void newStock(InterfaceCli client, Stock stock, int id) throws RemoteException {
        StockCli sc = new StockCli(stock, client, id); 
        stocks.add(sc);
    }

    @Override
    public List<StockCli> getStocks() throws RemoteException{
        return stocks;
    }
    
    @Override
    public void subscribe(InterfaceCli client, Stock stock) throws RemoteException {
        for (StockCli st: stocks) {
            if (st.stock.company.equals(stock.company)) {
                st.subscribers.add(client);
            }
        }
    }

    @Override
    public void updateStock() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
