package com.example.project;

import java.io.*;
import java.util.Arrays;

public class Solutions {
    int score;
    private String username;
    private String password;
    private int[] solutionId;
    private int quizId;
    static int id = 0;
    private int solId;
    public Solutions(String username, String password, int quizId, int[] solutionId) {
        this.username = username;
        this.password = password;
        id++;
        this.solId = id;
        this.quizId = quizId;
        this.solutionId = new int[solutionId.length];
        for (int i = 0; i < solutionId.length; i++) {
            this.solutionId[i] = solutionId[i];
        }
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
    public int[] getSolutionId() {
        return solutionId;
    }
    public int getQuizId() {
        return quizId;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public int getSolId() {
        return solId;
    }

    void writeSolToFile(String fileName) {
        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(this.solId + "," + this.username + "," + this.password + "," + this.quizId + "," +
                    Arrays.toString(this.solutionId).replace("[","").replace("]","").replace(" ","") + "," +
                    this.score);
            out.flush();

        }
        catch (IOException e)
        {
            System.out.println("Failed to write to file");
            e.printStackTrace();
        }
    }

}
