package org.fao.fenix.dissertation.services.rest;

import it.sauronsoftware.ftp4j.FTPClient;
import org.apache.log4j.Logger;
import org.fao.fenix.dissertation.services.bean.DatasourceBean;
import org.fao.fenix.dissertation.services.utils.ConfigReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.File;

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
    @Path("/modis/{year}/{day}/{filename}")
    public Response getModis(@PathParam("year") String year, @PathParam("day") String day, @PathParam("filename") String filename) throws Exception {

        /* Compute result */
        DatasourceBean modis = configReader.getDatasource("MODIS");

        /* Connect to FTP */
        FTPClient client = new FTPClient();
        client.connect(modis.getFtpUrl());
        client.login("anonymous", "FAO");
        client.changeDirectory(modis.getBaseUrl() + "/" + year + "/" + day);
        client.download(filename, new java.io.File(modis.getDownloadDir() + File.separator + filename));

        /* Disconnect from FTP */
        client.disconnect(true);

        /* Stream result */
        String m = filename;
        return Response.status(200).entity(m).build();

    }

}