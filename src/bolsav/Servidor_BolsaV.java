package bolsav;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe principal do servidor
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class Servidor_BolsaV {

    /**
     * Método principal
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //cria uma instância do servidor
            ServImpl servImpl = new ServImpl();
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor_BolsaV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
