// Copyright 2020
// Author: Matei Simtinică

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * This is the abstract base class for all tasks that have to be implemented.
 */
public abstract class Task {
    String inFilename;
    String oracleInFilename;
    String oracleOutFilename;
    String outFilename;

    int N;                  //numarul de familii
    int M;                  //numarul de relatii
    int K;                  //numarul de spioni/dimensiunea gruparii
    int[][] ints;        //contine relatiile citie (pe o line se alfa 2 noduri
                            //care apartin aceleiasi muchii)

    Map<Integer, List<Integer>> map = new TreeMap<> ();
    int[][] nonFriends;     //este complementul grafului dat in input

    public abstract void solve() throws IOException, InterruptedException;

    public abstract void readProblemData() throws IOException;

    public void formulateOracleQuestion() throws IOException {
    }

    public void decipherOracleAnswer() throws IOException, InterruptedException {
    }

    public abstract void writeAnswer() throws IOException;

    /**
     * Stores the files paths as class attributes.
     *
     * @param inFilename        the file containing the problem input
     * @param oracleInFilename  the file containing the oracle input
     * @param oracleOutFilename the file containing the oracle output
     * @param outFilename       the file containing the problem output
     */
    public void addFiles(String inFilename, String oracleInFilename, String oracleOutFilename,
                         String outFilename) {
        this.inFilename = inFilename;
        this.oracleInFilename = oracleInFilename;
        this.oracleOutFilename = oracleOutFilename;
        this.outFilename = outFilename;
    }

    /**
     * Asks the oracle for an answer to the formulated question.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void askOracle() throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder ();
        builder.redirectErrorStream ( true );
        builder.command ( "python3", "sat_oracle.py", oracleInFilename, oracleOutFilename );
        Process process = builder.start ();
        BufferedReader in =
                new BufferedReader ( new InputStreamReader ( process.getInputStream () ) );
        String buffer;
        StringBuilder output = new StringBuilder ();

        while ((buffer = in.readLine ()) != null) {
            output.append ( buffer ).append ( "\n" );
        }

        int exitCode = process.waitFor ();
        if (exitCode != 0) {
            System.err.println ( "Error encountered while running oracle" );
            System.err.println ( output.toString () );
            System.exit ( -1 );
        }
    }

    /**
     * citeste datele de intrare pentru task1 si task2
     * @throws IOException
     */
    public void readTasks123(int taskNr) throws IOException {
        BufferedReader reader = new BufferedReader ( new FileReader ( inFilename ) );

        String[] strings = reader.readLine ().trim ().split ( "\\s+" );
        N = Integer.parseInt ( strings[0] );
        M = Integer.parseInt ( strings[1] );
        if(taskNr != 3) {
            K = Integer.parseInt ( strings[2] );
        } else {
            K = N/2+1;
        }

        //fiecare linie din input va forma o linie in matricea 'ints'
        for (int i = 0; i < M; i++) {

            String line = reader.readLine ();
            String[] friend = line.trim ().split ( "\\s+" );

            int nr1 = Integer.parseInt ( friend[0] );
            int nr2 = Integer.parseInt ( friend[1] );

            if(map.containsKey ( nr1 )){

                for(Map.Entry<Integer, List<Integer>> entry : map.entrySet ()){
                    if(entry.getKey () == nr1){
                        entry.getValue ().add ( nr2 );
                        break;
                    }
                }
            } else {
                List<Integer> array = new ArrayList<> ();
                array.add ( nr2 );
                map.put ( nr1, array );
            }

        }
        reader.close ();
    }

    public static long pair(long a, long b) {

        return (long) (0.5 * (a + b) * (a + b + 1) + b);
    }

    public static long[] depair(long number) {

        long t = (int) (Math.floor((Math.sqrt(8 * number + 1) - 1) / 2));
        long x = t * (t + 3) / 2 - number;
        long y = number - t * (t + 1) / 2;
        return new long[]{x, y};
    }
}
