cmake_minimum_required(VERSION 3.5)

project(Kontact VERSION 0.1 LANGUAGES CXX)

set(CMAKE_INCLUDE_CURRENT_DIR ON)

set(CMAKE_AUTOUIC ON)
set(CMAKE_AUTOMOC ON)
set(CMAKE_AUTORCC ON)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)


find_package(QT NAMES Qt6 Qt5 COMPONENTS Widgets REQUIRED)
find_package(Qt${QT_VERSION_MAJOR} COMPONENTS Widgets REQUIRED)

set(PROJECT_SOURCES
        main.cpp
        mainwindow.cpp
        mainwindow.h
        mainwindow.ui
        widgets/creatnewcontact.h
        widgets/creatnewcontact.cpp
        widgets/creatnewcontact.ui
        widgets/about.h
        widgets/about.cpp
        widgets/about.ui
        widgets/additem.h
        widgets/additem.cpp
        widgets/additem.ui
        widgets/removeitem.h
        widgets/removeitem.cpp
        widgets/removeitem.ui
        widgets/modifyitem.h
        widgets/modifyitem.cpp
        widgets/modifyitem.ui
        widgets/finditem.h
        widgets/finditem.cpp
        widgets/finditem.ui
        widgets/statistics.h
        widgets/statistics.cpp
        widgets/statistics.ui
        core/core.hpp
        icon.qrc
        include/configor/configor_basic.hpp
        include/configor/configor_conversion.hpp
        include/configor/configor_declare.hpp
        include/configor/configor_encoding.hpp
        include/configor/configor_exception.hpp
        include/configor/configor_iterator.hpp
        include/configor/configor_parser.hpp
        include/configor/configor_serializer.hpp
        include/configor/configor_stream.hpp
        include/configor/configor_token.hpp
        include/configor/configor_value.hpp
        include/configor/configor.hpp
        include/configor/json.hpp
)

if(${QT_VERSION_MAJOR} GREATER_EQUAL 6)
    qt_add_executable(Kontact
        MANUAL_FINALIZATION
        ${PROJECT_SOURCES}
    )
# Define target properties for Android with Qt 6 as:
#    set_property(TARGET Kontact APPEND PROPERTY QT_ANDROID_PACKAGE_SOURCE_DIR
#                 ${CMAKE_CURRENT_SOURCE_DIR}/android)
# For more information, see https://doc.qt.io/qt-6/qt-add-executable.html#target-creation
else()
    if(ANDROID)
        add_library(Kontact SHARED
            ${PROJECT_SOURCES}
        )
# Define properties for Android with Qt 5 after find_package() calls as:
#    set(ANDROID_PACKAGE_SOURCE_DIR "${CMAKE_CURRENT_SOURCE_DIR}/android")
    else()
        add_executable(Kontact
            ${PROJECT_SOURCES}
        )
    endif()
endif()

target_link_libraries(Kontact PRIVATE Qt${QT_VERSION_MAJOR}::Widgets)

set_target_properties(Kontact PROPERTIES
    MACOSX_BUNDLE_GUI_IDENTIFIER my.example.com
    MACOSX_BUNDLE_BUNDLE_VERSION ${PROJECT_VERSION}
    MACOSX_BUNDLE_SHORT_VERSION_STRING ${PROJECT_VERSION_MAJOR}.${PROJECT_VERSION_MINOR}
    MACOSX_BUNDLE TRUE
    WIN32_EXECUTABLE TRUE
)

if(QT_VERSION_MAJOR EQUAL 6)
    qt_finalize_executable(Kontact)
endif()
