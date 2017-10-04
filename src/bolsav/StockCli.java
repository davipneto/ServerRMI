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
    public List<InterfaceCli> subscribers;

    public StockCli(Stock stock, InterfaceCli client) {
        this.stock = stock;
        this.client = client;
        this.subscribers = new ArrayList();
    }

    public Stock getStock() {
        return stock;
    }

    public InterfaceCli getClient() {
        return client;
    }
    
    
}
