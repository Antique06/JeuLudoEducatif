import extensions.CSVFile;
import extensions.File;

class main extends Program {
    CSVFile pokemonCSV=loadCSV("ressources/pokemon.csv");
    CSVFile evolutionCSV=loadCSV("ressources/evolution.csv");
    CSVFile questionsCSV=loadCSV("ressources/questions.csv");
    CSVFile leaderboardCSV = loadCSV("ressources/leaderboard.csv");


    void algorithm(){
        nettoyage();
        if(jouer()) {
            Joueur joueur1;
            Joueur joueur2 ;
            String commencer;
            int nbTour = 0;
            Joueur[] tabJoueurs;
            do{
                nettoyage();
                joueur1=choixJoueur();
                joueur2=choixJoueur();
                print("Voulez vous commencer ?(oui ou non) : ");
                commencer = readString();
            }while(!equals(commencer,"oui"));
            while (joueur1.pokemon.pvActuel>0 && joueur2.pokemon.pvActuel>0) {
                nbTour = tour(nbTour);
                tabJoueurs=tourJoueur(joueur1, joueur2, nbTour);
                joueur1=tabJoueurs[0];
                joueur2=tabJoueurs[1];
                if (joueur2.pokemon.pvActuel>0 ) {
                    nbTour = tour(nbTour);
                    tabJoueurs=tourJoueur(joueur2, joueur1, nbTour);
                    joueur2=tabJoueurs[0];
                    joueur1=tabJoueurs[1];
                }
            }
            if(joueur1.pokemon.pvActuel>0) {
                println("Bien Joué, " + joueur2.nom + " a gagné.");
            } else {
                println("Bien joué, " + joueur1.nom + " a gagné.");
            }
            String[][] tabLeaderboard = new String[5][2];
            for(int i=0; i<5; i++) {
                for(int j=0; j<2; j++) {
                    tabLeaderboard[i][j] = getCell(leaderboardCSV, i, j);
                }
            }tabLeaderboard = saveScoreCsvFile(nbTour, tabLeaderboard);
            tabLeaderboard = saveNombreCarapuce(joueur1, joueur2, tabLeaderboard);
            tabLeaderboard = saveNombrePikachu(joueur1, joueur2, tabLeaderboard);
            tabLeaderboard = saveNombreSalameche(joueur1, joueur2, tabLeaderboard);  
            saveCSV(tabLeaderboard, "ressources/leaderboard.csv");          
        }else {
            affichageLeaderboard();
        }
    }


    boolean evolutionPossible(Pokemon pokemon, int nbTour){
        boolean evolutionPossible=false;
        if(equals(pokemon.nom,"pikachu") && nbTour>=5 || pokemon.pvActuel<=20){
            evolutionPossible=true;
        }else if(equals(pokemon.nom,"salameche") && nbTour>=4 || pokemon.pvActuel<=20){
            evolutionPossible=true;
        }if(equals(pokemon.nom,"carapuce") && nbTour>=3 || pokemon.pvActuel<=20){
            evolutionPossible=true;
        }return evolutionPossible;
        
    }

    Pokemon evolution(Pokemon pokemon){
        int emplacementPokemon = -1;
        if (equals(pokemon.nom, "pikachu")) {
            emplacementPokemon = 1;
        } else if (equals(pokemon.nom, "carapuce")) {
            emplacementPokemon = 2;
        } else if (equals(pokemon.nom, "salameche")) {
            emplacementPokemon = 3;
        }
        pokemon.nom = getCell(evolutionCSV,emplacementPokemon,0);
        pokemon.dessin = "ressources/" + getCell(evolutionCSV,emplacementPokemon,1);
        pokemon.pvActuel =pokemon.pvActuel +stringToInt(getCell(evolutionCSV,emplacementPokemon,2));
        pokemon.pvMax = pokemon.pvMax + stringToInt(getCell(evolutionCSV,emplacementPokemon,2));
        pokemon.attaque1 = getCell(evolutionCSV,emplacementPokemon,3);
        pokemon.degats1 = stringToInt(getCell(evolutionCSV,emplacementPokemon,4));
        pokemon.attaque2 = getCell(evolutionCSV,emplacementPokemon,5);
        pokemon.degats2 = stringToInt(getCell(evolutionCSV,emplacementPokemon,6));
        return(pokemon);
    }

    String[][] saveScoreCsvFile(int nbTour,String[][] tabLeaderboard){
        if(nbTour<stringToInt(tabLeaderboard[0][1])) {
            tabLeaderboard[0][1] = "" + nbTour;
        }if(nbTour>stringToInt(tabLeaderboard[1][1])) {
            tabLeaderboard[1][1] = "" + nbTour;
        }return tabLeaderboard;
    }

