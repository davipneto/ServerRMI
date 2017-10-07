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
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
        t.schedule(update, 15000, 15000);
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
                NumberFormat formatter = new DecimalFormat("#0.00");
                String oldp = formatter.format(oldPrice).replace(',', '.');
                String newp = formatter.format(newPrice).replace(',', '.');
                subscriber.notify(event + " " + sc.stock.company + " "  + oldp + " "  + newp);
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
    public void newStock(InterfaceCli client, Stock stock, long id) throws RemoteException {
        for (StockCli sc: stocks) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setAvailable(true);
                return;
            }
        }
        StockCli sc = new StockCli(stock, client, id); 
        stocks.add(sc);
    }

    @Override
    public List<StockCli> getStocks() throws RemoteException{
        return stocks;
    }
    
    @Override
    public void subscribe(InterfaceCli client, String company) throws RemoteException {
        for (StockCli st: stocks) {
            if (st.stock.company.equals(company)) {
                st.subscribers.add(client);
            }
        }
    }

    @Override
    public void buy(InterfaceCli buyer, String company, double maxPrice, int qtde) throws RemoteException {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (StockCli sc: stocks) {
                    Stock s = sc.stock;
                    if (s.company.equals(company) && s.isAvailable() && s.actualPrice >= s.minPrice && s.actualPrice <= maxPrice && s.qt >= qtde && buyer!= sc.client) {
                        NumberFormat formatter = new DecimalFormat("#0.00");
                        double price = (s.actualPrice + maxPrice) / 2;
                        String p = formatter.format(price).replace(',', '.');
                        try {
                            buyer.notify("buy " + company + " " + p + " " + qtde + " " + s.minPrice);
                            sc.client.notify("sell " + company + " " + p + " " + qtde + " " + s.minPrice);
                            this.cancel();
                        } catch (RemoteException ex) {
                            Logger.getLogger(ServImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        };
        Timer t = new Timer();
        t.schedule(task, 5000, 5000);
    }
    
}
