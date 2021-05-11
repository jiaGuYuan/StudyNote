#include "showimage.h"

#include <iostream>
#include <QDateTime>

QImage get_image()
{
    const uint width = 20;
    const uint height = 10;
//    std::cout << sizeof(uint) << std::endl;
    uint *pdata = new uint[width * height]();

    for (uint i=0; i<height; i++) {
        for (uint j=0; j<width; j++) {
            pdata[i*width + j] = 0xff7aa327 + int(j/5) * 100;
        }
    }

    const uint (*prow)[width] = reinterpret_cast<uint (*)[width]>(pdata);
    for (uint i=0; i<height; i++){
        for (uint j=0; j<width; j++) {
            std::cout << uint(prow[i][j]) << ", ";
        }
        std::cout << std::endl;
    }

//    delete [] pdata;
    return QImage((uchar*)pdata, width, height, QImage::Format_RGB32);
}

QString get_cur_datetime_str(QString format="yyyy-MM-dd HH:mm:ss.zzz") {
    return QDateTime::currentDateTime().toString(format);
}

ShowImage::ShowImage(QWidget *parent):QWidget(parent)
{
    img = get_image();
    QImage save_img  = img.scaled(QSize(1000, 500), Qt::IgnoreAspectRatio);
    save_img.save(QString("./output_img/") + get_cur_datetime_str("yyyy-MM-dd_HH_mm_ss_zzz")+".png", "PNG");
}
ShowImage::~ShowImage()
{
}
void ShowImage::paintEvent(QPaintEvent *e)
{
    QPainter pp(this);
    QRect Temp(0,0,this->width(),this->height());
    pp.drawImage(Temp, img);
}

