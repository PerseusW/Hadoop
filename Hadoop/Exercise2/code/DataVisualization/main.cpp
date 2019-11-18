#include "chartview.h"

#include <QApplication>

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    ChartView *view = new ChartView();
    view->resize(800,800);
    view->show();
    return a.exec();
}
