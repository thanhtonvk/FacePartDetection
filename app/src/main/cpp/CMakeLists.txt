project(facepartdetection)
cmake_minimum_required(VERSION 3.10.2)
set(OpenCV_DIR ${CMAKE_SOURCE_DIR}/opencv-mobile-4.6.0-android/sdk/native/jni)
find_package(OpenCV REQUIRED core imgproc)

set(ncnn_DIR ${CMAKE_SOURCE_DIR}/ncnn-20240410-android-vulkan/${ANDROID_ABI}/lib/cmake/ncnn)
find_package(ncnn REQUIRED)

add_library(facepartdetection SHARED facepartdetection.cpp yolo.cpp ndkcamera.cpp)

target_link_libraries(facepartdetection ncnn ${OpenCV_LIBS} camera2ndk mediandk)
