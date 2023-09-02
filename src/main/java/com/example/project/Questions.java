package com.example.project;

import java.io.*;
import java.nio.charset.Charset;

public class Questions {
    private String[] answers;
    private int[]  isCorrect;
    private int[] answerId;
    private String username;
    private String password;
    private String type;
    private String text;
    private int qId;
    static int id = 0;
    static int answersId = 0;
    public Questions(String text, String type, String[] answers, int[] isCorrect) {
        this.text = text;
        this.type = type;
        id++;
        this.qId = id;
        this.answers = new String[answers.length];
        this.answerId = new int[answers.length];
        this.isCorrect = new int[isCorrect.length];
        for (int i = 0; i < answers.length; i++) {
            this.answers[i] = answers[i]; /*vector cu raspunsurile la o intrebare*/
            this.isCorrect[i] = isCorrect[i]; /*vector cu solutiile fiecarui raspuns : raspuns corect/gresit = 1/0 */
            this.answerId[i] = ++answersId; /*vector cu id-urile raspunsurilor*/
            /*pe pozitia i din fiecare vector retin cate un raspuns, valoarea lui de adevar si id-ul lui*/
        }
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String[] getAnswers() {
        return answers;
    }
    public void setAnswers(String[] answers) {
        this.answers = answers;
    }
    public int[] getAnswerId() {
        return answerId;
    }
    public void setAnswerId(int[] answerId) {
        this.answerId = answerId;
    }
    public int getqId() {
        return qId;
    }
    public void setqId(int qId) {
        this.qId = qId;
    }
    public int[] getIsCorrect() {
        return isCorrect;
    }

    void writeQToFile(String fileName) {
        try (FileWriter fw = new FileWriter(fileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.print(this.qId + "," + this.text + "," + this.type + "," + this.answers.length + ",");
            for (int i = 0; i < this.answers.length; i++) {
                if (i == this.answers.length - 1) {
                    out.print(this.answerId[i] + "," + this.answers[i]);
                } else {
                    out.print(this.answerId[i] + "," + this.answers[i] + ",");
                }
            }
            out.println();
            out.flush();
        }
        catch (IOException e)
        {
            System.out.println("Failed to write to file");
            e.printStackTrace();
        }
    }
    static boolean readFromFile(String fileName, String text) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] q = line.split(",");
                if (q[1].equals(text)) {
                    /*daca intrebarea cu acel text exista deja, atunci returnez true*/
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

    static int getId(String fileName, String text) {
        int id = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] q = line.split(",");
                if (q[1].equals(text)) {
                    /*gasesc intrebarea si returnez id-ul*/
                    id = Integer.parseInt(q[0]);
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

    static String getAllQ(String fileName) {
        String string = "";
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] q = line.split(",");
                string += "{\"question_id\" : \"" + q[0] + "\", \"question_name\" : \"" + q[1] + "\"}";
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

    static String getDetails(String fileName, int id) {
        String details = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] q = line.split(",");
                if (q[0].equals(Integer.toString(id))) {
                    /*gasesc intrebarea cu id-ul cerut si returnez detalii despre ea*/
                    details = "{\"question-name\":\"" + q[1] + "\", \"question_index\":\"" + q[0] + "\", \"question_type\":\"" + q[2] + "\", \"answers\":\"[";
                    for (int i = 0 ; i < Integer.parseInt(q[3]); i++) {
                        details += "{\"answer_name\":\"" + q[4 + 2 * i + 1] + "\", \"answer_id\":\"" + q[4 + 2 * i] + "\"}";
                        if (i < Integer.parseInt(q[3]) - 1) {
                            details += ", ";
                        }
                    }
                    details += "]\"}";
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
        return details;
    }
}
