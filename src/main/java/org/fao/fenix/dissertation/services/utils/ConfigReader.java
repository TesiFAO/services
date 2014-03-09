package org.fao.fenix.dissertation.services.utils;

import com.google.gson.stream.JsonReader;
import org.fao.fenix.dissertation.services.bean.DatasourceBean;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:guido.barbaglia@fao.org">Guido Barbaglia</a>
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 */
public class ConfigReader {

    private String configPath;

    private Map<String, DatasourceBean> datasources;

    public ConfigReader(Resource configPath) throws Exception {
        datasources = new HashMap<String, DatasourceBean>();
        try {
            this.setConfigPath(configPath.getFile().getPath());
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void init() throws IOException {
        File root = new File(this.configPath);
        File[] files = root.listFiles();
        for (int i = 0; i < files.length; i++) {
            InputStream is = new FileInputStream(files[i].getAbsoluteFile());
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            reader.beginArray();
            while (reader.hasNext()) {
                DatasourceBean b = readMessage(reader);
                this.datasources.put(b.getId(), b);
            }
            reader.endArray();
        }
    }

    public DatasourceBean readMessage(JsonReader reader) throws IOException {
        DatasourceBean b = new DatasourceBean();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equalsIgnoreCase("id")) {
                b.setId(reader.nextString());
            } else if (name.equalsIgnoreCase("baseUrl")) {
                b.setBaseUrl(reader.nextString());
            } else if (name.equalsIgnoreCase("startIdx")) {
                b.setStartIdx(reader.nextInt());
            } else if (name.equalsIgnoreCase("endIdx")) {
                b.setEndIdx(reader.nextInt());
            } else if (name.equalsIgnoreCase("minYear")) {
                b.setMinYear(reader.nextInt());
            } else if (name.equalsIgnoreCase("maxYear")) {
                b.setMaxYear(reader.nextInt());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return b;
    }

    public DatasourceBean getDatasource(String id) {
        return this.datasources.get(id.toUpperCase());
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

}