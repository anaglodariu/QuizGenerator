# tema-1-anaglodariu
tema-1-anaglodariu created by GitHub Classroom

- informatii despre useri, intrebari, chestionare, solutii vor fi tinute in fisiere de tip csv
- am creat in plus clasele User, Questions, Quiz si Solutions, fiecare cu campurile specifice lor
- pentru fiecare din cazurile de mai jos retin informatiile necesare in fisiere si liste abia dupa ce fac verificarile de validare a acestora

- daca primul argument din linia de comanda este de tipul "cleanup-all"
   - sterg tot ce am in fisiere cu functia statica "emptyfile"
   - variabilele statice sunt resetate la 0
   - listele (tot variabile statice) de care ma folosesc pentru a retine informatii (ca in fisiere) de diferite tipuri (user, intrebari, chestionare, solutii) sunt golite

- daca primul argument din linia de comanda este de tipul "create-user"
   - mai intai verific daca parametrii -u sau -p sunt dati
   - daca sunt dati, atunci verific daca userul este deja in system cu functia statica creata in clasa User "readFromFile", care imi citeste din fisier si verifica daca am deja username-ul userului in fisier
   - abia dupa toate aceste verificari creez o instanta a clasei User, pe care o retin in lista de tipul User si o scriu in fisier cu functia "writeUserToFile"

- daca primul argument din linia de comanda este de tipul "create-question"
   - functia statica "readFromFile" din clasa Questions verifica daca am deja in fisier o intrebare cu acelasi text
   - functia statica "duplicateAnswers" din clasa Tema1 verifica daca o intrebare are raspunsuri duplicate
   - functia "writeQToFile" din clasa Questions scrie in fisier informatiile despre intrebare

- daca primul argument din linia de comanda este de tipul "get-question-id-by-text"
   - functia statica "getId" din clasa Questions imi gaseste id-ul unei intrebari dupa textul ei citind din fisier
   - in clasa Questions am un camp static "id" pe care il incrementez la fiecare instantiere a unui obiect de tip intrebare, dupa fiecare incrementare retin id-ul fiecarei intrebari intr-un camp private "qId"
   - pentru raspunsurile la intrebari retin id-urile tot cu ajutorul unui camp static "answerId"
   
- daca primul argument din linia de comanda este de tipul "get-all-questions"
   - functia statica "getAllQ" din clasa Questions imi citeste din fisier toate intrebarile

- daca primul argument din linia de comanda este de tipul "create-quizz"
   - functia statica "readFromFile" din clasa Quiz verifica daca am deja un chestionar cu acelasi nume in sistem
   - functia "writeQuizToFile" din clasa Quiz scrie in fisier informatiile despre chestionar

- daca primul argument din linia de comanda este de tipul "get-quiz-by-name"
   - functia statica "getId" din clasa Quiz imi gaseste id-ul unui chestionar dupa numele lui citind din fisier

- daca primul argument din linia de comanda este de tipul "get-all-quizzes"
   - functia statica "getAllQuizez" din clasa Quiz afiseaza toate chestionarele, verificand inainte daca un user a raspuns deja la el (adica daca se afla deja in lista "solutions")
   
- daca primul argument din linia de comanda este de tipul "get-quizz-details-by-id"
   - functia statica "getIds" din clasa Quiz imi returneaza un vector cu id-urile intrebarile din chestionarul cerut
   - pentru fiecare id din vector afisez detaliile intrebarii corespunzatoare acestuia cu functia statica "getDetails" din clasa Questions
   - ambele functii imi cauta in fisier informatiile

- daca primul argument din linia de comanda este de tipul "submit-quizz"
   - functia statica "quizExists" verifica in lista "quizzez" daca exista un quiz cu id-ul dat ca parametru
   - functia statica "quizAlreadyCompleted" verifica in lista "solutions" daca quiz-ul dat a fost deja completat de user-ul dat
   - functia statica "cannotAnswerQuiz" verifica in lista quizzez care este autorul quiz-ului dat (un user nu-si poate raspunde la propriul quiz)
   - functia statica "findAnswersId" verifica ca toate id-urile raspunsurilor date ca parametru sunt valide
   - functia statica "calculateScoreQuiz" se apeleaza dupa ce am verificat ca toate argumentele date ca parametru sunt valide cu functiile anterioare
   - daca un user a dat submit la un quiz, quiz-ul va fi adaugat in fisier si in lista "solutions"

- daca primul argument din linia de comanda este de tipul "delete-quizz-by-id"
   - functia statica "updateLists" elimina quizz-ul dat ca paramtru din listele "quizzez" si "solutions"
   - functia statica "updateFiles" reface fisierele dupa listele updatate anterior

- daca primul argument din linia de comanda este de tipul "get-my-solutions"
   - functia statica "printSolDetails" afiseaza detaliile despre chestionarele completate
