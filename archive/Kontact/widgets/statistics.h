#ifndef STATISTICS_H
#define STATISTICS_H
#include <set>
#include <string>
#include <QWidget>
#include <QDialog>

namespace Ui {
class statistics;
}

class statistics : public QDialog
{
    Q_OBJECT

public:
    explicit statistics(QWidget *parent = nullptr);
    ~statistics();

private:
    Ui::statistics *ui;
};

#endif // STATISTICS_H
