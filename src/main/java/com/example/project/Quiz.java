package com.example.project;

import java.io.*;
import java.util.Arrays;

public class Quiz {
    private String name;
    private String username;
    private String password;
    private int[] questionsId;
    private int quizId;
    static int id = 0;

    public Quiz(String username, String password, String name, int[] questionsId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.questionsId = new int[questionsId.length];
        for (int i = 0 ; i < questionsId.length; i++) {
            this.questionsId[i] = questionsId[i];
        }
        id++;
        this.quizId = id;
    }
    public String getUsername() {
        return name;
    }
    public String getName() {
        return name;
    }
    public int[] getQuestionsId() {
        return questionsId;
    }
    public int getQuizId() {
        return quizId;
    }


    void writeQuizToFile(String fileName) {
        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(this.quizId + "," + this.name + "," + this.questionsId.length + "," +
                    Arrays.toString(this.questionsId).replace("[","").replace("]","").replace(" ",""));
            out.flush();
        }
        catch (IOException e)
        {
            System.out.println("Failed to write to file");
            e.printStackTrace();
        }
    }
    static boolean readFromFile(String fileName, String name) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] user = line.split(",");
                if (user[1].equals(name)) {
                    /*daca quiz-ul cu acel nume exista deja, atunci returnez true*/
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

    static int getId(String fileName, String name) {
        int id = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] quiz = line.split(",");
                if (quiz[1].equals(name)) {
                    /*gasesc quiz-ul si returnez id-ul*/
                    id = Integer.parseInt(quiz[0]);
                    br.close();
                    break;
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Failed to get id");
            e.printStackTrace();
        }
        return id;
    }

    static String getAllQuizez(String fileName) {
        String string = "";
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] quiz = line.split(",");
                int ok = 0;
                String username = "";
                for (int i = 0 ; i < Tema1.quizzes.size() ; i++) {
                    if (Tema1.quizzes.get(i).getQuizId() == Integer.parseInt(quiz[0])) {
                        username = Tema1.quizzes.get(i).username; /*gasesc ce user a creat quiz-ul*/
                        break;
                    }
                }
                for (int i = 0 ; i < Tema1.solutions.size() ; i++) {
                    if (Tema1.solutions.get(i).getQuizId() == Integer.parseInt(quiz[0]) && Tema1.solutions.get(i).getUsername().equals(username)) {
                        ok = 1; /*daca quiz-ul a fost rezolvat de catre user, atunci ok = 1*/
                        string += "{\"quizz_id\" : \"" + quiz[0] + "\", \"quizz_name\" : \"" + quiz[1] + "\", \"is_completed\" : \"True\"}";
                        break;
                    }
                }
                if (ok == 0) {
                    string += "{\"quizz_id\" : \"" + quiz[0] + "\", \"quizz_name\" : \"" + quiz[1] + "\", \"is_completed\" : \"False\"}";
                }
                count ++;
                if (count < id) {
                    string += ", ";
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Failed to get id");
            e.printStackTrace();
        }
        return string;
    }

    static int[] getIds(String fileName, int id) {
        int[] ids = new int[0];
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] quiz = line.split(",");
                if (quiz[0].equals(Integer.toString(id))) {
                    /*pentru id-ul cerut, returnez un vector cu id-urile intrebarilor din acel quiz*/
                    int numberOfQuestions = Integer.parseInt(quiz[2]);
                    ids = new int[numberOfQuestions];
                    for (int i = 0 ; i < numberOfQuestions ; i++) {
                        ids[i] = Integer.parseInt(quiz[3 + i]);
                    }
                    br.close();
                    break;
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("Failed to get id");
            e.printStackTrace();
        }
        return ids;
    }
}
