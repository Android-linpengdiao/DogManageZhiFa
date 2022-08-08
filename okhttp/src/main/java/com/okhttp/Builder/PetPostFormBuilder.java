package com.okhttp.Builder;

import com.okhttp.request.PetPostFormRequest;
import com.okhttp.request.RequestCall;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PetPostFormBuilder extends OkHttpRequestBuilder<PetPostFormBuilder> implements HasParamsable {
    private List<FileInput> files = new ArrayList<>();

    @Override
    public RequestCall build() {
        return new PetPostFormRequest(url, tag, params, headers, files, id).build();
    }

    public PetPostFormBuilder files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(key, filename, files.get(filename)));
        }
        return this;
    }

    public PetPostFormBuilder addFile(String name, String filename, File file) {
        files.add(new FileInput(name, filename, file));
        return this;
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }

    @Override
    public PetPostFormBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PetPostFormBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }
}
