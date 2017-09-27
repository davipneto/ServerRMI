/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bolsav;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author geova
 */
public class Servidor_BolsaV {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            ServImpl servImpl = new ServImpl();
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor_BolsaV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
