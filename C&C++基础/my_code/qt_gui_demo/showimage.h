#ifndef SHOWIMAGE_H
#define SHOWIMAGE_H

#include <QImage>
#include <QWidget>
#include <QPainter>
#include <QPaintEvent>

class ShowImage:public QWidget
{
public:
    ShowImage(QWidget *parent = 0);
    ~ShowImage();
    void paintEvent(QPaintEvent *e);
private:
    QImage img;
};

#endif // SHOWIMAGE_H
