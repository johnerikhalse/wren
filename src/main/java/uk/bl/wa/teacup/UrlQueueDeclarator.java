/**
 * 
 */
package uk.bl.wa.teacup;

import io.latent.storm.rabbitmq.Declarator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;

/**
 * @author Andrew Jackson <Andrew.Jackson@bl.uk>
 *
 */
public class UrlQueueDeclarator implements Declarator {

    private static final long serialVersionUID = 3021760909065861611L;

    private final String exchange;
    private final String queue;
    private final String routingKey;

    public UrlQueueDeclarator(String exchange, String queue) {
        this(exchange, queue, "");
    }

    public UrlQueueDeclarator(String exchange, String queue, String routingKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
    }

    @Override
    public void execute(Channel channel) {
        // you're given a RabbitMQ Channel so you're free to wire up your
        // exchange/queue bindings as you see fit
        try {
            Map<String, Object> args = new HashMap<>();
            channel.queueDeclare(queue, true, false, false, args);
            channel.exchangeDeclare(exchange, "topic", true);
            channel.queueBind(queue, exchange, routingKey);
        } catch (IOException e) {
            throw new RuntimeException("Error executing rabbitmq declarations.", e);
        }
    }
}