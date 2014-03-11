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
import java.util.ArrayList;
import java.util.StringTokenizer;

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

    @GET
    @Path("/modis/subdatasets/{filename}")
    @Produces("application/json")
    public Response getModisSubdatasets(@PathParam("filename") String filename) throws Exception {

        /* Compute result */
        DatasourceBean modis = configReader.getDatasource("MODIS");

        /* Use GDAL */
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec("gdalinfo " + modis.getDownloadDir() + File.separator + filename + ".hdf");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        int start_idx = output.indexOf("Subdatasets:");
        String subdatasets = output.substring(start_idx + "Subdatasets:".length()).trim();
        StringTokenizer t = new StringTokenizer(subdatasets, "\n");
        final ArrayList<String> l = new ArrayList<String>();
        while (t.hasMoreElements()) {
            String tmp = t.nextElement().toString();
            if (tmp.contains("NAME="))
                l.add(escapeSpaces(tmp.substring(tmp.indexOf("NAME=") + "NAME=".length())));
        }

        /* Stream the output */
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                /* Compute result */
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));

                /* Write the result of the query */
                writer.write("[");
                for (int i = 0 ; i < l.size() ; i++) {
                    writer.write(g.toJson(l.get(i)));
                    if (i < l.size() - 1)
                        writer.write(",");
                }
                writer.write("]");

                /* Convert and write the output on the stream */
                writer.flush();

            }

        };

        /* Stream result */
        return Response.status(200).entity(stream).build();

    }

    public String escapeSpaces(String s) {
        String t = "";
        for (int i = 0 ; i < s.length() ; i++) {
            if (s.charAt(i) == ' ') {
                t += "\\ ";
            } else {
                t += s.charAt(i);
            }
        }
        return t;
    }

}