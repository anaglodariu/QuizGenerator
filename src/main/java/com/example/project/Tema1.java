package com.example.project;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Tema1 {

	static ArrayList<Questions> questions = new ArrayList<>();
	static ArrayList<User> users = new ArrayList<>();
	static ArrayList<Quiz> quizzes = new ArrayList<>();
	static ArrayList<Solutions> solutions = new ArrayList<>();

	/*calculez cate raspunsuri corecte are fiecare intrebare*/
	static int numberOfCorrectAnswers(int[] sol) {
		int correctAnswers = 0;
		for (int j : sol) {
			if (j == 1) {
				correctAnswers++;
			}
		}
		return correctAnswers;
	}

	static double calculateScoreQuestion(int[] answerId, int[] isCorrect, int correctAnswers) {
		double qscore = 0d; /*punctajul pe fiecare intrebare*/
		int answers = answerId.length; /*cate raspunsuri are intrebarea in total*/
		double pondereCorrect = 1d / correctAnswers; /*calculam ponderea raspunsurilor corecte*/
		double pondereWrong = 1d / (answers - correctAnswers); /*calculam ponderea raspunsurilor gresite*/
		for (int l = 0 ; l < answerId.length ; l++) {
			for (int m = 0 ; m < solutions.get(solutions.size() - 1).getSolutionId().length ; m++) {
				if (answerId[l] == solutions.get(solutions.size() - 1).getSolutionId()[m]) {
					if (isCorrect[l] == 1) {
						qscore += pondereCorrect; /*daca raspunsul este corect, se adauga ponderea 'pondereCorrect'*/
					} else {
						qscore -= pondereWrong; /*daca raspunsul este gresit, se scade ponderea 'pondereWrong'*/
					}
					break;
				}
			}
		}
		return qscore;
	}

	static int calculateScoreQuiz(int quizId) {
		double score = 0d;
		int indexQuiz = 0;
		for (int i = 0 ; i < quizzes.size() ; i++) {
			if (quizzes.get(i).getQuizId() == quizId) { /*gasim quiz-ul la care s-a raspuns dupa id*/
				indexQuiz = i;
				break;
			}
		}
		int[] questionsId = quizzes.get(indexQuiz).getQuestionsId(); /*luam intrebarile din quiz*/
		double pondereQuestion = 100d / questionsId.length; /*calculam ponderea fiecarei intrebari*/
		for (int j = 0 ; j < questionsId.length ; j++) {
			for (int k = 0 ; k < questions.size() ; k++) {
				if (questions.get(k).getqId() == questionsId[j]) {
					int correctAnswers = numberOfCorrectAnswers(questions.get(k).getIsCorrect()); /*cate raspunsuri corecte are intrebarea*/
					int[] answerId = questions.get(k).getAnswerId(); /*luam fiecare raspuns din intrebare*/
					int[] isCorrect = questions.get(k).getIsCorrect(); /*luam valorile de adevar ale raspunsurilor*/
					double qscore = calculateScoreQuestion(answerId, isCorrect, correctAnswers);
					score += qscore * pondereQuestion; /*se adauga punctajul pe intrebare la punctajul total*/
					break;
				}
			}
		}
		if (score < 0d) {
			return 0;
		} else {
			return (int)Math.round(score);
		}
	}
	static boolean quizExists(int quizId) {
		for (int i = 0 ; i < quizzes.size() ; i++) {
			if (quizzes.get(i).getQuizId() == quizId) {
				return true;
			}
		}
		return false;
	}

	static boolean quizAlreadyCompleted(int quizId, String username) {
		for (int i = 0 ; i < solutions.size() ; i++) {
			if (solutions.get(i).getQuizId() == quizId && solutions.get(i).getUsername().equals(username)) {
				return true; /*daca quiz-ul a fost deja completat de catre user*/
			}
		}
		return false;
	}

	static boolean cannotAnswerQuiz(int quizId, String username) {
		for (int i = 0 ; i < quizzes.size() ; i++) {
			if (quizzes.get(i).getQuizId() == quizId) {
				if (quizzes.get(i).getUsername().equals(username)) {
					return true; /*daca user-ul este autorul quiz-ului*/
				}
				break;
			}
		}
		return false;
	}

	static String getQuizName(int quizId) {
		for (int i = 0 ; i < quizzes.size() ; i++) {
			if (quizzes.get(i).getQuizId() == quizId) {
				return quizzes.get(i).getName();
			}
		}
		return null;
	}

	static String printSolDetails(String username) {
		String result = "";
		int count = 0; /*numarul de quiz-uri completate de catre user-ul dat*/
		for (int i = 0; i < solutions.size(); i++) {
			if (solutions.get(i).getUsername().equals(username)) {
				count++;
			}
		}
		int j = 0;
		for (int i = 0; i < solutions.size(); i++) {
			if (solutions.get(i).getUsername().equals(username)) {
				String quizName = getQuizName(solutions.get(i).getQuizId());
				result += "{\"quiz-id\" : \"" + solutions.get(i).getQuizId() +
						"\", \"quiz-name\" : \"" + quizName + "\", \"score\" : \"" + solutions.get(i).getScore() +
						"\", \"index_in_list\" : \"" + solutions.get(i).getSolId() + "\"}";
				if (j < count - 1) {
					result += ", ";
				}
				j++;
			}
		}
		return result;
	}

	static void updateLists(int quizId) {
		/*elimin quiz-ul cu id-ul quizId din 'quizzez' si din 'solutions'*/
		for (int i = 0 ; i < quizzes.size() ; i++) {
			if (quizzes.get(i).getQuizId() == quizId) {
				quizzes.remove(i);
				break;
			}
		}
		for (int i = 0 ; i < solutions.size() ; i++) {
			if (solutions.get(i).getQuizId() == quizId) {
				solutions.remove(i);
				i--;
			}
		}
	}

	static void updateQuizFile(int index, String quizName) {
		try (FileWriter fw = new FileWriter(quizName, true);
			 BufferedWriter bw = new BufferedWriter(fw);
			 PrintWriter out = new PrintWriter(bw)) {
			out.println(quizzes.get(index).getQuizId() + "," + quizzes.get(index).getName() + "," +
					quizzes.get(index).getQuestionsId().length + "," +
					Arrays.toString(quizzes.get(index).getQuestionsId()).replace("[","").replace("]","").replace(" ",""));
			out.flush();
		}
		catch (IOException e)
		{
			System.out.println("Failed to write to file");
			e.printStackTrace();
		}
	}

	static void updateSolutionsFile(int index, String solName) {
		try (FileWriter fw = new FileWriter(solName, true);
			 BufferedWriter bw = new BufferedWriter(fw);
			 PrintWriter out = new PrintWriter(bw)) {
			out.println(solutions.get(index).getSolId() + "," + solutions.get(index).getUsername() + "," +
					solutions.get(index).getPassword() + "," + solutions.get(index).getQuizId() + "," +
					Arrays.toString(solutions.get(index).getSolutionId()).replace("[","").replace("]","").replace(" ","") + "," +
					solutions.get(index).getScore());
			out.flush();
		}
		catch (IOException e)
		{
			System.out.println("Failed to write to file");
			e.printStackTrace();
		}
	}
	static void updateFiles(String quizName, String solName) {
		/*daca elimin un quiz modific si fisierele*/
		/*scriu in fisiere ce am in 'quizzes' si 'solutions' dupa ce le fac update*/
		emptyFile(quizName);
		emptyFile(solName);
		for (int i = 0 ; i < quizzes.size() ; i++) {
			updateQuizFile(i, quizName);
		}
		for (int i = 0 ; i < solutions.size() ; i++) {
			updateSolutionsFile(i, solName);
		}
	}

	/*functie cu care curat fisierele de tip csv*/
	static void emptyFile(String fileName) {
		try (FileWriter fw = new FileWriter(fileName);
			 BufferedWriter bw = new BufferedWriter(fw);
			 PrintWriter out = new PrintWriter(bw)) {
			out.print("");
			out.flush();
		}
		catch (IOException e)
		{
			System.out.println("Failed to empty file");
			e.printStackTrace();
		}
	}

	/*verific daca o intrebare are raspunsurile duplicate*/
	static boolean duplicateAnswers (String[] answerText) {
		for (int i = 0 ; i < answerText.length - 1 ; i++) {
			for (int j = i + 1 ; j < answerText.length ; j++) {
				if (Objects.equals(answerText[i], answerText[j])) {
					return true;
				}
			}
		}
		return false;
	}

	static int contor;
	static void find(int[] answerId, int[] found) {
		/*cand dau un id valid (dat ca argument la submit-quizz) il pun in vectorul found*/
		for (int m = 0 ; m < solutions.get(solutions.size() - 1).getSolutionId().length ; m++) {
			for (int n = 0 ; n < answerId.length ; n++) {
				if (solutions.get(solutions.size() - 1).getSolutionId()[m] == answerId[n]) {
					found[contor++] = answerId[n];
				}
			}
		}
	}

	static int findAnswersId(int quizId) {

		int indexQuiz = 0;
		for (int i = 0 ; i < quizzes.size() ; i++) {
			if (quizzes.get(i).getQuizId() == quizId) { /*gasim quiz-ul la care s-a raspuns dupa id*/
				indexQuiz = i;
				break;
			}
		}
		/*retin id-urile intrebarilor din quiz-ul la care s-a raspuns*/
		int[] found = new int[solutions.get(solutions.size()- 1).getSolutionId().length];
		int[] questionsId = quizzes.get(indexQuiz).getQuestionsId(); /*luam intrebarile din quiz*/
		for (int j = 0 ; j < questionsId.length ; j++) {
			for (int k = 0 ; k < questions.size() ; k++) {
				if (questions.get(k).getqId() == questionsId[j]) {
					find(questions.get(k).getAnswerId(), found);
				}
			}
		}
		/*daca in vectorul found am gasit toate id-urile, atunci sunt toate valide*/
		for (int m = 0 ; m < solutions.get(solutions.size() - 1).getSolutionId().length ; m++) {
			int ok = 0;
			for (int n = 0 ; n < found.length ; n++) {
				if (solutions.get(solutions.size() - 1).getSolutionId()[m] == found[n]) {
					ok = 1;
					break;
				}
			}
			if (ok == 0) {
				/*daca gasesc un id invalid (nu-l gasesc in vectorul found), il returnez*/
				return solutions.get(solutions.size() - 1).getSolutionId()[m];
			}
		}
		return -1;
	}

	public static void main(final String[] args)
	{
		if (args == null) {
			System.out.println("Hello world!");
			return;
		}
		int count = 0;
		for (String s: args) {
			count ++;
		}

		File fileUsers = new File("users.csv");
		File fileQ = new File("questions.csv");
		File fileQuiz = new File("quiz.csv");
		File fileSol = new File("solutions.csv");

		/*verific ca am argumente in linia de comanda si verific tipul lor*/
		if (count > 0 && Objects.equals(args[0], "-cleanup-all")) {
			emptyFile(fileUsers.getName());
			emptyFile(fileQ.getName());
			emptyFile(fileQuiz.getName());
			emptyFile(fileSol.getName());
			Questions.id = 0; /*resetez id-urile la 0*/
			Questions.answersId = 0;
			Quiz.id = 0;
			Solutions.id = 0;
			contor = 0;
			questions.clear(); /*golesc si listele (tot variabile statice)*/
			quizzes.clear();
			users.clear();
			solutions.clear();
		} else if (count > 0 && Objects.equals(args[0], "-create-user")) {
			/*creez user*/
			if (count == 1) {
				/*parametrii -u si -p nu sunt furnizati*/
				System.out.println("{ 'status' : 'error', 'message' : 'Please provide username'}");
			} else if (count == 2) {
				/*parametrul -p nu este frunizat*/
				System.out.println("{ 'status' : 'error', 'message' : 'Please provide password'}");
			} else if (count == 3) {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				/*verific daca userul exista deja*/
				if (User.readFromFile(fileUsers.getPath(), username, password)) {
					System.out.println("{ 'status' : 'error', 'message' : 'User already exists'}");
				} else {
					User user = new User(username, password);
					user.writeUserToFile(fileUsers.getPath());
					users.add(user);
					System.out.println("{ 'status' : 'ok', 'message' : 'User created successfully'}");
				}
			}
		} else if (count > 0 && Objects.equals(args[0], "-create-question")) {
			if (count == 1 || count == 2) {
				/*parametrul -u sau -p nu este furnizat*/
				System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			} else if (count == 3) {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				/*verific daca userul exista sau daca parola e corecta*/
				if (!User.readFromFile(fileUsers.getPath(), username, password)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
				}
			} else {
				/*intrebarea are minim 2 intrebari*/
				if (count > 15) {
					System.out.println("{ 'status' : 'error', 'message' : 'More than 5 answers were submitted'}");
					return;
				}
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				if (!User.readFromFile(fileUsers.getPath(), username, password)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					return;
				}
				if (!Objects.equals(args[3].split(" ")[0], "-text")) {
					System.out.println("{ 'status' : 'error', 'message' : 'No question text provided'}");
					return;
				}
				if (count == 4) {
					System.out.println("{ 'status' : 'error', 'message' : 'No question type provided'}");
					return;
				}
				String text = args[3].split(" '")[1].replace("'", "");
				String type = args[4].split(" ")[1].replace("'", "");
				int numberOfAnswers = count - 5;
				if (numberOfAnswers == 0) {
					/*intrebarea nu include niciun raspuns*/
					System.out.println("{ 'status' : 'error', 'message' : 'No answer provided'}");
					return;
				} else if (numberOfAnswers == 2) {
					/*intrebarea include un singur raspuns*/
					System.out.println("{ 'status' : 'error', 'message' : 'Only one answer provided'}");
					return;
				} else if (numberOfAnswers % 2 == 1) {
					for (int i = 0 ; i < numberOfAnswers ; i++) {
						String string = args[5 + i].split(" ")[0].replace("'", "");
						if (!string.equals("-answer-" + (i + 1)) && i % 2 == 0) {
							System.out.println("{ 'status' : 'error', 'message' : 'Answer " + (i + 1) + " has no answer description'}");
							return;
						}
						if (!string.equals("-answer-" + (i + 1) + "-is-correct") && i % 2 == 1) {
							System.out.println("{ 'status' : 'error', 'message' : 'Answer " + i + " has no answer correct flag'}");
							return;
						}
					}
				}
				int correctAnswers = 0;
				String[] answerText = new String[numberOfAnswers / 2];
				int[] isCorrect = new int[numberOfAnswers / 2];
				int contor = 0;
				for (int i = 0 ; i < numberOfAnswers ; i = i + 2) {
					answerText[contor] = args[5 + i].split(" ")[1].replace("'", "");
					String string = args[6 + i].split(" ")[1].replace("'", "");
					isCorrect[contor++] = Integer.parseInt(string);
					if (Objects.equals(string, "1")) {
						correctAnswers++;
					}
				}
				if (duplicateAnswers(answerText)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Same answer provided more than once'}");
				} else if (correctAnswers > 1 && Objects.equals(type, "single")) {
					System.out.println("{ 'status' : 'error', 'message' : 'Single correct answer question has more than one correct answer'}");
				} else if (Questions.readFromFile(fileQ.getPath(), text)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Question already exists'}");
				} else {
					Questions q = new Questions(text, type, answerText, isCorrect);
					questions.add(q);
					q.writeQToFile(fileQ.getName());
					System.out.println("{ 'status' : 'ok', 'message' : 'Question added successfully'}");
				}
			}
		} else if (count > 0 && Objects.equals(args[0], "-get-question-id-by-text")) {
			if (count == 1 || count == 2) {
				/*parametrul -u sau -p nu este furnizat*/
				System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			} else if (count == 3) {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				/*verific daca userul exista sau daca parola este corecta*/
				if (!User.readFromFile(fileUsers.getPath(), username, password)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
				}
			} else {
				String text = args[3].split(" '")[1].replace("'", "");
				if (!Questions.readFromFile(fileQ.getPath(), text)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Question does not exist'}");
				} else {
					int qId = Questions.getId(fileQ.getPath(), text);
					System.out.println("{ 'status' : 'ok', 'message' : '" + qId + "'}");
				}
			}
		} else if (count > 0 && Objects.equals(args[0], "-get-all-questions")) {
			if (count == 1 || count == 2) {
				/*parametrul -u sau -p nu este furnizat*/
				System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			} else if (count == 3) {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				/*verific daca userul exista sau daca parola este corecta*/
				if (!User.readFromFile(fileUsers.getPath(), username, password)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					return;
				}
				System.out.println("{ 'status' : 'ok', 'message' : '[" + Questions.getAllQ(fileQ.getName()) + "]'}");
			}
		} else if (count > 0 && Objects.equals(args[0], "-create-quizz")) {
			if (count == 1 || count == 2) {
				/*parametrul -u sau -p nu este furnizat*/
				System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			} else if (count == 3) {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				/*verific daca userul exista sau daca parola este corecta*/
				if (!User.readFromFile(fileUsers.getPath(), username, password)) {
					/*daca userul nu exista sau parola este incorecta*/
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
				}
			} else {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				String name = args[3].split(" '")[1].replace("'", "");
				int numberOfQuestions = count - 4;
				if (numberOfQuestions > 10) {
					System.out.println("{ 'status' : 'error', 'message' : 'Quizz has more than 10 questions'}");
				}
				int[] questionsId = new int[numberOfQuestions];
				for(int i = 0 ; i < numberOfQuestions ; i++) {
					questionsId[i] = Integer.parseInt(args[4 + i].split(" ")[1].replace("'", ""));
					if(questionsId[i] > Questions.id) {
						System.out.println("{ 'status' : 'error', 'message' : 'Question ID for question " + (i + 1) + " does not exist'}");
						return;
					}
				}
				if (Quiz.readFromFile(fileQuiz.getName(), name)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Quizz name already exists'}");
				} else {
					Quiz quiz = new Quiz(username, password, name, questionsId);
					quiz.writeQuizToFile(fileQuiz.getName());
					quizzes.add(quiz);
					System.out.println("{ 'status' : 'ok', 'message' : 'Quizz added succesfully'}");
				}
			}
		} else if (count > 0 && Objects.equals(args[0], "-get-quizz-by-name")) {
			if (count == 1 || count == 2) {
				/*parametrul -u sau -p nu este furnizat*/
				System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			} else if (count == 3) {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				/*verific daca userul exista*/
				if (!User.readFromFile(fileUsers.getPath(), username, password)) {
					/*daca userul nu exista sau parola este incorecta*/
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
				}
			} else {
				String name = args[3].split(" '")[1].replace("'", "");
				if (!Quiz.readFromFile(fileQuiz.getName(), name)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Quizz does not exist'}");
				} else {
					int quizId = Quiz.getId(fileQuiz.getName(), name);
					System.out.println("{ 'status' : 'ok', 'message' : '" + quizId + "'}");
				}
			}
		} else if (count > 0 && Objects.equals(args[0], "-get-all-quizzes")) {
			if (count == 1 || count == 2) {
				/*parametrul -u sau -p nu este furnizat*/
				System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			} else if (count == 3) {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				/*verific daca userul exista*/
				if (!User.readFromFile(fileUsers.getName(), username, password)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
					return;
				}
				System.out.println("{ 'status' : 'ok', 'message' : '[" + Quiz.getAllQuizez(fileQuiz.getName()) + "]'}");
			}
		} else if (count > 0 && Objects.equals(args[0], "-get-quizz-details-by-id")) {
			if (count == 1 || count == 2) {
				/*parametrul -u sau -p nu este furnizat*/
				System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			} else if (count == 3) {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				/*verific daca userul exista sau daca parola este corecta*/
				if (!User.readFromFile(fileUsers.getName(), username, password)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
				}
			} else {
				int quizId = Integer.parseInt(args[3].split(" ")[1].replace("'", ""));
				int[] ids = Quiz.getIds(fileQuiz.getName(), quizId);
				String output =	"";
				for (int i = 0 ; i < ids.length ; i++) {
					output += Questions.getDetails(fileQ.getName(), ids[i]);
					if (i < ids.length - 1) {
						output += ", ";
					}
				}
				System.out.println("{'status' : 'ok', 'message' : '[" + output + "]'}");
			}

		} else if (count > 0 && Objects.equals(args[0], "-submit-quizz")) {
			if (count == 1 || count == 2) {
				/*parametrul -u sau -p nu este furnizat*/
				System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			} else if (count == 3) {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				/*verific daca userul exista*/
				if (!User.readFromFile(fileUsers.getName(), username, password)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
				} else {
					System.out.println("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
				}
			} else {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				int quizId = Integer.parseInt(args[3].split(" ")[1].replace("'", ""));
				/*verific daca exista un quiz cu id-ul = quizId*/
				if (!quizExists(quizId)) {
					System.out.println("{ 'status' : 'error', 'message' : 'No quiz was found'}");
					return;
				}
				/*verific daca user-ul a completat deja chestionarul*/
				if (quizAlreadyCompleted(quizId, username)) {
					System.out.println("{ 'status' : 'error', 'message' : 'You already submitted this quizz'}");
					return;
				}
				/*user-ul nu isi poate raspunde la propriul quiz*/
				if (cannotAnswerQuiz(quizId, username)) {
					System.out.println("{ 'status' : 'error', 'message' : 'You cannot answer your own quizz'}");
					return;
				}
				int numberOfSolutions = count - 4;
				int[] solutionId = new int[numberOfSolutions];
				for (int i = 0 ; i < numberOfSolutions ; i++) {
					solutionId[i] = Integer.parseInt(args[i + 4].split(" ")[1].replace("'", ""));
				}
				Solutions sol = new Solutions(username, password, quizId, solutionId);
				solutions.add(sol);
				/*verific ca id-urile date pt raspunsurile la intrebarile din quiz exista*/
				if (findAnswersId(quizId) != -1) {
					System.out.println("{ 'status' : 'error', 'message' : 'Answer id for answer " + findAnswersId(quizId) + " does not exist'}");
					solutions.remove(solutions.size() - 1);
					return;
				}
				int score = calculateScoreQuiz(quizId);
				solutions.get(solutions.size() - 1).setScore(score);
				sol.writeSolToFile(fileSol.getName());
				System.out.println("{ 'status' : 'ok', 'message' : '" + score + " points'}");
			}
		} else if (count > 0 && Objects.equals(args[0], "-delete-quizz-by-id")) {
			if (count == 1 || count == 2) {
				/*parametrul -u sau -p nu este furnizat*/
				System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			} else if (count == 3) {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				/*verific daca userul exista*/
				if (!User.readFromFile(fileUsers.getName(), username, password)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
				} else {
					System.out.println("{ 'status' : 'error', 'message' : 'No quizz identifier was provided'}");
				}
			} else {
				int quizId = Integer.parseInt(args[3].split(" ")[1].replace("'", ""));
				/*verific daca exista un quiz cu id-ul = quizId*/
				if (!quizExists(quizId)) {
					System.out.println("{ 'status' : 'error', 'message' : 'No quiz was found'}");
				} else {
					/*update 'solutions' and 'quizzez'*/
					/*update files*/
					updateLists(quizId);
					updateFiles(fileQuiz.getName(), fileSol.getName());
					System.out.println("{ 'status' : 'ok', 'message' : 'Quizz deleted successfully'}");
				}
			}
		} else if (count > 0 && Objects.equals(args[0], "-get-my-solutions")) {
			if (count == 1 || count == 2) {
				/*parametrul -u sau -p nu este furnizat*/
				System.out.println("{ 'status' : 'error', 'message' : 'You need to be authenticated'}");
			} else if (count == 3) {
				String username = args[1].split(" ")[1].replace("'", "");
				String password = args[2].split(" ")[1].replace("'", "");
				/*verific daca userul exista*/
				if (!User.readFromFile(fileUsers.getName(), username, password)) {
					System.out.println("{ 'status' : 'error', 'message' : 'Login failed'}");
				} else {
					/*daca userul exista, atunci caut ce chestionare a completat si le afisez detaliile*/
					System.out.println("{ 'status' : 'ok', 'message' : '[" + printSolDetails(username) + "]'}");
				}
			}
		}
	}
}
