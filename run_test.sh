# Cleanup (defensive).
rm -f *.class 2>/dev/null

# Compile and run.
javac Tp2.java
java Tp2 data/exemple1.txt test_output.txt

# Cleanup.
rm *.class