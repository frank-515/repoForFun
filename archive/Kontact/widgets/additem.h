#ifndef ADDITEM_H
#define ADDITEM_H

#include <string>
#include <vector>
#include <QWidget>
#include <QMessageBox>
#include <QDebug>
namespace Ui {
class AddItem;
}

class AddItem : public QDialog
{
    Q_OBJECT

public:
    explicit AddItem(QWidget *parent = nullptr);
    ~AddItem();

private:
    Ui::AddItem *ui;
};

#endif // ADDITEM_H
