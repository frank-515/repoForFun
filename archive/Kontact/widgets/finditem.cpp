#include "finditem.h"
#include "ui_finditem.h"
#include "core/core.hpp"

extern studentsList *sList;

FindItem::FindItem(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::FindItem)
{
    ui->setupUi(this);

    auto findMethod = [=](){
        auto target = ui->lineEdit->text();
        auto type = ui->comboBox->currentIndex();
        int index = -1;
        if (target.isEmpty()) {
            QMessageBox::information(this, tr("Info"), QString("missing argument."));
            return;
        }
        switch (type){
            case 0: 
                index = sList->find_sn(target.toStdString());
                break;
            case 1:
                index = sList->find_pn(target.toStdString());
                break;
            default:
                QMessageBox::critical(this, tr("Error"), QString("Unknown error occurred."));
        }
        if (index < 0) {
            QMessageBox::information(this, tr("Error"), QString("No such student."));
            return;
        }
        Student student = sList->students.at(index);
        ui->nameLabel->setText(QString(student.studentName.c_str()));
        ui->phoneLabel->setText(QString(student.phoneNumber.c_str()));
        ui->classLabel->setText(QString(student.className.c_str()));
        vector<string> tags = student.tags;
        QString QTags;
        for (auto tag : tags){
            QTags += QString(tag.c_str());
            QTags += '\n';
        }
        ui->textBrowser->setText(QTags);
        qDebug() << "Call findMethod.";
    };
    connect(ui->okButton, &QPushButton::clicked, findMethod);
    connect(ui->lineEdit, &QLineEdit::returnPressed, findMethod);
    connect(ui->cancleButton, &QPushButton::clicked, [=](){close();});
    ui->lineEdit->setFocus();
}

FindItem::~FindItem()
{
    delete ui;
}
