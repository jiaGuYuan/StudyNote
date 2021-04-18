# matplotlib绘图问题

## 在交互式matplotlib中,每次更新时窗口都会弹出到前面。
在交互式matplotlib中,每次更新时窗口都会弹出到前面; 导致其他软件无法正常使用(如VSCode无法进行编辑)。
参考: [https://codingdict.com/questions/173381](https://codingdict.com/questions/173381)
我遇到的问题是，在交互式绘图中，matplotlib绘图窗口始终位于最前面，并且会夺走其他应用的鼠标焦点，导致其他办公软件无法正常使用。
我希望它保持在放置它的z顺序中。
这个问题与matplotlib所使用的后端以为plt.pause函数有关。
**解决方法一: 改变后端**
```python
# 使用get_backend()函数可以获取当前正在使用哪个后端
import matplotlib
matplotlib.get_backend()
matplotlib.use('Qt5agg') # 使用matplotlib.use()函数可以修改matplotlib的后端
```
PS: 在我的Win10上将后端修改为'TkAgg'后;在交互式matplotlib中虽然窗口还是在前面，但不会夺走其他应用的鼠标焦点，其他办公软件可正常使用。

**解决方法二: 实现自定义的pause函数, 不使用plt.pause()**
如果不想更改后端，则可使用该方法。
窗口不断弹出的原因是内部plt.pause调用plt.show()。因此，自定义pause无需调用plt.show即可实现自己的show。这要求首先处于交互模式plt.ion()，然后至少调用一次plt.show()。之后，您可以使用自定义pause功能更新图
```python
def pyplot_pause(plot, interval):
    """
    在交互式matplotlib中,每次更新时窗口都会弹出到前面; 导致其他软件无法正常使用.
    使用该函数代替plt.pause()可以保持matplotlib交互时更新图像的Z顺序.
    该函数要求首先处于交互模式plt.ion()，然后至少调用一次plt.show()
    参考: https://codingdict.com/questions/173381
    """
    backend = plot.rcParams['backend']
    if backend in matplotlib.rcsetup.interactive_bk:
        figManager = matplotlib._pylab_helpers.Gcf.get_active()
        if figManager is not None:
            canvas = figManager.canvas
            if canvas.figure.stale:
                canvas.draw()
            canvas.start_event_loop(interval)
            return
```
PS: 在我的Win10上使用pyplot_pause函数替换plt.pause后;在交互式matplotlib中,窗口的Z序将被保持，窗口的更新不再影响其他办公软件的使用。