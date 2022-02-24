#include "statistics.h"
#include "ui_statistics.h"
#include "core/core.hpp"
#include <QMessageBox>
#include <vector>
std::set<std::string> tmpSet;
int sumClass = -1;
extern studentsList *sList;


statistics::statistics(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::statistics)
{
    ui->setupUi(this);

    for (auto it = sList->students.begin(); it != sList->students.end(); it++){
        tmpSet.insert(it->className);
    }
    sumClass = tmpSet.size();
    auto stat = [=](){
        QString target;
        target = ui->lineEdit->text();
        if (target.isEmpty()) {
            QMessageBox::information(this, "Info", "Invalid input.");
            return;
        }
        else if (tmpSet.find(target.toStdString()) == tmpSet.end()) {
            QMessageBox::information(this, "Info", "Nothing found.");
        }
        int sum = 0;
        std::vector<QString> nameList;
        for (auto it = sList->students.begin(); it != sList->students.end(); it++){
            if (it->className == target.toStdString()) {
                sum++;
                nameList.push_back(tr(it->studentName.c_str()));
            }
        }
        QString textBrowserDisplay = tr("%1 item(s) found.\n").arg(sum);
        for (auto s : nameList){
            textBrowserDisplay = textBrowserDisplay + s + '\n';
        }
        ui->textBrowser->setText(textBrowserDisplay);
};
    ui->labelSum->setText(tr("There are %1 classes").arg(sumClass));
    connect(ui->quitButton, &QPushButton::pressed, [=](){
       close();
    });
    connect(ui->lineEdit, &QLineEdit::returnPressed, stat);
    connect(ui->pushButton, &QPushButton::clicked, stat);
}
statistics::~statistics()
{
    delete ui;
}
