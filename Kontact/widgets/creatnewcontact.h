#ifndef CREATNEWCONTACT_H
#define CREATNEWCONTACT_H

#include <QWidget>
#include <QDialog>
namespace Ui {
class CreatNewContact;
}

class CreatNewContact : public QDialog
{
    Q_OBJECT

public:
    explicit CreatNewContact(QWidget *parent = nullptr);
    ~CreatNewContact();

private:
    Ui::CreatNewContact *ui;
};

#endif // CREATNEWCONTACT_H
