/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolsav;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author davi
 */
public class Stock implements Serializable{
    
    public String company;
    public int qt;
    public double minPrice;
    public boolean available;
    public double actualPrice;

    public Stock(String company, int qt, double minPrice) {
        this.company = company;
        this.qt = qt;
        this.minPrice = minPrice;
        this.actualPrice = minPrice;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getQt() {
        return qt;
    }

    public void setQt(int qt) {
        this.qt = qt;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public String getPrice() {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format(minPrice).replace(',', '.');
    }
    
}
