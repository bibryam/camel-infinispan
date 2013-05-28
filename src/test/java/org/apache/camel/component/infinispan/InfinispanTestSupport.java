package org.apache.camel.component.infinispan;

import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.infinispan.api.BasicCacheContainer;
import org.infinispan.manager.DefaultCacheManager;
import org.junit.Before;

public class InfinispanTestSupport extends CamelTestSupport {
    protected BasicCacheContainer basicCacheContainer;

    @Override
    @Before
    public void setUp() throws Exception {
        basicCacheContainer = new DefaultCacheManager();
        basicCacheContainer.start();
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        basicCacheContainer.stop();
        super.tearDown();
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();
        registry.bind("cacheContainer", basicCacheContainer);
        return registry;
    }
}