    String[][] saveNombreCarapuce(Joueur joueur1,Joueur joueur2,String[][] tabLeaderboard){
        if(equals(joueur1.pokemon.nom, "carapuce") || equals(joueur2.pokemon.nom, "carapuce")) {
            if(equals(joueur1.pokemon.nom, joueur2.pokemon.nom)) {
                tabLeaderboard[2][1] = "" + (stringToInt(tabLeaderboard[2][1]) + 2);
            } else {
                tabLeaderboard[2][1] = "" + (stringToInt(tabLeaderboard[2][1]) + 1);
            }
        }return tabLeaderboard;
    } 

    String[][] saveNombreSalameche(Joueur joueur1,Joueur joueur2,String[][] tabLeaderboard){
        if(equals(joueur1.pokemon.nom, "salameche") || equals(joueur2.pokemon.nom, "salameche")) {
            if(equals(joueur1.pokemon.nom, joueur2.pokemon.nom)) {
                tabLeaderboard[3][1] = "" + (stringToInt(tabLeaderboard[3][1]) + 2);
            } else {
                tabLeaderboard[3][1] = "" + (stringToInt(tabLeaderboard[3][1]) + 1);
            }
        }return tabLeaderboard;
    }

    String[][] saveNombrePikachu(Joueur joueur1,Joueur joueur2,String[][] tabLeaderboard){
        if(equals(joueur1.pokemon.nom, "pikachu") || equals(joueur2.pokemon.nom, "pikachu")) {
            if(equals(joueur1.pokemon.nom, joueur2.pokemon.nom)) {
                tabLeaderboard[4][1] = "" + (stringToInt(tabLeaderboard[4][1]) + 2);
            } else {
                tabLeaderboard[4][1] = "" + (stringToInt(tabLeaderboard[4][1]) + 1);
            }
        }return tabLeaderboard;
    }

    String [] questionTab(String choix){
        String [] questionTab= new String[17];
        if (equals(choix, "1")) {
            for(int i=1; i<length(questionTab)+1;i++){
                questionTab[i-1]=getCell(questionsCSV,i,0);
            }
        } else if(equals(choix, "2")) {
            for(int i=1; i<length(questionTab)+1;i++){
                questionTab[i-1]=getCell(questionsCSV,i,2);
            }
        }
        return questionTab;
    }

    void testQuestionTab(){
        String [] testQuestionTab= new String[]{"Combien font 63+4","Combien font 5*8","Combien font 46-9","Combien font 5*5","Combien font 8*7","Combien font 25-7","Combien font 24+12", "Combien font 3*7","Combien font 49-7", "Combien font 16+36","Conjuguer le verbe « chanter » au présent avec « ils »","Combien de mois dans une année","Combien de côté a un triangle","Quelle est la capitale de la France","Quelle est la couleur du ciel quand il est clair","Quelle est la couleur de l’herbe en été","Combien de jours dans une semaine"};
        assertArrayEquals(testQuestionTab, questionTab("1"));
    }

    String [] reponseTab(String choix){
        String [] reponseTab= new String[17];
        if (equals(choix, "1")) {
            for(int i=1; i<length(reponseTab)+1;i++){
                reponseTab[i-1]=getCell(questionsCSV,i,1);
            }
        } else if(equals(choix, "2")) {
            for(int i=1; i<length(reponseTab)+1;i++){
                reponseTab[i-1]=getCell(questionsCSV,i,3);
            }
        }
        return reponseTab;
    }

    void testReponseTab(){
        String [] testReponseTab= new String[]{"67","40","37","25","56","18","36","21","42","52","chantent","12","3","Paris","bleu","vert","7"};
        assertArrayEquals(testReponseTab, reponseTab("1"));
    }

    Joueur choixJoueur(){
        Joueur joueur= new Joueur();
        print("Quelle nom veux-tu ? ");
        joueur.nom = readString();
        joueur.pokemon = creationPokemon(choixPokemon());
        affichageDessin(joueur.pokemon);
        println(joueur.nom + " a choisis " + joueur.pokemon.nom);
        return joueur;
    }

    // void testChoixJoueur(){
    //     Joueur joueur=new Joueur();
    //     joueur.nom="a";
    //     joueur.pokemon=choixPokemon();
    //     assert(joueur, choixJoueur());
    // }
    
    int tour(int nbTour) {
        nettoyage();
        nbTour++;
        println("Tour " + nbTour);
        return nbTour;
    }

