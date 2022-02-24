#include "additem.h"
#include "ui_additem.h"
#include "core/core.hpp"
extern studentsList *sList;

AddItem::AddItem(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::AddItem)
{
    ui->setupUi(this);
    int *personAdded = new int;
    *personAdded = 0;

    auto addNext = [=]() mutable{
        QString name, phone, class_;
        name = ui->nameLine->text();
        phone = ui->phoneLine->text();
        class_ = ui->classLine->text();
        
        
        if (name.isEmpty() || phone.isEmpty() || class_.isEmpty()){
            QMessageBox::critical(this, tr("Warning"), "Incomplete information");
            return;
        }
        sList->add({name.toStdString(), phone.toStdString(), class_.toStdString()});

        //这里写的很不好, 添加Tag
        std::string tags = ui->tagsLine->text().toStdString() + ' ', tmp;
        std::vector<std::string> tmpTags;
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

        (*personAdded)++;
        ui->nameLine->clear();
        ui->phoneLine->clear();
        ui->classLine->clear();
        ui->tagsLine->clear();
        ui->groupBox->setTitle(QString("%1 person added").arg(*personAdded));
        qDebug() << "personAdded: " << *personAdded;
    };

    //包括用户体验的一些优化,不知道为啥这在macOS上不起作用，Ubuntu正常
    connect(ui->doneButton, &QPushButton::clicked, [=]() mutable{
        QMessageBox::information(this, tr("Info"), QString("%1 item(s) added to list %2.").arg(*personAdded).arg(sList->listName.c_str()));
        this->close();
    });
    connect(ui->addNextButton, &QPushButton::clicked, addNext);
    connect(ui->tagsLine, &QLineEdit::returnPressed, addNext);
    connect(ui->nameLine, &QLineEdit::returnPressed, [=](){
        ui->phoneLine->setFocus();
    });
    connect(ui->phoneLine, &QLineEdit::returnPressed, [=](){
        ui->classLine->setFocus();
    });
    connect(ui->classLine, &QLineEdit::returnPressed, [=](){
        ui->tagsLine->setFocus();
    });
    connect(ui->tagsLine, &QLineEdit::returnPressed, [=](){
        ui->nameLine->setFocus();
    });
}

AddItem::~AddItem()
{
    delete ui;
}
