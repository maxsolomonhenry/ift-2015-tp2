#!/bin/bash

rm -f *.class test_output.txt
javac Tp2.java || exit 1

for i in {1..8}; do
    input="data/exemple${i}.txt"
    expected="data/exemple${i}+.txt"
    output="test_output.txt"

    java Tp2 "$input" "$output"

    if diff -q --strip-trailing-cr "$expected" "$output" > /dev/null; then
        echo "exemple$i: OK"
    else
        echo "exemple$i: FAIL"
        diff --strip-trailing-cr -u "$expected" "$output"
    fi
done

rm -f *.class test_output.txt