    void testTours(){
        assertEquals(13, tour(12));
    }

    void affichageTerrain(Joueur joueurAttaquant,Joueur joueurDefenseur, int nbTour){
        affichageStat(joueurDefenseur);
        affichageDessin(joueurDefenseur.pokemon);
        affichageDessin(joueurAttaquant.pokemon);
        affichageStat(joueurAttaquant);
        affichageMenu(joueurAttaquant, nbTour);
    }

    String choixAction(Pokemon pokemon,int nbTour){
        String choix;
        do{
            print("Choisissez le chiffre correspondant à une action : ");
            choix=readString();
        }while (!equals(choix,"1") && !equals(choix,"2") && !equals(choix,"3") || (equals(choix,"3") && !evolutionPossible(pokemon, nbTour)));
        return choix;
    }

    // void testChoixAction(){
    //     assertEquals(12, choixAction());
    // }

    Joueur[] tourJoueur(Joueur joueurAttaquant, Joueur joueurDefenseur, int nbTour){
        String choix;
        affichageTerrain(joueurAttaquant, joueurDefenseur, nbTour);
            choix= choixAction(joueurAttaquant.pokemon,nbTour);
        if (equals(choix,"3") && evolutionPossible(joueurAttaquant.pokemon, nbTour)) {
            joueurAttaquant.pokemon=evolution(joueurAttaquant.pokemon);
        }else{
            int questionRandom=questionRandom(choix);
            String question=poserQuestion(questionTab(choix), questionRandom);
            print(question + " ? ");
            String bonneReponse=sansMaj(demandeReponse(reponseTab(choix), questionRandom));
            String reponseJoueur=sansMaj(readString());
            if (equals(choix,"1")) {
                if(equals(bonneReponse,reponseJoueur)){
                    joueurDefenseur.pokemon.pvActuel = degatsJoueur(joueurAttaquant.pokemon.degats1, joueurDefenseur.pokemon.pvActuel);
                }
            }else if (equals(choix,"2")){
                if(equals(bonneReponse,reponseJoueur)){
                    joueurDefenseur.pokemon.pvActuel = degatsJoueur(joueurAttaquant.pokemon.degats2, joueurDefenseur.pokemon.pvActuel);
                }
            }
        }
        nettoyage();
        Joueur[] tabJoueur=new Joueur[]{joueurAttaquant,joueurDefenseur};
        return tabJoueur;
    }

    boolean jouer() {
        String reponse;
        do {
            print("Voulez-vous lancer la partie (jouer) ou regarder les statistiques (stat) ? ");
            reponse = sansMaj(readString());
        } while(!equals(reponse, "jouer") && !equals(reponse, "stat"));
        if(equals(reponse, "jouer")) {
            return true;
        } else {
            return false;
        }
    }

    // void testTourJoueur() {
    //     Joueur joueur1 = choixJoueur();
    //     Joueur joueur2 = choixJoueur();
    //     joueur2 = tourJoueur(joueur1, joueur2);
    //     assertTrue(joueur2.pokemon.pvActuel < 50); // Attaque réussie
    // }

    int questionRandom(String choix){
        String[] questionTab= questionTab(choix);
        int questionRandom=(int)(random()*length(questionTab));
        return questionRandom;
    }

    String poserQuestion(String[] questionTab, int random){
        String question=questionTab[random];
        return question;
    }

    String demandeReponse(String[] reponseTab, int random){
        String reponse=reponseTab[random];
        return reponse;
    }

    int degatsJoueur(int degats,int pv){
        pv=pv-degats;
        return pv;
    }

    void nettoyage(){
        clearScreen();
        up(150);
    }


    int choixPokemon() {
        int i;
        do {
            i=1;
            print("Choisis ton Pokemon parmi : ");
            for(int j=1; j<rowCount(pokemonCSV)-1; j++) {
                print(getCell(pokemonCSV, j, 0) + " / ");
            }
            print(getCell(pokemonCSV, rowCount(pokemonCSV)-1, 0));
            println();
            print("Nom du Pokemon (SANS ACCENT) : ");
            String nom=sansMaj(readString());
            while(i<rowCount(pokemonCSV) && !equals(nom, getCell(pokemonCSV, i, 0))) {
                i++;
            }
        } while(i==rowCount(pokemonCSV));
        return i;

    }

