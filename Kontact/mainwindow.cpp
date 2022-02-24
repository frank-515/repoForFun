#include "mainwindow.h"
#include "./ui_mainwindow.h"
#include "widgets/creatnewcontact.h"
#include "core/core.hpp"
#include "widgets/about.h"
#include "widgets/additem.h"
#include "widgets/removeitem.h"
#include "widgets/modifyitem.h"
#include "widgets/finditem.h"
#include "widgets/statistics.h"

extern studentsList *sList;

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    
    QLabel *currentFile = new QLabel("Untitled", this);
    QAction *aboutAction = ui->menuKontact->addAction("About Kontact");
    QAction *quitAction = ui->menuKontact->addAction("Quit Kontact");
    QMenu *menuFile = new QMenu("File", this);
    QMenu *menuEdit = new QMenu("Edit", this);
    QAction *saveAction = menuFile->addAction("Save");
    QAction *reloadAction = menuFile->addAction("Reload");
    QAction *addItemAction = menuEdit->addAction("Add");
    QAction *removeItemAction = menuEdit->addAction("Remove");
    QAction *modifyItemAction = menuEdit->addAction("Modify");
    QAction *findItemAction = menuEdit->addAction("Find");
    QAction *statisticsAction = menuEdit->addAction("Statistics");
    ui->statusbar->addWidget(currentFile);
    ui->menuKontact->addSeparator();
    ui->menubar->addSeparator();
    ui->menubar->addMenu(menuFile);
    ui->menubar->addMenu(menuEdit);

    connect(findItemAction, &QAction::triggered, [=](){
        QWidget *newWin = new FindItem(this);
        newWin->setAttribute(Qt::WA_DeleteOnClose);
        newWin->show();
    });
    connect(modifyItemAction, &QAction::triggered, [=](){
        QWidget *newWin = new ModifyItem(this);
        newWin->setAttribute(Qt::WA_DeleteOnClose);
        newWin->show();
    });
    connect(removeItemAction, &QAction::triggered, [=](){
        QWidget *newWin = new removeItem(this);
        newWin->setAttribute(Qt::WA_DeleteOnClose);
        newWin->show();
    });
    connect(statisticsAction, &QAction::triggered, [=](){
       QWidget *newWin = new  statistics(this);
       newWin->setAttribute(Qt::WA_DeleteOnClose);
       newWin->show();
    });
    connect(ui->actionCreat, &QAction::triggered, [=]() {
        currentFile->setText("File: "+ tr(sList->listName.c_str()));
        //qDebug() << "set current list name:" << currentFile->text();
    });
    connect(aboutAction, &QAction::triggered, [=](){
        QWidget *aboutWin = new About(this);
        aboutWin->setAttribute(Qt::WA_DeleteOnClose);
        aboutWin->show();
    });
    connect(quitAction, SIGNAL(triggered()), this, SLOT(close()));
    connect(saveAction, &QAction::triggered, [=](){
        if (sList->listName.empty()){
            QMessageBox::warning(this, "warning", "Untitled file");
            return;
        }
        sList->saveJson();
        QMessageBox::information(this, tr("Info"), tr("successfully saved as ") + sList->listName.c_str() + tr(".json"));
    });
    connect(reloadAction, &QAction::triggered, [=](){
        sList->loadJson();
    });
    connect(addItemAction, &QAction::triggered, [=](){
        QWidget *newWin = new AddItem();
        newWin->setAttribute(Qt::WA_DeleteOnClose);
        newWin->show();
    });



    connect(ui->fileButton, &QPushButton::pressed, [=](){
       QWidget *fileWin = new CreatNewContact(this);
       fileWin->setAttribute(Qt::WA_DeleteOnClose);
       fileWin->show();
    });

    connect(ui->addButton, &QPushButton::pressed, [=](){
       QWidget *addWin = new AddItem(this);
       addWin->setAttribute(Qt::WA_DeleteOnClose);
       addWin->show();
    });

    connect(ui->modifyButton, &QPushButton::pressed, [=](){
        QWidget *modifyWin = new ModifyItem(this);
        modifyWin->setAttribute(Qt::WA_DeleteOnClose);
        modifyWin->show();
    });
    connect(ui->removeButton, &QPushButton::pressed, [=](){
        QWidget *removeWin = new removeItem(this);
        removeWin->setAttribute(Qt::WA_DeleteOnClose);
        removeWin->show();
    });
    connect(ui->findButton, &QPushButton::pressed, [=](){
        QWidget *findWin = new FindItem(this);
        findWin->setAttribute(Qt::WA_DeleteOnClose);
        findWin->show();
    });

    connect(ui->refreshButton, &QPushButton::pressed, [=](){
        if (currentFile->text() == tr("Untitled")) return;
        sList->loadJson();
    });


}

MainWindow::~MainWindow()
{
    delete ui;
}




void MainWindow::on_actionCreat_triggered()
{
    CreatNewContact newWin(this);
    //使用DeleteOnClose会导致double free
    //newWin.setAttribute(Qt::WA_DeleteOnClose);
    newWin.exec();
    // QWidget *newWinCreat = new CreatNewContact(this);
    // newWinCreat->setAttribute(Qt::WA_DeleteOnClose);
    // newWinCreat->show();
}

