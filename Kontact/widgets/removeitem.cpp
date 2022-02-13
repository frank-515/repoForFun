#include "removeitem.h"
#include "ui_removeitem.h"
#include "core/core.hpp"

extern studentsList *sList;

removeItem::removeItem(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::removeItem)
{
    ui->setupUi(this);

    auto removeMethod = [=](){
        QString target = ui->lineEdit->text();
        int type = ui->comboBox->currentIndex();
        bool status = false;
        switch (type)
        {
        case 0:
            status = sList->remove_sn(target.toStdString());
            break;
        case 1:
            status = sList->remove_pn(target.toStdString());
            break;
        default:
            QMessageBox::critical(this, tr("Error"), tr("unknown error"));
        }
        if (!status) QMessageBox::information(this, tr("Info"), QString("Can't find  %1, nothing removed.").arg(target));
        else QMessageBox::information(this, tr("Info"), QString("Successfully removed."));
    };

    connect(ui->cancleButton, &QPushButton::clicked, [=](){close();});
    connect(ui->okButton, &QPushButton::clicked, removeMethod);
    connect(ui->lineEdit, &QLineEdit::returnPressed, removeMethod);
}

removeItem::~removeItem()
{
    delete ui;
}
