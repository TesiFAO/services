package org.fao.fenix.dissertation.services.utils;

import it.sauronsoftware.ftp4j.*;
import junit.framework.TestCase;

import java.io.IOException;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class FTPClientTest extends TestCase {

    public void testFTP() {
        try {
            FTPClient client = new FTPClient();
            client.connect("ladsweb.nascom.nasa.gov");
            client.login("anonymous", "ftp4j");
            client.changeDirectory("/allData/5/MOD13A2/2012/001/");
            FTPFile[] list = client.list();
            for (FTPFile f : list)
                System.out.println(f.getName());
            client.disconnect(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
        } catch (FTPException e) {
            e.printStackTrace();
        } catch (FTPDataTransferException e) {
            e.printStackTrace();
        } catch (FTPAbortedException e) {
            e.printStackTrace();
        } catch (FTPListParseException e) {
            e.printStackTrace();
        }
    }

}