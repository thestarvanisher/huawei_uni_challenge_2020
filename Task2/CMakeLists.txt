cmake_minimum_required(VERSION 3.1)

project(huawei_uni_challenge_2020)

set(CMAKE_CXX_STANDARD 20)

find_package(Threads REQUIRED)

include_directories(include)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -I/include")

# http://stackoverflow.com/questions/10555706/
macro(add_executable _name)
    # invoke built-in add_executable
    _add_executable(${ARGV})
    if (TARGET ${_name})
        target_link_libraries(${_name} ${CMAKE_THREAD_LIBS_INIT})
    endif ()
endmacro()

add_executable(huawei_uni_challenge_2020 src/main.cpp)

install(TARGETS DESTINATION ../..)