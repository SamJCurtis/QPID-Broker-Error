/*
 * THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO IBM CORP AND MAY NOT
 * BE REPRODUCED PUBLISHED OR DISCLOSED TO OTHERS WITHOUT ITS AUTHORIZATION.
 *
 *
 * Copyright © 2019. IBM Corporation.
 *
 *
 * All Rights Reserved. IBM is a trademark or registered trademarks of IBM Corporation or
 * its affiliates in the U.S. and other countries. Other names may be trademarks of their
 * respective owners.
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BrokerConnector {

    private static final Logger LOG = LoggerFactory.getLogger(BrokerConnector.class);

    //specifies host, username, password, and port for embedded qpid broker
    protected static String BROKER_URI = "amqp://guest:guest@127.0.0.1:5672";

    public void createQueues() throws Exception {

        //connect to embedded broker
        ConnectionFactory factory = createConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> args = new HashMap<>();
        //args.put("alternateExchange","test");
        args.put("x-qpid-dlq-enabled","true");
        channel.queueDeclare("Test Queue", true, false, false, args);

    }

    /**
     * This method creates a ConnectionFactory object using the URI specified by BROKER_URI. This is used by anything that wants to connect
     * to the embedded broker.
     *
     * @return a ConnectionFactory object with URI specified by BROKER_URI
     * @throws Exception
     */
    private ConnectionFactory createConnectionFactory() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(BROKER_URI);
        return factory;
    }



}
