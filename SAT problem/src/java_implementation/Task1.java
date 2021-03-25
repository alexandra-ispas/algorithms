// Copyright 2020
// Author: Matei Simtinică

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Task1
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task1 extends Task {


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
        readTasks123 (1);
    }

    @Override
    public void formulateOracleQuestion() throws IOException {

        int clause = N + M * K + N*(K/2*(K-1));

        StringBuilder out = new StringBuilder ();
        out.append ( "p cnf " )
                .append ( N * K )
                .append ( " " )
                .append ( clause )
                .append ( "\n" );

        //fiecare familie are un spion
        for (int v = 1; v <= N; v++) {
            for (int i = 1; i <= K; i++) {
                out.append ( pair ( v,i ) )
                    .append ( " " );
            }
            out.append ( "0\n" );
        }

        //orice familie are asignat un singur spion
        for (int v = 1; v <= N; v++) {
            for (int i = 1; i < K; i++) {

                    out.append ( "-" )
                            .append ( pair ( v, i ) )
                            .append ( " -" )
                            .append ( pair ( v, i+1 ) )
                            .append ( " 0\n" );
            }
        }

        //oricare 2 familii prietene au spioni diferiti
        for (Map.Entry<Integer, List<Integer>> entry : map.entrySet ()){
            for (int i = 1; i <= K; i++) {

                for(int number : entry.getValue ()) {

                    out.append ( "-" )
                            .append ( pair (entry.getKey (), i ) )
                            .append ( " -" )
                            .append ( pair ( number, i ) )
                            .append ( " 0\n" );
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

        //daca exista un mod de a distribui spionii,
        //scriu doar False
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
                    nrs.put ( pair[0], pair[1] );
                }
            }

            for(Map.Entry<Long, Long> entry : nrs.entrySet ())
                out.append ( entry.getValue () ).append ( " " );
        }

        BufferedWriter writer = new BufferedWriter ( new FileWriter ( outFilename ) );
        writer.write ( String.valueOf ( out ) );
        writer.close ();
    }


}
