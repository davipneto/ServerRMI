package bolsav;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A classe ServImpl implementa os métodos remotos da interface InterfaceServ e
 * implementa os métodos locais que trabalha com as ações dos clientes. O servidor
 * apenas armazena as ações que os clientes disponibilizam.
 *
 * @author Davi Pereira Neto
 * @author Geovana Franco Santos
 */
public class ServImpl extends UnicastRemoteObject implements InterfaceServ {

    //lista de ações e os clientes associados a elas
    public List<StockCli> stocks;

    /**
     * Construtor da classe, cria um registro do servidor e o deixa disponível,
     * e cria a tarefa que atualiza os preços das ações de forma randômica de
     * tempos em tempos.
     *
     * @throws RemoteException
     */
    public ServImpl() throws RemoteException {
        stocks = new ArrayList();
        //cria um registro do servidor no serviço de nomes na porta 1099
        Registry referenciaServicoNomes = LocateRegistry.createRegistry(1099);
        referenciaServicoNomes.rebind("RefServidor", this);
        //tarefa que atualiza os preços das ações
        TimerTask update = new TimerTask() {
            @Override
            public void run() {
                updatePrices();
            }
        };
        Timer t = new Timer();
        //a tarefa executa de 15 em 15 segundos
        t.schedule(update, 15000, 15000);
        System.out.println("Servidor funcionando");
    }

    /**
     * Método que atualiza os preços das ações presentes na lista local do
     * servidor de forma randômica
     */
    public void updatePrices() {
        //cria um randomGenerator
        Random randomGenerator = new Random();
        //para todas as ações presentes na lista de ações
        for (StockCli st : stocks) {
            //gera um boolean para saber se soma ou subtrai o valor da ação
            boolean op = randomGenerator.nextBoolean();
            //cria um número para ser adicionado ou somado
            int range = randomGenerator.nextInt(5);
            //pega o valor atual da ação
            double old = st.stock.actualPrice;
            //se op for true e o range maior q 0
            if (op && range > 0) { //faz a soma do preço atual com o range
                st.stock.actualPrice += range;
                //chama o método que notifica clientes inscritos que o preço subiu
                notifyClients(st, "rise", old, st.stock.actualPrice);
            } //se o preço atual não fica menor que o preço mínimo que o cliente deseja quando subtraido o range7
            //e se o range maior que 0
            else if (st.stock.actualPrice >= (range + st.stock.minPrice) && range > 0) { //faz a subtracao do preço atual com o range
                st.stock.actualPrice -= range;
                //chama o método que notifica clientes inscritos que o preço caiu
                notifyClients(st, "drop", old, st.stock.actualPrice);
            }
        }
    }

