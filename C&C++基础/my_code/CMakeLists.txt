cmake_minimum_required(VERSION 3.0.0)
project(demo VERSION 0.1.0)

include(CTest)
enable_testing()
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -g -Wall -pthread -fPIC")

# 为了使用Qt的库, 需要
# 1. 配置qt的头文件路径,
# 2. 将需要将-fPIE or -fPIC标志添加到编译器
# 3. 配置链接库(Qt5Core等)
set(QT_INCLUDE 
    /usr/include/x86_64-linux-gnu/qt5
)
INCLUDE_DIRECTORIES(
    "${QT_INCLUDE}" # 引号可选
    "${QT_INCLUDE}/QtCore"
)
# 链接库目录
LINK_DIRECTORIES(
    "/usr/lib/x86_64-linux-gnu/"
)
set(src_files
    ${CMAKE_CURRENT_SOURCE_DIR}/main.cpp 
    # ${CMAKE_CURRENT_SOURCE_DIR}/test.cpp
)


# 指定生成目标
ADD_EXECUTABLE(demo 
    ${src_files}
)

# 添加链接库 -- 需要写在ADD_EXECUTABLE之后
TARGET_LINK_LIBRARIES(demo Qt5Core) # libXXX.so

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
include(CPack)
