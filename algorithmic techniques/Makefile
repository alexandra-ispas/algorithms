P1 = crypto.cpp
P2 = stocks.cpp
P3 = valley.cpp
P4 = ridge.cpp
P5 = trigigel.cpp
HEADERS = $(wildcard *.h)


CC = g++
CFLAGS = -Wall -Wextra -std=c++17 -g
LDFLAGS = -lm

TARGET = main

build: crypto stocks valley ridge trigigel
	
crypto: $(P1)
	$(CC) $(CFLAGS) $(LDFLAGS) $(P1) -o crypto
	
stocks: $(P2) 	
	$(CC) $(CFLAGS) $(LDFLAGS) $(P2) -o stocks
	
valley: $(P3) 	
	$(CC) $(CFLAGS) $(LDFLAGS) $(P3) -o valley
	
ridge: $(P4) 	
	$(CC) $(CFLAGS) $(LDFLAGS) $(P4) -o ridge
	
trigigel: $(P5) 	
	$(CC) $(CFLAGS) $(LDFLAGS) $(P5) -o trigigel	
	
run-p1:      crypto
	./crypto

run-p2:      stocks
	./stocks

run-p3:      valley
	./valley

run-p4:      ridge
	./ridge

run-p5: trigigel
	./trigigel

clean:
	rm -f crypto stocks valley ridge trigigel
	
.PHONY: build clean

