package org.apache.camel.component.infinispan;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class InfinispanProducerTest extends InfinispanTestSupport {

    @Test
    public void producerPublishesKeyAndValue() throws Exception {
        template.send("direct:start", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(InfinispanConstants.KEY, "keyOne");
                exchange.getIn().setHeader(InfinispanConstants.VALUE, "valueOne");
            }
        });

        Object value = basicCacheContainer.getCache().get("keyOne");
        assertThat(value.toString(), is("valueOne"));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start")
                        .to("infinispan://localhost?cacheContainer=#cacheContainer");
            }
        };
    }
}
