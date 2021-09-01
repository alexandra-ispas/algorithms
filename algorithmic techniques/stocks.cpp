#include <bits/stdc++.h>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>

using namespace std;

/* class for storing the input data
 * 'price' is the price of a stock
 * 'profit' is the difference between maxValue and currentValue
 * 'loss' is the difference between currentValue and minValue */
class Stock {
 public:
    int price;
    int profit;
    int loss;

    Stock(int p, int pf, int l) {
        price = p;
        profit = pf;
        loss = l;
    }
};
/* comparator used for soring the stocks
 * they are sorted in ascending order by price.
 * If two prices are equal, the comparison is done
 * considering the loss a stock could cause */
bool cmp(Stock a, Stock b) {
    if (a.price < b.price)
        return true;
    if (a.price == b.price) {
        if (a.loss < b.loss)
            return true;
        if (a.loss == b.loss)
            return a.profit > b.profit;
    }
    return false;
}
/* function for computing the required algorithm */
int compute_stocks(Stock* stocks, int N, int B, int L) {
    /* allocate memory for auxiliary matrix used for dynamic programming */
    int ***matrix = (int***) malloc((N+1) * sizeof (int**));
    for (int i= 0; i <= N; i++) {
        matrix[i] = (int**) malloc((B+1) * sizeof(int*));
        for (int j = 0; j <= B; j++) {
            matrix[i][j] = (int*)malloc((L+1) * sizeof (int));
        }
    }

    int i, j , k;
    for (i = 0; i <= N; i++) {
        for (j = 0; j <= B; j++) {
            for (k = 0; k <= L; k++) {
                if (i == 0 || j == 0 || k == 0) {
                    /* for a gain/cost of 0 or it no stock is chosen,
                     * the profit is 0 */
                    matrix[i][j][k] = 0;
                } else {
                    /* check if the price can be covered with j money
                     * and the loss is less than the limit for this iteration - k*/
                    if (stocks[i - 1].price <= j && stocks[i - 1].loss <= k) {
                        /* we try to maximize the profit by choosing between
                         * its last value and the value it would reach
                         * if the current stock is chosen */
                        matrix[i][j][k] = max(stocks[i - 1].profit +
                        matrix[i-1][j-stocks[i-1].price][k-stocks[i-1].loss],
                        matrix[i-1][j][k]);
                    } else {
                        /* if the previous condition is not verified,
                         * the profit cannot be updated */
                        matrix[i][j][k] = matrix[i-1][j][k];
                    }
                }
            }
        }
    }
    /* store the result of the algorithm */
    int return_value = matrix[i-1][j-1][k-1];
    /* free memory for matrix */
    for (int i= 0; i <= N; i++) {
        for (int j = 0; j <= B; j++) {
            free(matrix[i][j]);
        }
        free(matrix[i]);
    }
    free(matrix);
    return return_value;
}

int main() {
    /* read from input file */
    string in = "stocks.in", out = "stocks.out";
    ifstream input;
    input.open(in);
    if ( !input ) {
        cout << "Could not open input file.\n";
        exit(EXIT_FAILURE);
    }
    int N, B, L;
    input >> N;
    input >> B;
    input >> L;
    /* allocate array of 'Stock' class to store the input data */
    Stock *stocks = (Stock*)malloc(N * sizeof(Stock));
    if (stocks == nullptr) {
        cout << "Allocation error.\n";
        exit(EXIT_FAILURE);
    }
    int currentValue, minValue, maxValue;
    for (int i = 0; i < N; i++) {
        input >> currentValue;
        input >> minValue;
        input >> maxValue;
        stocks[i] = Stock(currentValue, maxValue - currentValue,
                          currentValue-minValue);
    }
    input.close();
    /* sort the stocks using the previously created comparator */
    sort(stocks, stocks+N, cmp);
    /* write the result in the output file */
    ofstream output;
    output.open(out);
    output << compute_stocks(stocks, N, B, L);
    output.close();
    /* free memory for the array of stocks */
    free(stocks);
    return 0;
}
