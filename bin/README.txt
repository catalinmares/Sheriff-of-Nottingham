\\------------------------ Mares Catalin-Constantin -------------------------// 

\\\------------------------------ Grupa 322CD ------------------------------///

\\\\------------------ PROGRAMARE ORIENTATATA PE OBIECTE ------------------////

\\\\\----------------------------- Tema nr. 1 ----------------------------/////

\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\////////////////////////////////////////



PREZENTAREA CLASELOR SI A METODELOR
Proiectul pe care l-am realizat, este compus, pe langa cele 2 clase, Main si
GameInput din schelet, din inca 5 clase pe care mi le-am definit in vederea
implementarii jocului. Aceste clase sunt:

1. BasicPlayer - cea mai importanta clasa din program, oferind toate datele 
despre jucatori (monede, bunuri in mana, bunuri in sac, bunuri pe taraba)si 
totodata metode prin care acesti jucatori interactioneaza intre ei. Pe langa
metode banale, precum getteri si setteri, aceasta clasa implementeaza urmatoarele
metode:
a) createSack - calculeaza numarul de bunuri legale din mana;
              - daca avem bunuri legale, se apeleaza metoda createLegalSack;
              - daca nu avem bunuri legale, se apeleaza metoda createIllegalSack.

b) createLegalSack - implementeaza strategia de baza pentru crearea sacului 
                     comerciantului;
                   - foloseste un Hash Table pentru a calcula frecventele 
                     bunurilor din mana si le salveaza intr-un vector de frecvente 
                     pe baza ID-ului lor;
                   - calculeaza frecventa maxima;
                   - salveaza intr-un vector profiturile bunurilor cu frecventa 
                     maxima;
                   - calculeaza profitul maxim si salveaza primul bun din mana 
                     cu cea mai mare frecventa si cel mai mare profit, il extrage
                     din mana, il adauga in sac si il declara.

c) createIllegalSack - implementeaza cazul in care jucatorul de baza nu are 
                       bunuri legale in mana;
                     - cauta in mana jucatorului bunul ilegal cu profitul cel 
                       mai mare, il scoate din mana si il adauga in sac,
                       declarand "Mere".

d) control - implementeaza strategia de baza pentru rolul de serif;
           - obtine declaratia de la jucator;
           - returneaza mita oferita de acesta si ii verifica sacul;
           - daca gaseste bunuri ilegale sau nedeclarate, i le confisca si i se 
             plateste penalty-ul pentru fiecare bun nedeclarat/ilegal;
	   - daca a controlat un comerciant cinstit, ii plateste penalty pentru 
             toate bunurile din sac.

e) endRound - implementeaza etapa adaugarii bunurilor ramase in sac pe taraba;
            - cat timp sacul nu e gol, se extrage un element din sac si se 
              adauga pe taraba.


2. GreedyPlayer - mosteneste clasa BasicPlayer (Greedy si Bribed aplica in
anumite cazuri strategia de baza, deci cumva o mostenesc). Aceasta subclasa
suprascrie urmatoarele metode din superclasa:
a) createSack - apeleaza metoda createSack cu acelasi nume din superclasa 
                (aplica strategia de baza); 
              - contorizeaza runda curenta de comerciant pentru Greedy;
              - daca se afla intr-o runda para si are mai putin de 5 bunuri in sac, 
                se cauta in mana bunul cu profitul cel mai mare si daca acesta 
                este ilegal, se extrage din mana si se adauga in sac.

b) control - nu verifica sacul comerciantului daca acesta a dat mita; i se ia mita 
             si se adauga la castigul serifului;
           - daca jucatorul nu a dat mita, se apeleaza metoda cu acelasi nume din 
             superclasa (se aplica strategia de baza pentru serif).


