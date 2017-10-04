/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolsav;

import java.io.Serializable;

/**
 *
 * @author davi
 */
public class StockCli implements Serializable{
    
    public Stock stock;
    public InterfaceCli client;
    public int id;

    public StockCli(Stock stock, InterfaceCli client, int id) {
        this.stock = stock;
        this.client = client;
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public InterfaceCli getClient() {
        return client;
    }
    
    public int getId() {
        return id;
    }
    
}
