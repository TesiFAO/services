package org.fao.fenix.dissertation.services.rest;

import com.google.gson.Gson;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;
import org.apache.log4j.Logger;
import org.fao.fenix.dissertation.services.bean.DatasourceBean;
import org.fao.fenix.dissertation.services.utils.ConfigReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
@Component
@Path("/browse")
public class Browser {

    @Autowired
    private ConfigReader configReader;

    private Logger L = Logger.getLogger(Browser.class);

    private final Gson g = new Gson();

    @GET
    @Path("/modis/{year}/{day}")
    @Produces("application/json")
    public Response getModis(@PathParam("year") String year, @PathParam("day") String day) throws Exception {

        /* Compute result */
        DatasourceBean modis = configReader.getDatasource("MODIS");

        /* Connect to FTP */
        FTPClient client = new FTPClient();
        client.connect(modis.getFtpUrl());
        client.login("anonymous", "FAO");
        client.changeDirectory(modis.getBaseUrl() + "/" + year + "/" + day);
        final FTPFile[] list = client.list();

        /* Stream the output */
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                /* Compute result */
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                /* Write the result of the query */
                writer.write("[");
                for (int i = 0 ; i < list.length ; i++) {
                    writer.write(g.toJson(list[i].getName()));
                    if (i < list.length - 1)
                        writer.write(",");
                }
                writer.write("]");

                /* Convert and write the output on the stream */
                writer.flush();

            }

        };

        /* Disconnect from FTP */
        client.disconnect(true);

        /* Stream result */
        return Response.status(200).entity(stream).build();

    }

}