3. BribePlayer - mosteneste clasa BasicPlayer. Aceasta subclasa suprascrie
urmatoarea metoda din superclasa:
a) createSack - selecteaza toate bunurile ilegale din mana si le contorizeaza.
              - daca nu sunt bunuri ilegale in mana se apeleaza metoda cu acelasi 
                nume din superclasa (se aplica metoda de baza);
              - se sorteaza bunurile ilegale in functie de profit si se seteaza mita;
              - daca are 6 bunuri ilegale in mana, se exclude bunul cel mai putin 
                profitabil;
              - se seteaza mita;
              - daca jucatorul nu are suficienti bani pentru mita, se aplica 
                strategia de baza;
              - altfel se pregateste mita, se extrag bunurile ilegale din mana, 
                se adauga in sac si se declara "Mere".


4. Goods - clasa pentru organizarea structurata a bunurilor;
         - contine o metoda statica (getProfit) care returneaza profitul unui bun 
           in functie de ID-ul lui;
         - am facut aceasta metoda statica pentru a putea implementa in main
           desfasurarea jocului.


5. GoodsComparator - clasa ce contine o metoda compare ce compara bunurile pentru
a le sorta in functie de profit.

De asemenea, in clasa Main, am implementat urmatoarele metode statice:
a) maxValue - calculeaza valoarea maxima dintr-un vector;
            - folosita la determinarea frecventei maxime si a profitului maxim.

b) giveBonus - atribuie King Bonus si QueenBonus pentru un anumit bun pe baza unui
               vector de countAsset;
             - in countAsset, pe pozitia i se afla numarul de bunuri de tip
               Asset pe care le are jucatorul i la finalul jocului pe taraba;
             - calculeaza maximul din countAsset si acorda King Bonus jucatorilor 
               cu numarul maxim de bunuri de acel tip;
             - pentru acesti jucatori se seteaza numarul de bunuri la -1 pentru a
               nu primi si Queen Bonus cand toti jucatorii au la fel de multe bunuri
               de acelasi tip;
             - recalculeaza maximul si se procedeaza analog pentru Queen Bonus.

c) printScore - calculeaza scorul maxim si determina cui apartine;
              - afiseaza jucatorul si scorul si pentru acel jucator scorul devine 0;
              - returneaza noul scor maxim;
              - este apelata cat timp returneaza un scor nenul si astfel
                afiseaza scorul tuturor jucatorilor.

d) calculateScores - adauga pe taraba bunurile bonus oferite de bunurile ilegela;
                   - adauga la scor banii si profitul fiecarui bun de pe taraba;
                   - contorizeaza numarul de bunuri legale de fiecare tip pentru
                     fiecare jucator pentru acordarea bonusurilor de King si Queen;
                   - apeleaza metoda statica giveBonus pentru fiecare bun.

FUNCTIONAREA JOCULUI - functia main:
1. Se creeaza o lista de bunuri care va fi gramada de carti din care jucatorii vor
trage in fiecare runda.
2. Se creeaza un vector de jucatori si se creeaza mana initiala a fiecarui jucator.
3. Daca sunt x jucatori, trebuie sa se joace 2*x runde pentru ca fiecare jucator sa
fie serif de 2 ori.
4. In fiecare runda se determina seriful ca fiind runda curenta % numarul de jucatori.
5. Fiecare comerciant isi creeaza sacul.
6. Seriful controleaza comerciantii.
7. Comerciantii isi pun lucrurile pe taraba si isi recompleteaza bunurile din mana/
8. Cand se termina jocul se calculeaza si afiseaza scorurile finale prin apelarea
metodelor statice calculateScores si printScores.

IMPLEMENTAREA BONUSULUI
Pentru bonus, am mai implementat o clasa WizardPlayer care mosteneste BasicPlayer si
suprascrie urmatoarele metode:
a) createSack - aplica strategia de baza, adica apeleaza metoda cu acelasi nume din
                superclasa;
              - daca a bagat in sac un bun ilegal, ofera mita.

b) control - daca comerciantul controlat a dat mita, se returneaza mita si se verifica
             sacul;
           - daca comerciantul nu a dat mita, i se va verifica sacul numai in rundele
             pare de serif ale lui Wizard.