    /**
     * Método que formata a mensagem a ser enviada aos subscribers daquela
     * empresa depois do valor da ação ter sido modificada pelo servidor.
     *
     * @param sc com a ação e o cliente associada a ela
     * @param event com a situação do preço da ação (drop ou rise)
     * @param oldPrice com a valor antigo da ação
     * @param newPrice com o valor novo da ação
     */
    public void notifyClients(StockCli sc, String event, double oldPrice, double newPrice) {
        for (InterfaceCli subscriber : sc.subscribers) {
            try {
                //formata os preços das ações para enviar a mensagem
                NumberFormat formatter = new DecimalFormat("#0.00");
                String oldp = formatter.format(oldPrice).replace(',', '.');
                String newp = formatter.format(newPrice).replace(',', '.');
                //notifica os subscribers da ação
                subscriber.notify(event + " " + sc.stock.company + " " + oldp + " " + newp);
            } catch (RemoteException ex) {
                Logger.getLogger(ServImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Método de teste para verificar se conexão entre cliente e servidor
     * funciona.
     *
     * @param nome
     * @param cliente
     * @throws RemoteException
     */
    @Override
    public void chamar(String nome, InterfaceCli cliente) throws RemoteException {
        try {
            System.out.println("cliente invocou esse metodo");
            cliente.echo("Servidor chamando");
        } catch (RemoteException re) {
            System.out.println("Deu erro");
        }
    }

    /**
     * Insere nova ação recebida na lista de ações do servidor caso não exista
     * ou atualiza caso exista
     *
     * @param client com a referência do cliente
     * @param stock com a ação
     * @param id com o id do cliente
     * @throws RemoteException
     */
    @Override
    public void newStock(InterfaceCli client, Stock stock, long id) throws RemoteException {
        //verifica se a ação e o cliente associada com ela já existe na lista
        //se existe apenas atualiza a quantidade e a disponibilidade de venda
        for (StockCli sc : stocks) {
            if (sc.stock.company.equals(stock.company) && client.equals(sc.client)) {
                sc.stock.setQt(stock.getQt());
                sc.stock.setAvailable(true);
                return;
            }
        }
        //se não existe na lista adiciona uma nova
        StockCli sc = new StockCli(stock, client, id);
        stocks.add(sc);
    }

    /**
     * Retorna a lista de ações associadas aos clientes presentes no servidor
     *
     * @return do tipo lista de StockCli
     * @throws RemoteException
     */
    @Override
    public List<StockCli> getStocks() throws RemoteException {
        return stocks;
    }

    /**
     * Adiciona o cliente na lista de subscribers da ação
     *
     * @param client com a referência do cliente
     * @param company com o nome da empresa da ação
     * @throws RemoteException
     */
    @Override
    public void subscribe(InterfaceCli client, String company) throws RemoteException {
        for (StockCli st : stocks) {
            if (st.stock.company.equals(company)) {
                st.subscribers.add(client);
            }
        }
    }

    /**
     * Método que implementa a venda de uma ação, recebe a referência do cliente
     * que quer comprar e os dados da compra, tenta realizar a compra, caso não
     * seja bem sucedido agenda a tarefa de verificar na base de dados até
     * aparecer uma ação que bate com o desejado
     *
     * @param buyer com a referência do cliente que deseja comprar
     * @param company com a empresa da ação
     * @param maxPrice com o preço máximo de compra
     * @param qtde com a quantidade desejada
     * @throws RemoteException
     */
    @Override
    public void buy(InterfaceCli buyer, String company, double maxPrice, int qtde) throws RemoteException {
        //cria uma tarefa para tentar realizar a compra, caso não consiga de primeira ele executa de 5s em 5s
        //tentando realizar a compra
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (StockCli sc : stocks) {
                    Stock s = sc.stock;
                    if (s.company.equals(company) && s.isAvailable() && s.actualPrice >= s.minPrice && s.actualPrice <= maxPrice && buyer != sc.client) {
                        int qtde1 = 0;
                        //se a quantide da ação for menor do que a desejada transaciona o quanto ter
                        if (s.qt <= qtde) {
                            qtde1 = s.qt;
                        } else {
                            qtde1 = qtde;
                        }
                        NumberFormat formatter = new DecimalFormat("#0.00");
                        double price = (s.actualPrice + maxPrice) / 2;
                        String p = formatter.format(price).replace(',', '.');
                        try {
                            buyer.notify("buy " + company + " " + p + " " + qtde1 + " " + s.minPrice);
                            TimeUnit.SECONDS.sleep(2);
                            sc.client.notify("sell " + company + " " + p + " " + qtde1 + " " + s.minPrice);
                            this.cancel();
                        } catch (RemoteException ex) {
                            System.out.println("ex remote em buy");
                            Logger.getLogger(ServImpl.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                            System.out.println("ex interrupted em buy");
                            Logger.getLogger(ServImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        };
        Timer t = new Timer();
        t.schedule(task, 0, 10000);
    }

    /**
     * Método que apaga da lista de ações as ações do cliente que saiu do
     * mercado evitando que tente vender no futuro de um cliente inexistente
     *
     * @param client com a referência do cliente
     * @throws RemoteException
     */
    @Override
    public void logout(InterfaceCli client) throws RemoteException {
        for (StockCli st : stocks) {
            if (st.client.equals(client)) {
                stocks.remove(st);
            }
        }
    }
}
