package org.fao.fenix.dissertation.services.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
@Provider
public class RESTErrorManager implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error in REST call:\n" + e.getMessage()).build();
    }

}