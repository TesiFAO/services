package org.fao.fenix.dissertation.services.utils;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class GDALTest extends TestCase {

    public void _testGDAL() {
        StringBuffer output = new StringBuffer();
        Process p;
        try {
            p = Runtime.getRuntime().exec("gdalinfo /home/kalimaha/Desktop/MODIS/TEST.hdf");
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
        ArrayList<String> l = new ArrayList<String>();
        while (t.hasMoreElements()) {
            String tmp = t.nextElement().toString();
            if (tmp.contains("NAME="))
                l.add(tmp.substring(tmp.indexOf("NAME=") + "NAME=".length()));
        }
        for (String s : l)
            System.out.println(s);

    }

    public void testReplace() {
        String s = "HDF4_EOS:EOS_GRID:\"/home/kalimaha/Desktop/MODIS/TEST.hdf\":MODIS_Grid_16DAY_1km_VI:1 km 16 days NDVI";
        s = s.replaceAll(" ", "\\ ");
        System.out.println(s);
        String t = "";
        for (int i = 0 ; i < s.length() ; i++) {
            if (s.charAt(i) == ' ') {
                t += "\\ ";
            } else {
                t += s.charAt(i);
            }
        }
        System.out.println(t);
    }

}