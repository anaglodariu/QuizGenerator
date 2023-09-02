package com.example.project;

import java.io.*;

public class User {
    private String username;
    private String password;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }



    void writeUserToFile(String fileName) {
        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(this.username + "," + this.password);
            out.flush();

        }
        catch (IOException e)
        {
            System.out.println("Failed to write to file");
            e.printStackTrace();
        }
    }

    static boolean readFromFile(String fileName, String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] user = line.split(",");
                if (user[0].equals(username) && user[1].equals(password)) {
                    /*daca userul exista deja, atunci returnez true*/
                    br.close();
                    return true;
                }
            }
            br.close();
            return false;
        }
        catch (IOException e)
        {
            System.out.println("Failed to read from file");
            e.printStackTrace();
        }
        return false;
    }

}
