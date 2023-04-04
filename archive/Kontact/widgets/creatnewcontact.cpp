#include "creatnewcontact.h"
#include "ui_creatnewcontact.h"
#include "qmessagebox.h"
#include "core/core.hpp"
#include <QDebug>
#include <QMessageBox>
extern studentsList *sList;
CreatNewContact::CreatNewContact(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::CreatNewContact)
{
    ui->setupUi(this);
    auto f = [=](){
        QString name = ui->nameLine->text();
        if (name.isEmpty()){
            QMessageBox::critical(this, tr("Error"), tr("Invalid Filename."));
            return;
        }
        sList->setName(name.toStdString());
        qDebug() << "set current list name:" << sList->listName.c_str();
        QMessageBox::information(this, tr("Info:"), "Successfully using " + name + ".json");
        close();
    };
    connect(ui->nameLine, &QLineEdit::returnPressed, f);
    connect(ui->okButton, &QPushButton::clicked, f);
    connect(ui->cancleButton, &QPushButton::clicked, [=](){close();});
}

CreatNewContact::~CreatNewContact()
{
    delete ui;
}
