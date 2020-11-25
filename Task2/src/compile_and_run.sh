g++ -O3 ../include/*.cpp main.cpp -o main
time ./main "$1" "$2" "$3"
rm main
