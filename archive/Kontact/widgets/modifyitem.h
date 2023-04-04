#ifndef MODIFYITEM_H
#define MODIFYITEM_H

#include <QWidget>
#include <QDialog>
#include <QMessageBox>
namespace Ui {
class ModifyItem;
}

class ModifyItem : public QDialog
{
    Q_OBJECT

public:
    explicit ModifyItem(QWidget *parent = nullptr);
    ~ModifyItem();

private:
    Ui::ModifyItem *ui;
};

#endif // MODIFYITEM_H
