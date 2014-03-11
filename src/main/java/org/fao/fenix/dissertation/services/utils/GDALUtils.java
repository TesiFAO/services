package org.fao.fenix.dissertation.services.utils;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class GDALUtils {

    @Autowired
    private ConfigReader configReader;

    public List<String> getSubdataset(String filename) {
        List<String> l = new ArrayList<String>();
        return l;
    }

}