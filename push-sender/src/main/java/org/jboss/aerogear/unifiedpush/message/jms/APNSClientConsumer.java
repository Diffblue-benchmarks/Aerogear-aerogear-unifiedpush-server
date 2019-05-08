package org.jboss.aerogear.unifiedpush.message.jms;

import org.jboss.aerogear.unifiedpush.api.iOSVariant;
import org.jboss.aerogear.unifiedpush.message.cache.SimpleApnsClientCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.inject.Inject;

public class APNSClientConsumer extends AbstractJMSMessageListener<iOSVariant> {

    @Inject
    SimpleApnsClientCache apnsClientCache;

    private static final Logger logger = LoggerFactory.getLogger(APNSClientConsumer.class);

    @Override
    public void onMessage(iOSVariant iOSVariant) {
        logger.info("Resetting iOS with new cert");
        apnsClientCache.disconnectOnChange(iOSVariant);
    }

}
