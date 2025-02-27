package com.example.demo.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;

import static com.example.demo.route.RouteConfiguration.FILE_IN_PROCESS;

@Component
public class FileProcessor implements Processor {

    @Value("${FilesPath: json}")
    private String newFileDirectory;

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setBody(null);
        File folder = new File(newFileDirectory);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        String json = Files.readString(file.toPath());
                        exchange.getIn().setBody(json);
                        exchange.getIn().getExchange().setProperty(FILE_IN_PROCESS, moveFileTo("processed", file));
                        return;
                    }
                }
            }
        }
    }

    public File moveFileTo(String folderName, File file) {
        File jsonFolder = new File(newFileDirectory);
        File folder = new File(jsonFolder.getParentFile().getAbsolutePath() + "/" + folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File newFile = new File(folder.getAbsolutePath() + "/" + file.getName());
        if (folder.exists() && folder.isDirectory() && file.exists() && !file.isDirectory()) {
            file.renameTo(newFile);
        }
        return newFile;
    }
}
