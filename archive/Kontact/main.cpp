#include "mainwindow.h"

#include <iostream>
using std::cout;
#include <QApplication>
#include "core/core.hpp"
studentsList *sList = new studentsList();

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;
    w.show();
    return a.exec();
}