    Pokemon creationPokemon(int emplacementPokemon){
        Pokemon pokemon = new Pokemon();
        pokemon.nom = getCell(pokemonCSV,emplacementPokemon,0);
        pokemon.dessin = "ressources/" + getCell(pokemonCSV,emplacementPokemon,1);
        pokemon.pvActuel = stringToInt(getCell(pokemonCSV,emplacementPokemon,2));
        pokemon.pvMax = stringToInt(getCell(pokemonCSV,emplacementPokemon,2));
        pokemon.attaque1 = getCell(pokemonCSV,emplacementPokemon,3);
        pokemon.degats1 = stringToInt(getCell(pokemonCSV,emplacementPokemon,4));
        pokemon.attaque2 = getCell(pokemonCSV,emplacementPokemon,5);
        pokemon.degats2 = stringToInt(getCell(pokemonCSV,emplacementPokemon,6));
        return(pokemon);
    }

    String sansMaj(String chaine) {
        String nouvelleChaine = "";
        for(int i=0; i<length(chaine); i++) {
            if(charAt(chaine, i) >= 'A' && charAt(chaine, i) <= 'Z') {
                nouvelleChaine = nouvelleChaine + (char) (charAt(chaine, i)+32);
            } else {
                nouvelleChaine = nouvelleChaine + (char) (charAt(chaine, i));
            }
        }
        return nouvelleChaine;
    }

    void affichageDessin(Pokemon pokemon) {
        String s="";
        extensions.File dessin = newFile(pokemon.dessin);
        while(ready(dessin)) {
            s=s+readLine(dessin)+'\n';
        }
        println(s);
    }
    
    int[] rechercheChiffre(String s) {
        int[] tab = new int[] {0, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        for(int i=0; i<length(s); i++) {
            if(charAt(s, i) >= '1' && charAt(s, i) <= '9') {
                tab[charToInt(charAt(s, i))] = i;
                tab[0] = 1;
            }
        }
        return tab;
    }

    void affichageMenu(Joueur joueur, int nbTour) {
        String attaque1 = joueur.pokemon.attaque1, attaque2 = joueur.pokemon.attaque2;
        extensions.File menu;
        int lenAttaque1 = length(attaque1), lenAttaque2 = length(attaque2);
        for(int i=0; i<(27-lenAttaque1)/2; i++) {
            attaque1 = " " + attaque1 + " ";
        }
        if(lenAttaque1 % 2 == 0) {
            attaque1 += " ";
        }
        for(int i=0; i<(27-lenAttaque2)/2; i++) {
            attaque2 = " " + attaque2 + " ";
        }
        if(lenAttaque2 % 2 == 0) {
            attaque2 += " ";
        }
        if(evolutionPossible(joueur.pokemon, nbTour)) {
            menu = newFile("ressources/affichageMenuEvo.txt");
        } else {
            menu = newFile("ressources/affichageMenu.txt");
        }
        String s;
        int[] tab;
        while(ready(menu)) {
            s = readLine(menu);
            tab = rechercheChiffre(s);
            if(tab[0]==1) {
                s = substring(s, 0, tab[1]) + attaque1 + substring(s, tab[1]+1, tab[2]) + attaque2 + substring(s, tab[2]+1, length(s));
            }
            println(s);
        }
    }

    void affichageStat(Joueur joueur) {
        String nomJ = joueur.nom, nomP = joueur.pokemon.nom, pv = " " + joueur.pokemon.pvActuel + "/" + joueur.pokemon.pvMax;
        int lenNomJ = length(nomJ), lenNomP = length(nomP), lenPv = length(pv);
        for(int i=0; i<40-lenNomJ; i++) {
            nomJ += " ";
        }
        for(int i=0; i<40-lenNomP; i++) {
            nomP += " ";
        }
        for(int i=0; i<37-lenPv; i++) {
            pv += " ";
        }
        extensions.File stat = newFile("ressources/affichageStat.txt");
        String s;
        int[] tab;
        while(ready(stat)) {
            s = readLine(stat);
            tab = rechercheChiffre(s);
            if(tab[0]==1) {
                if(tab[1] != -1) {
                    s = substring(s, 0, tab[1]) + nomJ + substring(s, tab[1]+1, length(s));
                } else if(tab[2] != -1) {
                    s = substring(s, 0, tab[2]) + nomP + substring(s, tab[2]+1, length(s));
                } else if(tab[3] != -1) {
                    s = substring(s, 0, tab[3]) + pv + substring(s, tab[3]+1, length(s));
                }
            }
            println(s);
        }
    }

    void affichageLeaderboard() {
        nettoyage();
        for(int i=0; i<5; i++) {
            println(getCell(leaderboardCSV,i,0)+ " : " + getCell(leaderboardCSV,i,1));
            println();
        }
    }
}