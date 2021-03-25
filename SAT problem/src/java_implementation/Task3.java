// Copyright 2020
// Author: Matei Simtinică

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Task3
 * This being an optimization problem, the solve method's logic has to work differently.
 * You have to search for the minimum number of nonArrests by successively querying the oracle.
 * Hint: it might be easier to reduce the current task to a previously solved task
 */
public class Task3 extends Task {
    private final Task2 task2Solver = new Task2 ();
    int[][] nonFriends;

    //familiile care formeaza cea mai mare grupare de familii
    // care nu sunt prietene
    String task2InFilename;
    String task2OutFilename;
    StringBuilder out = new StringBuilder ();
    StringBuilder inTask2 = new StringBuilder ();


    @Override
    public void solve() throws IOException, InterruptedException {
        task2InFilename = inFilename + "_t2";
        task2OutFilename = outFilename + "_t2";

        task2Solver.addFiles ( task2InFilename, oracleInFilename, oracleOutFilename,
                task2OutFilename );

        readProblemData ();

        while (out.length () == 0) {

            reduceToTask2 ();
            task2Solver.solve ();
            extractAnswerFromTask2 ();
        }
        writeAnswer ();
    }

    @Override
    public void readProblemData() throws IOException {

        readTasks123 ( 3 );

        for(int counter = 1; counter <= N; counter++) {

            if(map.containsKey ( counter )){

                for(int number = counter + 1; number <= N; number++){
                    if(!map.get ( counter ).contains ( number )){

                        inTask2.append ( counter)
                                .append ( " " )
                                .append ( number )
                                .append ( "\n" );
                    }
                }
            } else {
                for (int number = counter+1; number<=N; number++){

                    inTask2.append ( counter)
                            .append ( " " )
                            .append ( number )
                            .append ( "\n" );
                }
            }
        }

    }

    public void reduceToTask2() throws IOException {

        String out1 = N +
                " " +
                ( N * (N - 1) / 2 - M) +
                " " +
                K +
                "\n" +
                inTask2;

        BufferedWriter writer = new BufferedWriter ( new FileWriter ( task2InFilename ) );
        writer.write ( out1  );
        writer.close ();
    }

    public void extractAnswerFromTask2() throws IOException {

        BufferedReader reader = new BufferedReader ( new FileReader ( task2OutFilename ) );

        String truthValue = reader.readLine ();

        if (truthValue.equals ( "True" )) {

            String[] families = reader.readLine ().trim ().split ( "\\s+" );

            for(int i = N; i >= 1; i--) {
                int ok = 0;
                for (String family : families) {

                    if(Integer.parseInt ( family ) == i){
                        ok = 1;
                        break;
                    }
                }
                if(ok == 0){
                    out.append ( i ).append ( " " );
                }
            }

        } else {
            K--;
        }
    }

    @Override
    public void writeAnswer() throws IOException {

        BufferedWriter writer = new BufferedWriter ( new FileWriter ( outFilename ) );
        writer.write ( String.valueOf ( out ) );
        writer.close ();
    }

}
