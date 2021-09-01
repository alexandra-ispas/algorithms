#include <bits/stdc++.h>
#include <iostream>
#include <fstream>
#include <string>

using namespace std;

unsigned long long int compute_ridge(unsigned long long int  **dp,
                                        int **hills, int N) {
    unsigned long long int aux, mini, second_minimum;
    unsigned long long int new_second_mini = ULLONG_MAX, new_mini = ULLONG_MAX;
    long poz, new_poz;
    for (int i = 0; i < N; i++) {
        /* update the minimum heights from the previous iteration
         * and set new ones to the maximum */
        if (i > 0) {
            second_minimum = new_second_mini;
            mini = new_mini;
            poz = new_poz;
            new_mini = ULLONG_MAX;
            new_second_mini = ULLONG_MAX;
        }
        /* iterate over the possible changes for a hill.
         * For the algorithm to be efficient, the height can remain unchanged,
         * or it can be decreased by one or two */
        long j;
        for (long k = hills[i][0]; k >= max(0, hills[i][0]-2); k--) {
            /* how much the height has changed */
            j = hills[i][0] - k;
            if (i == 0) {
                /* for the first hill in the array, the cost doesn't
                 * depend on any previous operation, it only depends on it's
                 * own cost and the number of changes */
                dp[i][j] = (hills[i][0] - k) * hills[i][1];
                /* compute the minimum cost and its position */
                if (dp[i][j] < new_mini) {
                    new_second_mini = new_mini;
                    new_mini = dp[0][j];
                    new_poz = k;
                } else if (dp[i][j] < new_second_mini) {
                    /* this is a guard for the case when the minimum cost
                     * is found at j = 0 */
                    new_second_mini = dp[i][j];
                }
            }  else {
                dp[i][j] = ULLONG_MAX;
                /* at each step, the previous cost used is the minimum one,
                 * considering that the new element's value after it is
                 * modified will be different from the value which corresponds
                 * to that minimum element */
                if (poz != k) {
                    aux = mini;
                } else {
                    aux = second_minimum;
                }
                /* a choice is made in order to minimize the cost */
                dp[i][j] = min(dp[i][j],
                               (hills[i][0] - k) * hills[i][1] + aux);
                /* the minimum values are updated */
                if (dp[i][j] < new_mini) {
                    new_second_mini = new_mini;
                    new_mini = dp[i][j];
                    new_poz = k;
                } else if (dp[i][j] < new_second_mini) {
                    /* the same guard */
                    new_second_mini = dp[i][j];
                }
            }
        }
    }
    return new_mini;
}

int main() {
    /* read from input file */
    string in = "ridge.in", out = "ridge.out";
    ifstream input;
    input.open(in);
    if ( !input ) {
        cout << "Could not open input file.\n" << endl;
        exit(1);
    }
    int N;
    input >> N;
    /* matrix used for storing input data */
    int **hills = (int**)malloc(N * sizeof(int*));
    if (hills == nullptr) {
        cout << "Allocation error.\n";
        exit(EXIT_FAILURE);
    }
    /* allocate memory for matrix used to implement dynamic programming */
    unsigned long long int  **dp = (unsigned long long int **)malloc(N *
                            sizeof (unsigned long long int*));
    for (int i = 0; i < N; i++) {
        hills[i] = (int*)malloc(2 * sizeof(int));
        if (hills[i] == nullptr) {
            for (int j = 0; j < i; j++) {
                free(hills[j]);
                free(dp[i]);
            }
            free(hills);
            free(dp);
            cout << "Allocation error.\n";
            exit(EXIT_FAILURE);
        }
        input >> hills[i][0];
        input >> hills[i][1];
        /* allocate memory for every row in the matrix */
        dp[i] = (unsigned long long int*)malloc(3 *
                        sizeof(unsigned long long int));
        if (dp[i] == nullptr) {
            for (int j = 0; j < i; j++) {
                free(dp[j]);
                free(hills[j]);
            }
            free(dp);
            free(hills);
            cout << "Allocation error.\n";
            exit(EXIT_FAILURE);
        }
    }
    /* write in the output file */
    ofstream output;
    output.open(out);
    output << compute_ridge(dp, hills, N);
    output.close();
    /* free memory */
    for (int i = 0; i < N; i++) {
        free(dp[i]);
        free(hills[i]);
    }
    free(dp);
    free(hills);
    return 0;
}
