/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolsav;

/**
 *
 * @author davi
 */
public class StockCli {
    
    private Stock stock;
    private InterfaceCli client;

    public StockCli(Stock stock, InterfaceCli client) {
        this.stock = stock;
        this.client = client;
    }

    public Stock getStock() {
        return stock;
    }

    public InterfaceCli getClient() {
        return client;
    }
    
    
}
