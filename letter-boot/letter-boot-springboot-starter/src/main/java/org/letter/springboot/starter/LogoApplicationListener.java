package org.letter.springboot.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Letter Welcome Logo {@link ApplicationListener}
 *
 * @author letter
 * @see ApplicationListener
 * @since 2.7.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 20 + 1)  // After LoggingApplicationListener#DEFAULT_ORDER
public class LogoApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static AtomicBoolean processed = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {

         if (processed.get()) {
            return;
        }

        /**
         * Gets Logger After LoggingSystem configuration ready
         * @see LoggingApplicationListener
         */
        final Logger logger = LoggerFactory.getLogger(getClass());

        String bannerText = buildBannerText();

        if (logger.isInfoEnabled()) {
            logger.info(bannerText);
        } else {
            System.out.print(bannerText);
        }

        // mark processed to be true
        processed.compareAndSet(false, true);
    }

    String buildBannerText() {

        StringBuilder bannerTextBuilder = new StringBuilder();
		bannerTextBuilder.append("\n");
		bannerTextBuilder.append(" :: letter app starter \n");
        return bannerTextBuilder.toString();

    }

}
