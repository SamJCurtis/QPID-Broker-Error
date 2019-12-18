import java.io.IOException;

public class Main {


    public static void main(String args[]) throws Exception {
        EmbeddedInMemoryQpidBroker broker = new EmbeddedInMemoryQpidBroker();
        broker.start();

        BrokerConnector connector = new BrokerConnector();
        connector.createQueues();

    }


}
