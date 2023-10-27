package ru.fisenko.shareAll.integration;

import org.springframework.stereotype.Component;

import java.io.*;


//Сan use any cloud storage (Amazon S3, Google Cloud Storage, Azure Blob Storage and etc.)
@Component
public class FileCRUD {
    private String defaultPath = "storage/";

    public void create(String path, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(defaultPath + path))) {
            writer.write(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public String read(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(defaultPath + path))) {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "File not found";
        }
    }


    public void update(String path, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(defaultPath + path))) {
            writer.write(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void delete(String path) {
        File file = new File(defaultPath + path);
        try {
            if (file.exists())
                file.delete();
            else System.out.println("Can't delete file: " + defaultPath + path);
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }
}