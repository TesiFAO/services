package org.fao.fenix.dissertation.services.utils;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class ConfigReader {

    private String configPath;

    public ConfigReader(Resource configPath) throws Exception {
        try {
            this.setConfigPath(configPath.getFile().getPath());
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void init() {
        System.out.println("adsadasdas");
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

}