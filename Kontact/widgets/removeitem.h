#ifndef REMOVEITEM_H
#define REMOVEITEM_H

#include <QWidget>
#include <QDialog>
#include <QMessageBox>
namespace Ui {
class removeItem;
}

class removeItem : public QDialog
{
    Q_OBJECT

public:
    explicit removeItem(QWidget *parent = nullptr);
    ~removeItem();

private:
    Ui::removeItem *ui;
};

#endif // REMOVEITEM_H
