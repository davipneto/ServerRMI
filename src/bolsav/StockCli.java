/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolsav;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author davi
 */
public class StockCli implements Serializable{
    
    public Stock stock;
    public InterfaceCli client;
    public long id;
    public List<InterfaceCli> subscribers;

    public StockCli(Stock stock, InterfaceCli client, long id) {
        this.stock = stock;
        this.client = client;
        this.id = id;
        this.subscribers = new ArrayList();
    }

    public Stock getStock() {
        return stock;
    }

    public InterfaceCli getClient() {
        return client;
    }
    
    public long getId() {
        return id;
    }
    
}
