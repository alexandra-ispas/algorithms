// Copyright 2020
// Author: Matei Simtinică

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Task2
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task2 extends Task {

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData ();
        formulateOracleQuestion ();
        askOracle ();
        decipherOracleAnswer ();
        writeAnswer ();
    }

    @Override
    public void readProblemData() throws IOException {
        readTasks123 (2);
    }

    @Override
    public void formulateOracleQuestion() throws IOException {

        int clause = K + N * (K * (K - 1)) / 2 +  (N * (N - 1) / 2 - M)*K * (K-1);

        StringBuilder out = new StringBuilder ();

        out.append ( "p cnf " )
                .append ( N * K )
                .append ( " " )
                .append ( clause )
                .append ( "\n" );

        //fiecare nod este conectat la altul printr-o muchie
        for (int i = 1; i <= K; i++) {
            for (int v = 1; v <= N; v++) {

                out.append (pair ( v, i ) )
                        .append ( " " );
            }
            out.append ( "0\n" );
        }

        //fiecarui nod ii corespunde un singur numar de muchii
        for (int i = 1; i < K; i++) {
            for (int j = i + 1; j <= K; j++) {
                for (int v = 1; v <= N; v++) {

                    out.append ( "-" ).append (pair ( v, i ) ).append ( " -" )
                            .append ( pair ( v, j ) ).append ( " 0\n" );

                }
            }
        }
        System.out.println ("map = " + map);
        //oricare 2 noduri neadiacente nu pot fi ambele incluse in clique
        for (int i = 1; i <= K; i++) {

            for (int j = 1; j <= K; j++) {
                if(i != j) {

                    for(int counter = 1; counter <= N; counter++) {

                        if(map.containsKey ( counter )){

                            for(int number = counter + 1; number <= N; number++){
                                if(!map.get ( counter ).contains ( number )){

                                    out.append ( "-" ).append ( pair ( counter, i ) )
                                            .append ( " -" )
                                            .append ( pair ( number, j ) )
                                            .append ( " 0\n" );
                                }
                            }
                        } else {
                            for (int number = counter+1; number<=N; number++){

                                out.append ( "-" ).append ( pair ( counter, i ) )
                                        .append ( " -" )
                                        .append ( pair ( number, j ) )
                                        .append ( " 0\n" );
                            }
                        }
                    }
                }
            }
        }
        BufferedWriter writer = new BufferedWriter ( new FileWriter ( oracleInFilename ) );
        writer.write ( String.valueOf ( out ) );
        writer.close ();
    }

    @Override
    public void decipherOracleAnswer() throws IOException, InterruptedException {
        askOracle ();
    }

    @Override
    public void writeAnswer() throws IOException {

        BufferedReader reader = new BufferedReader ( new FileReader ( oracleOutFilename ) );

        StringBuilder out = new StringBuilder ();

        String truthValue = reader.readLine ();

        if (truthValue.equals ( "False" )) {
            out.append (  "False\n" );
            reader.close ();
        } else {
            out.append ( "True\n" );

            int numberOfVariables = Integer.parseInt ( reader.readLine () );
            String[] result = reader.readLine ().trim ().split ( "\\s+" );
            reader.close ();

            Map<Long, Long> nrs = new TreeMap<> ();

            for (int i = 0; i < numberOfVariables; i++) {
                int number = Integer.parseInt ( result[i] );
                if (number > 0) {

                    long[] pair = depair ( number );
                    nrs.put ( pair[1], pair[0] );

                }
            }

            for(Map.Entry<Long, Long> entry : nrs.entrySet ())
                out.append ( entry.getValue () ).append ( " " );
        }


        BufferedWriter writer = new BufferedWriter ( new FileWriter ( outFilename ) );
        writer.write ( String.valueOf ( out ) );
        writer.close ();
    }

    /**
     * verifica daca 2 familii sunt prietene
     * @param fam1
     * @param fam2
     * @return
     */
    public boolean isFriends(int fam1, int fam2) {
        if (ints == null) {
            return false;
        }
        for (int[] friend : ints) {
            if (friend[0] == fam1 && friend[1] == fam2) {
                return true;
            }
        }
        return false;
    }

    /**
     * intoarce familiile care nu se inteleg
     * pe fiecare linie din matricea nonfriends
     * se afla cate 2 noduri ce nu sunt conectate prin nicio muchie
     */
    public void getNonFriends() {

        nonFriends = new int[N * (N - 1) / 2 - M][2];

        int idx = 0;
        for (int i = 1; i < N; i++) {
            for (int j = i + 1; j <= N; j++) {

                if (!isFriends ( i, j )) {
                    nonFriends[idx][0] = i;
                    nonFriends[idx++][1] = j;
                }
            }
        }
    }
}


