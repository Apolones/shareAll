package ru.fisenko.shareAll.integration;

import org.springframework.stereotype.Component;

import java.io.*;


//Сan use any cloud storage (Amazon S3, Google Cloud Storage, Azure Blob Storage and etc.)
@Component
public class FileCRUD {
    private String defaultPath = "storage/";
    public void create(String path, String data){
        try {
            FileWriter writer = new FileWriter(defaultPath + path);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String read(String path) {
        String data;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(defaultPath + path));
            data = reader.readLine();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public void update(String path, String data) {
        try {
            FileWriter writer = new FileWriter(defaultPath + path, false);
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String path){

        File file = new File(defaultPath + path);
        if(file.exists())
            file.delete();
    }

    public void setDefaultPath(String defaultPath){
        this.defaultPath=defaultPath;
    }
}