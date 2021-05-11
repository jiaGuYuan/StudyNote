#include "mainwindow.h"
#include "ui_mainwindow.h"


MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    img = new ShowImage(this);
    img->setGeometry(QRect(0, 0, this->width(), this->height()));
    img->show();
}

MainWindow::~MainWindow()
{
    delete ui;
    delete img;
}
