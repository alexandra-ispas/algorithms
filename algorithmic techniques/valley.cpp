#include <bits/stdc++.h>
#include <iostream>
#include <fstream>
#include <string>

using namespace std;

/* function for computing the required algorithm */
long long compute_valley(int minim, int N, int idx, int *v) {
    /* the final cost for creating the valley */
    long long cost = 0;
    /* if the minimum element is the last one,
     * change the previous one to make them equal*/
    if (idx == N - 1) {
        cost += (v[idx-1] - v[idx]);
        v[idx-1] = v[idx];
    }
    /* got to the left side of the minimum element */
    int i = idx - 1;
    while (i >= 0) {
        /* get the new minimum of the subarray */
        minim = *min_element(v, v+i);
        /* all the elements to the left of the minimum
         * should be made equal to its value until*/
        while (i >= 0 && v[i] > minim) {
            cost += (v[i] - minim);
            i--;
        }
        i--;
    }
    /* got to the right side of the minimum element */
    i = idx + 1;
    while (i < N) {
        minim = *min_element(v+i, v+N);
        while (i < N && v[i] > minim) {
            cost += (v[i] - minim);
            i++;
        }
        i++;
    }
    /* if the array was initially sorted in ascending order,
     * modify the second element*/
    if (cost == 0 && idx == 0) {
        cost += (v[1] - v[0]);
    }
    /* if the array was initially sorted in descending order,
     * modify the last element*/
    if (cost == 0 && idx == N - 1) {
        cost += (v[N-1] - v[N-2]);
    }
    return cost;
}

int main() {
    /* read input data */
    string in = "valley.in", out = "valley.out";
    ifstream input;
    input.open(in);
    if (!input) {
        cout << "Could not open input file.\n";
        exit(1);
    }
    int N;
    input >> N;
    /* array of ints used for storing input data */
    int *v = (int*)malloc(N * sizeof(int));
    if (v == nullptr) {
        cout << "Allocation error\n";
        exit(EXIT_FAILURE);
    }
    /* reading from the input file and also
     * finding the minimum element and its position in the array */
    int minim = INT_MAX, idx;
    for (int i = 0; i < N; i++) {
        /* read input array */
        input >> v[i];
        /* get the minimum element of the array
         * and it's position */
        if (v[i] < minim) {
            minim = v[i];
            idx = i;
        }
    }
    input.close();
    /* write in the output file */
    ofstream output;
    output.open(out);
    output <<compute_valley(minim, N, idx, v);
    output.close();
    /* free memory */
    free(v);
    return 0;
}
