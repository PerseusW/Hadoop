#ifndef CHARTVIEW_H
#define CHARTVIEW_H

#include <QChartView>
#include <QTimer>
#include <QScatterSeries>

QT_CHARTS_USE_NAMESPACE

class ChartView : public QChartView
{
    Q_OBJECT
public:
    explicit ChartView(QWidget *parent = nullptr);

protected:
    void mouseReleaseEvent(QMouseEvent *event) override;

private:
    QChart *scatterChart;
    QTimer *switchTimer;
    QList<QScatterSeries *> clusters;
    int clusterNum = 0;
    int iterationCount = 1;

public slots:
    void updateClusters();
};

#endif // CHARTVIEW_H
