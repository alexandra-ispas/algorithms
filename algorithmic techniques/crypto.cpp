#include <bits/stdc++.h>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>

using namespace std;
/*
 * comparator used for sorting the paris of
 * <Pi, Ui>, in increasing order, by Pi
 */
bool cmp(pair<int, int> a, pair<int, int> b) {
    return a.first < b.first;
}

/* function for computing the required algorithm */
int compute_crypto(vector<pair<int, int>> pairs, int N, int B) {
    /* array for storing the total cost required in order to
     * increase by 1 the power of the first 'i' computers */
    int *dp = (int*) malloc(N * sizeof(int));
    if (dp == nullptr) {
        cout << "Allocation error.\n";
        exit(EXIT_FAILURE);
    }
    /* number of coins */
    int power = pairs[0].first;
    dp[0] = pairs[0].second;
    int i = 1;
    while (1) {
        dp[i] = dp[i-1];
        /* if there is a gap between the powers of 2 consecutive
         * computers, we should see if we can reach the power of the
         * stronger one, or settle for something less powerful,
         * depending on how much money we have left */
        int step = pairs[i].first - power;
        if (step > 0) {
            /* the number of units of power we can afford to
             * add to out current computers */
            int times = floor(B / dp[i]);
            if (times <= step) {
                /* if the user cannot reach the power of the next computer,
                * he has no money left and the algorithm ends here */
                /* update the number of coins created by the system
                 * and the money left */
                power += times;
                B -= times * dp[i];
                break;
            } else {
                /* if the user has more money than necessary to get to the
                 * next computer's power, the algorithm continues and we
                 * only update these values */
                power += step;
                B -= step * dp[i];
            }
        }
        /* the current cost increases as we have added
         * a new computer, the one from the ith position */
        dp[i] += pairs[i].second;
        /* if we don't have enough money to make another update
         * or we have iterated over all the devices, we exit the loop */
        if (B < dp[i] || i >= N)
            break;
        i++;
    }
    /* if there is money left, Gigel will try to update the
     * current computers */
    power += B / dp[i];
    /* free memory */
    free(dp);
    return power;
}

int main(void) {
    /* read from input file */
    string in = "crypto.in", out = "crypto.out";
    ifstream input;
    input.open(in);
    if (!input) {
        cout << "Could not open input file.\n";
        exit(EXIT_FAILURE);
    }
    int N, B;
    /* the input is stored as an array of pairs */
    vector<pair<int, int>> pairs;
    input >> N;
    input >> B;
    int aux1, aux2;
    for ( int i = 0; i < N; i++ ) {
        input >> aux1 >> aux2;
        pairs.push_back(make_pair(aux1, aux2));
    }
    input.close();
    /* sort the input in ascending onder by the processing power */
    sort(pairs.begin(), pairs.end(), cmp);
    /* write in the output file */
    ofstream output;
    output.open(out);
    output << compute_crypto(pairs, N, B);
    output.close();
    return 0;
}
