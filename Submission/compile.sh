export PATH="/opt/gcc-10.2.0/bin:$PATH"
export LD_LIBRARY_PATH=/opt/gcc-10.2.0/lib64
g++ -O3 -pthread include/*.cpp src/parallel.cpp -o main