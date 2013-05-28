package org.apache.camel.component.infinispan;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class InfinispanComponentTest extends InfinispanTestSupport {

    @Test
    public void consumerReceivedEntryCreatedEventNotifications() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(2);

        basicCacheContainer.getCache().put("keyOne", "valueOne");
        assertMockEndpointsSatisfied();
    }

    @Test
    public void producerPublishesKeyAndValue() throws Exception {
        template.send("direct:start", new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(InfinispanConstants.KEY, "keyTwo");
                exchange.getIn().setHeader(InfinispanConstants.VALUE, "valueTwo");
            }
        });

        Object value = basicCacheContainer.getCache().get("keyTwo");
        assertThat(value.toString(), is("valueTwo"));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("infinispan://localhost?cacheContainer=#cacheContainer&eventTypes=CACHE_ENTRY_CREATED")
                        .to("mock:result");

                from("direct:start")
                        .to("infinispan://localhost?cacheContainer=#cacheContainer");
            }
        };
    }
}
