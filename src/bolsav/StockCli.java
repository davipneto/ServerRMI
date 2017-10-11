package bolsav;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A classe StockCli relaciona uma ação com a interface de um cliente
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class StockCli implements Serializable {

    public Stock stock;
    public InterfaceCli client;
    public long id;
    //lista dos subscribers que desejam receber updates dessa ação
    public List<InterfaceCli> subscribers;

    /**
     * Construtor da classe seta a ação, o cliente a quem ela pertence, o id o
     * cliente e cria um novo array de subscribers
     *
     * @param stock com a ação que o cliente possui
     * @param client com a referência do cliente
     * @param id com o id do cliente
     */
    public StockCli(Stock stock, InterfaceCli client, long id) {
        this.stock = stock;
        this.client = client;
        this.id = id;
        this.subscribers = new ArrayList();
    }

    /**
     * Retorna a ação
     *
     * @return do tipo Stock com a ação desejada
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * Retorna a referência do cliente
     *
     * @return do tipo InterfaceCli com a referência do cliente
     */
    public InterfaceCli getClient() {
        return client;
    }

    /**
     * Retorna o id do cliente
     *
     * @return do tipo long com o id do cliente
     */
    public long getId() {
        return id;
    }

}
