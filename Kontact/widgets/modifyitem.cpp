#include "modifyitem.h"
#include "ui_modifyitem.h"
#include "core/core.hpp"

extern studentsList *sList;

ModifyItem::ModifyItem(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::ModifyItem)
{
    ui->setupUi(this);
    //⚠️： 代码写在setupUI以下，不然等着Null parameter    
    auto modifyMethod = [=]() {
        QString findLine = ui->lineEdit->text();
        QString targetLine = ui->lineEdit_2->text();
        if (findLine.isEmpty() || targetLine.isEmpty()){
            QMessageBox::warning(this, "warring", "Missing argument");
            return;
        } 

        int type = ui->comboBox->currentIndex(), targetIndex = -1;
        switch(type){
            case 0:
                targetIndex = sList->find_sn(findLine.toStdString());
                if (targetIndex < 0) {
                    QMessageBox::information(this, tr("Error"), QString("there is no %1 %2 on the list").arg(ui->comboBox->currentText()).arg(findLine));
                    return;
                }
                break;
            case 1:
                targetIndex = sList->find_pn(findLine.toStdString());
                if (targetIndex < 0) {
                    QMessageBox::information(this, tr("Error"), QString("there is no %1 %2 on the list").arg(ui->comboBox->currentText()).arg(findLine));
                    return;
                }
                break;
            default:
                QMessageBox::critical(this, tr("Error"), QString("Unknown error occurred."));
                return;
        }
        QString name = QString(sList->students.at(targetIndex).studentName.c_str());
        int modifyType = ui->comboBox_2->currentIndex();
        bool status = false;
        std::string tags = targetLine.toStdString(), tmp;
        std::vector<std::string> tmpTags;
        switch (modifyType) {
            case 0:
                status = sList->modify_sn(targetIndex, targetLine.toStdString());
                break;
            case 1:
                status = sList->modify_pn(targetIndex, targetLine.toStdString());
                break;
            case 2:
                status = sList->modify_cn(targetIndex, targetLine.toStdString());
                break;
            case 3: //这里复用前面的代码，还把变量声明移动到前面去了，鬼知道会出现什么问题。。。
                sList->students.at(targetIndex).tags.clear();
                
                for (auto c = tags.begin(); c != tags.end(); ++c) {
                    if (*c != ' ') tmp += *c;
                    else {
                        tmpTags.push_back(tmp);
                        tmp.clear();
                    }
                }
                for (auto tag : tmpTags) {
                    sList->students.back().addTag(tag);
                }
                status = true;
                break;
            default:
                QMessageBox::critical(this, tr("Error"), tr("Unknown error occurred."));
        }
        if (!status) QMessageBox::critical(this, tr("Error"), tr("Unknown error occurred."));
        else {
            QMessageBox::information(this, tr("Info"), QString("Successfully modified %1's %2 to %3").arg(name).arg(ui->comboBox_2->currentText()).arg(ui->lineEdit_2->text()));
        }
        close();
    };
    connect(ui->okButton, &QPushButton::clicked, modifyMethod);
    connect(ui->lineEdit_2, &QLineEdit::returnPressed, modifyMethod);
    connect(ui->lineEdit, &QLineEdit::returnPressed, [=](){
        ui->lineEdit_2->setFocus();
    });
    connect(ui->cancleButton, &QPushButton::clicked, [=](){close();});
    
}

ModifyItem::~ModifyItem()
{
    delete ui;
}
