package org.fao.fenix.dissertation.services.rest;

import org.apache.log4j.Logger;
import org.fao.fenix.dissertation.services.bean.DatasourceBean;
import org.fao.fenix.dissertation.services.utils.ConfigReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
@Component
@Path("/download")
public class Downloader {

    @Autowired
    private ConfigReader configReader;

    private Logger L = Logger.getLogger(Downloader.class);

    @GET
    @Path("/modis")
    public Response getModis() throws Exception {

        /* Compute result */
        DatasourceBean modis = configReader.getDatasource("MODIS");

        String path = modis.getBaseUrl() + "/2000/049/MOD13A2.A2000049.h20v08.005.2006269161232.hdf";
        System.out.println(path);
        URL url = new URL(path);
        String userAgent = "Opera/9.63 (Windows NT 5.1; U; en) Presto/2.1.1";
        downloadFromUrl(url, "/Users/simona/Desktop/test.hdf", userAgent);

        /* Stream result */
        return Response.status(200).entity(path).build();

    }

    private void downloadFromUrl(URL url, String localFilename, String userAgent) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URLConnection urlConn = url.openConnection();
            if (userAgent != null)
                urlConn.setRequestProperty("User-Agent", userAgent);
            is = urlConn.getInputStream();
            fos = new FileOutputStream(localFilename);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) > 0)
                fos.write(buffer, 0, len);
        } finally {
            try {
                if (is != null)
                    is.close();
            } finally {
                if (fos != null)
                    fos.close();
            }
        }
    }

}