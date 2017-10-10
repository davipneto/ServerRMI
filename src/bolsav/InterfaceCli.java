package bolsav;

import java.rmi.*;

/**
 * A interface InterfaceCli possui os métodos remotos relacionados com o acesso
 * ao cliente
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public interface InterfaceCli extends Remote {

    /**
     * Método para verificar se o cliente recebe alguma mensagem
     *
     * @param eee com a mensagem
     * @throws RemoteException
     */
    public void echo(String eee) throws RemoteException;

    /**
     * Notifica o cliente com uma mensagem
     *
     * @param event com a mensagem desejada
     * @throws RemoteException
     */
    public void notify(String event) throws RemoteException;
}
