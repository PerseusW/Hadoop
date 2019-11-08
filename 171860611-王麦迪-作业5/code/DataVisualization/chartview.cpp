#include "chartview.h"
#include <QLegendMarker>
#include <QTextStream>
#include <QRandomGenerator>
#include <QDebug>

ChartView::ChartView(QWidget *parent) : QChartView(new QChart(), parent)
{
    QFile file("C:/Users/user/Desktop/output/0-C/part-r-00000");
    if (!file.open(QFile::ReadOnly)) {
        exit(0);
    }
    QTextStream inputStream(&file);
    while (!inputStream.atEnd()) {
        clusterNum++;
        inputStream.readLine();
    }
    file.close();
    scatterChart = chart();
    scatterChart->setTitle("Cluster Visualization");
    scatterChart->setTheme(QChart::ChartThemeHighContrast);
    scatterChart->legend()->setMarkerShape(QLegend::MarkerShapeFromSeries);
    switchTimer = new QTimer(this);
    connect(switchTimer,SIGNAL(timeout()),this,SLOT(updateClusters()));
    for (int i = 0; i < clusterNum; ++i) {
        QScatterSeries *cluster = new QScatterSeries();
        cluster->setName("Cluster " + QString::number(i));
        cluster->setMarkerShape(QScatterSeries::MarkerShapeRectangle);
        cluster->setMarkerSize(10.0);
        clusters.append(cluster);
    }
}

void ChartView::mouseReleaseEvent(QMouseEvent *event)
{
    switchTimer->start(1000);
    event->accept();
}

void ChartView::updateClusters()
{
    scatterChart->setTitle("Iteration " + QString::number(iterationCount));
    for (QScatterSeries *cluster : clusters) {
        cluster->clear();
    }
    QFile file("C:/Users/user/Desktop/output/" + QString::number(iterationCount++) + "-P/part-r-00000");
    if (!file.open(QIODevice::ReadOnly)) {
        exit(0);
    }
    QTextStream inputStream(&file);
    while (!inputStream.atEnd()) {
        QString line = inputStream.readLine();
        QStringList keyValue = line.split("\t");
        int clusterId = keyValue.at(0).toInt();
        QStringList clusterInfo = keyValue.at(1).split(",");
        double x = clusterInfo.at(2).toDouble();
        double y = clusterInfo.at(3).toDouble();
        clusters.at(clusterId - 1)->append(x,y);
    }
    file.close();
    for (QScatterSeries *cluster: clusters) {
        if (iterationCount != 2) {
            scatterChart->removeSeries(cluster);
        }
        scatterChart->addSeries(cluster);
        cluster->setPen(QPen(cluster->color()));
    }
    scatterChart->createDefaultAxes();
}
