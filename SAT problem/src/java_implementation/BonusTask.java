// Copyright 2020
// Author: Matei Simtinică

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Bonus Task
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class BonusTask extends Task {
    int N;
    int M;
    int[][] friends;

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

        BufferedReader reader = new BufferedReader ( new FileReader ( inFilename ) );

        String[] strings = reader.readLine ().trim ().split ( "\\s+" );
        N = Integer.parseInt ( strings[0] );
        M = Integer.parseInt ( strings[1] );

        //citesc muchiile
        friends = new int[M][2];

        for (int i = 0; i < M; i++) {
            String line = reader.readLine ();
            String[] friend = line.trim ().split ( "\\s+" );

            friends[i][0] = Integer.parseInt ( friend[0] );
            friends[i][1] = Integer.parseInt ( friend[1] );
        }
        reader.close ();
    }

    @Override
    public void formulateOracleQuestion() throws IOException {

        Map<Integer,Integer> connections = getNumberOfConnections ();

        int clause = N + M;

        int top = N * (M - 2) + 1;

        StringBuilder out = new StringBuilder ();

        out.append ( "p wcnf " )
                .append ( N )
                .append ( " " )
                .append ( clause )
                .append ( " " )
                .append ( top )
                .append ( "\n" );

        //clauzele hard
        for (int i = 0; i < M; i++) {
            out.append ( top )
                    .append ( " " )
                    .append ( friends[i][0] )
                    .append ( " " )
                    .append ( friends[i][1] )
                    .append ( " 0\n" );
        }

        //clauzele soft
        for (int i = 1; i <= N; i++) {
            if (connections.get ( i ) != 0) {

                out.append ( M - connections.get ( i ) )
                        .append ( " -" )
                        .append ( i )
                        .append ( " 0\n" );
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

        String[] firstLine = reader.readLine ().trim ().split ( "\\s+" );
        String[] secondLine = reader.readLine ().trim ().split ( "\\s+" );

        reader.close ();

        StringBuilder out = new StringBuilder ();

        for (int i = 0; i < Integer.parseInt ( firstLine[0] ); i++) {

            //aleg dor familiile care au un semn pozitiv in fisier
            if (Integer.parseInt ( secondLine[i] ) > 0) {
                out.append ( secondLine[i] ).append ( " " );
            }
        }
        BufferedWriter writer = new BufferedWriter ( new FileWriter ( outFilename ) );
        writer.write ( String.valueOf ( out ) );
        writer.close ();
    }

    /**
     * fiecare familie va reprezenta un entry in Map,
     * iar arraylist-ul corespunzator va fi format de toate
     * celelalte familiile cu care aceasta are o relatie.
     * @return
     */
    public Map<Integer, Integer> getNumberOfConnections() {

        Map<Integer, Integer> map = new HashMap<> ();

        for (int i = 1; i <= N; i++) {

            int nr = 0;

            //parcurg lista de prieteni
            //daca familia se afla pe o linie,
            //adaug in lista familia cealalta
            for (int[] friend : friends) {

                if (friend[0] == i || friend[1] == i) {
                    nr++;
                }
            }
            map.put ( i, nr );
        }
        return map;
    }
}
