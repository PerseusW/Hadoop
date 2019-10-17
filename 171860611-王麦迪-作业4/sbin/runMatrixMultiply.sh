rm -rf output/
hadoop/bin/hadoop jar jar/MatrixMultiply.jar MatrixMultiply input/Matrix/M_3_4 input/Matrix/N_4_2 output
echo Output:
cat output/*
mv output/part* ans/MatrixMultiply
