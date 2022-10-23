package org.letter.common.config.configcenter;

import org.letter.common.URL;
import org.letter.common.extension.ExtensionLoader;

public class ConfigCenterTest {
    public static void main(String[] args) {
        String GROUP = "letter";
        DynamicConfigurationFactory cfgFactory = ExtensionLoader.getExtensionLoader(DynamicConfigurationFactory.class)
                .getExtension("file");
        DynamicConfiguration dynamicConfiguration = cfgFactory.getDynamicConfiguration(URL.valueOf("letter://127.0.0.1:8080"));
        dynamicConfiguration.publishConfig("111", GROUP, "123");
        dynamicConfiguration.getConfig("111", GROUP);
        System.out.println(dynamicConfiguration.getConfig("111", GROUP));
    }

}
