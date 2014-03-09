package org.fao.fenix.dissertation.rest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
@Component
@Path("/test")
public class RESTTest {

    private Logger LOGGER = Logger.getLogger(RESTTest.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    public Response sayHello(@PathParam("name") final String name) throws Exception {

        LOGGER.info("Hallo world!");

        // Initiate the stream
        StreamingOutput stream = new StreamingOutput() {

            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {

                // compute result
                Writer writer = new BufferedWriter(new OutputStreamWriter(os));
                writer.write("Hallo " + name);

                // Convert and write the output on the stream
                writer.flush();

            }

        };

        // Stream result
        return Response.status(200).entity(stream).build();

    }

}