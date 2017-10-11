package bolsav;

import java.rmi.*;
import java.util.List;

/**
 * A interface InterfaceServ possui os métodos remotos relacionado com o
 * servidor
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public interface InterfaceServ extends Remote {

    /**
     * Chama no servidor para teste
     *
     * @param nome com o nome
     * @param cliente e com a referência do cliente
     * @throws RemoteException
     */
    public void chamar(String nome, InterfaceCli cliente) throws RemoteException;

    /**
     * Adiciona uma nova ação na base de dados do servidor
     *
     * @param client com a referência do cliente
     * @param stock com a ação que o cliente possui
     * @param id com o id do cliente
     * @throws RemoteException
     */
    public void newStock(InterfaceCli client, Stock stock, long id) throws RemoteException;

    /**
     * Retorna a lista de ações que o servidor possui na sua base de dados
     *
     * @return do tipo lista com as ações e os cliente que a possuem
     * @throws RemoteException
     */
    public List<StockCli> getStocks() throws RemoteException;

    /**
     * Adiciona um cliente como subscriber para acompanhar os preços das ações
     * de uma companhia
     *
     * @param client com a referência do cliente
     * @param company com o nome da empresa
     * @throws RemoteException
     */
    public void subscribe(InterfaceCli client, String company) throws RemoteException;

    /**
     * Realiza a compra de ações
     *
     * @param buyer com a referência do comprador
     * @param company com o nome da empresa que deseja comprar as ações
     * @param maxPrice com o valor máximo que deseja pagar
     * @param qtde com a quantidade desejada
     * @throws RemoteException
     */
    public void buy(InterfaceCli buyer, String company, double maxPrice, int qtde) throws RemoteException;
    
    /**
     * Realiza o logout do cliente no mercado de ações
     * @param client com a referência do cliente
     * @throws RemoteException 
     */
    public void logout(InterfaceCli client) throws RemoteException;
}
