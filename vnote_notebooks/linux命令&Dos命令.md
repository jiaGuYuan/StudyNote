pm2常用命令:
    pm2 status # 查看程序的状态 (pm2 list)
    pm2 show XXX # # 查看查看进程详细信息
    pm2 stop XXX # 停止XXX程序. XXX可以是"App name"或id.[all--表示所有]
    pm2 start XXX # 启动XXX程序
    pm2 restart XXX # 启动XXX程序
    pm2 delect XXX # 删除PM2进程
    pm2 logs # 查看实时日志

git:
    git branch -a # 查看分支. -r: 查看远程分支
    git checkout -b branchname # 切换分支
    
    # 将远程的develop分支设置为与master一致:(将远程的develop分支用与master覆盖)
        # git checkout develop // 切换到develop分支 
        # git reset --hard origin/master // 用远程的master覆盖本地的develop分支
        # git push origin develop  --force // 再develop到远程仓库(此时远程的develop分支将与master一致)
    
    git强制使用远程master覆盖本地工作区:
        git fetch --all 
        git reset --hard origin/master
        git pull
        
xargs: 在不同命令中传递参数
    -t: 表示先打印命令,然后再执行
    -i/-I: 将xargs的每项,一般是一行一行赋值给{}, 可以用{}代替
        eg: ls *.so | xargs -I {} cp {} ./libs 将以'.so'结尾的文件复制到'./libs'目录下
    -d: delim 分隔符,默认的xargs分隔符是回车,argument的分隔符是空格,这里修改的是xargs的分隔符
    -L: num 从标准输入一次读取 num 行送给 command 命令

创建文件:
    touch fileName

    
创建软连接: ln
    ln -s 源文件 目标文件(创建的软连接文件)
    -s 选项,表示创建软连接.软连接类似win的快捷方式,删除软连接不会影响原文件
    eg: ln -s srcfileName createLinkfileName  #srcfileName<==createLinkfileName
    
find:
    当前目录及子目录下查找所有以.txt和.pdf结尾的文件:
        find . \( -name "*.txt" -o -name "*.pdf" \)
        或find . -name "*.txt" -o -name "*.pdf" 

    在当前目录下搜索名为filename的文件,并将它删除:
        find . -name filename -exec rm {} \;

    在当前目录下搜索文件名匹配*filename*的文件,并将它删除:
        find . -name *filename* -exec rm {} \;

    从根目录开始查找所有扩展名为.log的文本文件,并找出包含"ERROR"的行:
        find / -type f -name "*.log" | xargs grep -n "ERROR" //-n:输出行号
        例子:从当前目录开始查找所有扩展名为.in的文本文件,并找出包含"thermcontact"的行
        find . -name "*.in" | xargs grep "thermcontact"
    
    在当前目录下(不递归)查找所有扩展名为.log的文本文件,并找出包含"ERROR"的行:
        find . -maxdepth 1 -type f -name "*.log" | xargs grep -i "ERROR"
        
    从根目录开始查找所有包含名为crypto.so的库文件,并将过滤掉错误信息:(如 find: "/lost+found": 权限不够 ...)
        find / -name *crypto.so*  2>/dev/null 
        

    将当前目录及其子目录(但排除./copy_outdir子目录)下的所有".o"文件移动到"./copy_outdir":
        find . -path "./copy_outdir" -prune -o \( -name "*.o" -exec mv  {} ./copy_outdir/ \; \)
        注: -path "./copy_outdir" 指定寻找目录或文件路径的范本样式
            -prune 表示排除(不寻找的文件或目录的范本样式)
            -path "./copy_outdir" -prune: 表示排除"./copy_outdir"目录
            Note:
                从当前目录排除'./prune_dir'后的其他位置找出所有'.o'后缀的文件
                    find . -path "./prune_dir" -prune -o -name "*.o" -print
                    等价于
                    find . -path "./prune_dir" -prune -o \( -name "*.o" -print \) # 重点理解这个就行
                    等价于
                    find . \( \( -path "./prune_dir" -prune \) -o \( -name "*.o" -print \) \) # 等价于
                    find . \( \( -path "./prune_dir" -a -prune \) -o \( -name "*.o"  -a -print \) \) # -a默认省略
                    
                说明:
                    查找过程中,如果是当前目录下的prune_dir,则-o后面的语句不会执行(利用-o的短路功能);
                               如果是当前目录下的其它文件或目录,则-o后面的语句会执行.
                    逻辑运算符优先级:
                        a || b && c <==> a || (b && c)
                    

    删除掉当前目录及子目录下的所有"*.cxx"后缀的文件:
        find . -name "*.cxx" -type f -print -exec rm -rf {} \;
            -name "*.cxx": 搜索满足"*.cxx"的文件(目录也是文件)
            -type f: 搜索文件(不包括目录)
            -exec rm -rf {} \; : -exec "command" \;表示对find附加命令"command", "{}"表示find找到的文件
                 
    避开多个文件夹:
        find /usr/sam \( -path /usr/sam/dir1 -o -path /usr/sam/file1 \) -prune -o -print
        注: 括号"()"内两侧有空格

    find命令选项:
        -maxdepth: 指定最大搜索深度
        -name: 按文件名查找(区分大小写); -iname: 不区分文件名大小写
        -o 是或者的意思  ---与||类似的短路求值.find . xxx -o yyy  如果 -o 之前的xxx为true那么-o之后的内容将不会将执行
        -a 是而且的意思(默认-a可以省略)  ---与&&类似的短路求值.find . xxx -a yyy;  如果 -o 之前的xxx为false那么-o之后的内容将不会将执行
        -not 是相反的意思


    在/etc目录下找文件大小在1M以上的文件并且显示,文件用换行符隔开;-exec后面的{} \;是不能少的:
        sudo find /etc -size +1M -exec echo {} \;
    达到-exec相同的功能但是用空格分开寻找到的文件:
        sudo find /etc -size +1M | xargs echo
    
    在当前目录下搜索username用户的文件:
        find ./ -user username
        
    在当前目录下搜索非username用户的文件:
        find ./ -not -user username 
    
    在当前目录下搜索除文件名fielname之外的文件:
        find ./ -not -name fielname
    
    寻找不匹配的文件:在当前目录下搜索除specificType类型之外的文件:
        find ./ -not -type specificType(一般文件f,目录d,字符文件c)
    
    寻找当前目录下指定用户文件或者(-o)一般文件:
        find ./ -user 用户名 -o -type f
        
    寻找当前目录下文件大于1M的文件或者是目录:
        find ./ -size +1M -o -type d
    
    寻找当前目录下文件小于1M并且文件类型是一般文件的文件:
        find ./ -size -1M -a -type f
     
    按修改时间查找文件:
        [a|c|m]min: [最后访问|最后状态修改|最后内容修改]min; 计量单位是分钟
        [a|c|m]time: [最后访问|最后状态修改|最后内容修改]time; 计量单位是天(即24H)
        
        ----(+n)---------|------------(n)--------------|---------(-n)----|当前
         (n+1)*24H之前   |     n*24H ~ (n+1)*24H之间   |     n*24h之内   |

        -mtime -n: 查找距现在 0 ~ n*24H 内修改过的文件
        -mtime n:  查找距现在 n*24H ~ (n+1)*24H 内修改过的文件
        -mtime +n: 查找距现在 (n+1)*24H 前修改过的文件

        eg: 
            find ./ -mtime -2 -name "*.log" # 查找2天内修改的log文件
            find ./ -mtime +2 -name "*.log" # 查找最后一次修改时间超过2天的log文件
            find ./ -mmin  -10  -name "*.log" # 查找10分钟内修改的log文件

        linux 文件的几种时间 (以 find 为例):
            atime 最后一次访问时间, 如 ls, more 等, 但 chmod, chown, ls, stat 等不会修改些时间, 使用 ls -utl 可以按此时间顺序查看;
            ctime 最后一次状态修改时间, 如 chmod, chown 等状态时间改变但修改时间不会改变, 使用 stat file 可以查看;
            mtime 最后一次内容修改时间, 如 vi 保存后等, 修改时间发生改变的话, atime 和 ctime 也相应跟着发生改变.
            注意: linux 里是不会记录文件的创建时间的, 除非这个文件自创建以来没有发生改变, 那么它的创建时间就是它的最后一次修改时间.

    find 正则查找: '-regex'(区分大小写); '-iregex'(不区分大小写)
        -regex属于测试项. 使用-regex时要注意: -regex不是匹配文件名, 而是匹配完整的文件名(包括路径).
        例如,有一个文件"/xxx/yyy/abar9",如果你用"ab.*9"来匹配,将查找不到任何结果,正确的方法是使用".*/ab.*9"来匹配.
        查找'test_'前缀加多个数字的c文件,可以这么写: 
            $ find . -regex ".*/test_[0-9]+/.c" -print
            
    参数说明:
    
        -o 是或者的意思
        -a 是而且的意思
        -not 是相反的意思
        根据上面再从find的寻找方式中任意组合你乐意的方式.
        find /etc -not -perm mode(222)
        find /etc -not -perm -mode(-222)
        find /etc -not -perm +mode(+222)
        #-perm是按文件权限来查找文件
        mode是完全匹配所对应的权限,如果不包括suid/sgid/sticky
        -mode是权限位转化为二进制之后的1必须全部匹配,+mode则需要其中任何一个1被匹配.
        -mode应该包含mode,+mode则包含-mode.
        
        

Systemd & systemctl:
    Systemd为系统的启动和管理提供一套完整的解决方案. Systemd是系统的第一个进程(PID=1),其他进程都是它的子进程.
    systemctl是Systemd 的主命令,用于管理系统
    管理服务:
        systemctl start jupyter.service #启动
        systemctl stop jupyter.service #停止
        systemctl status jupyter.service #状态,可查看是否成功运行
        systemctl restart jupyter.service #重启
        systemctl enable jupyter.service #开机自启
        systemctl disable jupyter.service #关闭开机自启
        systemctl systemctl daemon-reload #重载守护进程,更改service文件后需要执行.
        

Supervisor 进程管理工具:
    Supervisor是一个 C/S 模型的程序; supervisord是server端, supervisorctl是client端.
    supervisord -c /etc/supervisord.conf # 以指定配置文件启动supervisord        
    supervisorctl:
        > status    # 查看程序状态
        > start apscheduler  # 启动 单一程序
        > stop toutiao:*   # 关闭 toutiao组 程序
        > start toutiao:*  # 启动 toutiao组 程序
        > restart toutiao:*    # 重启 toutiao组 程序
        > update    # 重启配置文件修改过的程序


IO重定向:
    command > filename 把标准输出重定向到一个新文件中
    command >> filename 把标准输出重定向到一个文件中(追加)
    command 1 > fielname 把标准输出重定向到一个文件中
    command > filename 2>&1 把标准输出和标准错误一起重定向到一个文件中
    command 2 > filename 把标准错误重定向到一个文件中
    command 2 >> filename 把标准输出重定向到一个文件中(追加)
    command >> filename 2>&1 把标准输出和标准错误一起重定向到一个文件中(追加)
    command < filename > filename2 把command命令以filename文件作为标准输入,以filename2文件作为标准输出
    command < filename 把command命令以filename文件作为标准输入
    command << delimiter 把从标准输入中读入,直至遇到delimiter分界符
    command <&m 把文件描述符m作为标准输入
    command >&m 把标准输出重定向到文件描述符m中
    command <&- 把关闭标准输入

    说明:如果是重定向到一个已打开的文件描述符,写需要写成 &fd.

将输出信息同时输出到终端和文件中：command | tee output.txt

查看进程:
    ps -ef | grep XXX
    或 ps -aux | grep XXX

查看进程打开的文件描述符:
    cd /proc/$(pidof Process_name)/fd   # pidof 程序名 可以直接得到进程id
    相当于 pidof  Process_nam 得到进程ID, 再cd /proc/ID_X/fd

grep:
    按正则查找:
        grep -e "正则表达式"
    -i: 不区分大小写
        grep -i "要查找的字符串"
        
    -A ROWNUM: 输出匹配位置后的ROWNUM行
        grep -A 100 "[ATS]" shield.ini //输出匹配位置后的100行
    
    查找Telnet服务器的端口号(/etc/services中包含了服务与端口)
    grep telnet /etc/services  

    #查找当前目录下所有匹配matchstring的文件, 并将这些文件中的string1替换为string2
    grep -rlb "matchstring" . | xargs sed -i "s/string1/string2/g"


    #将 int 
    #   main() 格式的main函数定义转换为 int main()格式
    find . -name "*.c"| xargs sed -i "/\(^int\)/,/\(^main.*)$\)/{/^int$/d; s/main/int main/}"

    在int main()函数前插入 Doxygen格式的注释/** */
    find . -name "*.c"| xargs sed -i "s#\(^int main\)#/**\n *\n */\n\1#"

前/后台执行:
    command >out.file 2>&1 &: 在后台执行命令command,并当标准输入和输出重定向到out.file．但是当关闭控制台后该后台作业会停止运行．
    nohup command >out.file 2>&1 &: 后台执行命令command,关闭控制台作业不会被挂起(no hang up).
    ctrl + z :　可以将一个正在前台执行的命令放到后台,并且处于暂停状态．
    Ctrl+c　: 终止前台命令
    jobs :查看当前shell在后台运行的任务(-l 选项可显示所有任务的PID)．
    ps -aux | grep command : 查看正在执行的command的相关信息

linux查看.so文件的命令:
    nm
    查看动态库符号:
        nm -D libims_r8.so
    
添加库的查找路径:
    LD_LIBRARY_PATH=$LD_LIBRARY_PATH:要添加的路径
    export LD_LIBRARY_PATH
  
查看系统版本:
    cat /proc/version
    
杀死进程: 
```
linux: kill, pkill, killall
    通过进程号杀进程: kill
    通过进程名杀进程: pkill, killall
    列出信号名: kill -l
        杀进程一般使用信号9(SIGKILL): kill -s KILL 256 (kill -9 256)
    eg: 杀掉所有ap进程: killall ap_proc

    找到某进程并kill它:(如jupyter-notebook)
        ### !!! 'grep -v grep'表示去除包含grep的进程行
        ps -ef | grep jupyter-notebook | grep -v grep| awk '{print $2}' | xargs kill >/dev/null 2>&1 & 
        

windows: tasklist & taskkill
查找指定进程: tasklist | findstr "python"

终止进程: taskkill /f /t /im python.exe
/f 表示强制终止进程。
/t 表示结束此进程和其子进程。
/im 用来指定进程的影映像名称（有 .exe 后缀）
```

使用dd命令从文件中获取指定的块[startByte, endByte): --位置从0开始计
    dd if=inFileName of=outFileName ibs=1 obs=1 skip=startByte seek=0 count=endByte-startByte
    if: 输入文件名
    of: 输出文件名
    ibs: 一次读入的字节数(输入块大小)
    obs: 一次输出的字节数(输出块大小)
    skip: 从输入文件开头跳过的块数目
    seek: 从输出文件开头多少个块开始复制
    count: 拷贝的块数目
    
生成目录结构:
    windows:tree命令
        tree /f dirName > out.txt

bash shell:
    获取脚本所在目录:
        scriptDir=`dirname "${BASH_SOURCE-$0}"`   #写法参考shell字条串的替换
        or 
        scriptDir=$(cd "$(dirname "${BASH_SOURCE-$0}")"; pwd)
        
    shell索引数组:
        my_array=(1 2 A B) 
        
        # 获取所有元素.  {}不可省略
        echo ${my_array[@]}  #1 2 A B
        echo ${my_array[*]}  #1 2 A B
        
        # 单个元素
        echo ${my_array[0]}  #1
        
        # 得到长度
        echo ${#my_array[@]} #4
        echo ${#my_array[*]} #4
        
        #得到索引  -- 可用于遍历
        echo ${!my_array[@]} #0 1 2 3
    
    shell关联数组:
        declare -A kv_array   #声明关联数组
        kv_array=([key1]=value1 [key2]=value2 [3]=v3 [4]=v4) 
        # 获取所有元素值.  {}不可缺
        echo ${kv_array[@]}  #value1 value2 v3 v4
        echo ${kv_array[*]}  #value1 value2 v3 v4
        # 单个元素
        echo ${kv_array[key1]}  #value1
        # 得到长度
        echo ${#kv_array[@]} #4
        echo ${#kv_array[*]} #4
        #得到索引  -- 可用于遍历
        echo ${!kv_array[@]} #key1 key2 3 4
    
    Shell特殊变量: ($0, $#, $*, $@, $?, $$)
        $0: 当前脚本的文件名
        $n: 传递给脚本或函数的第n个参数. 如:第一个参数是$1,第二个参数是$2.
        $#: 传递给脚本或函数的参数个数.
        $*: 传递给脚本或函数的所有参数($*:以"$1" "$2" ...形式分多个输出; "$*":以"$1 $2 ..."形式作为一个整体输出).
        $@: 传递给脚本或函数的所有参数.($@/"$@":以"$1" "$2" ...形式分多个输出).
        $?: 上个命令的退出状态,或函数的返回值.
        $$: 当前Shell进程ID.对于 Shell 脚本,就是这些脚本所在的进程ID.
        
        eg: test.sh 执行 test.sh 1 2 3 
            #! /bin/bash
            echo '$*'=$*     # '1' '2' '3'
            echo '"$*"'="$*" # '1 2 3'

            for var in $*
            do
                echo "$var"  # 会循环进入多次
            done

            for var in "$*"
            do
                echo "$var" # 只会进入一次
            done

    
    双引号中可以取变量的值:
        LANG=en_US
        var="lang is ${LANG}"  --> lang is en_US
    单引号中是纯文本:
        LANG=en_US
        var="lang is ${LANG}"  --> lang is ${LANG}
        
    变量内容的删除&替换:
        删除:
            vartest="/usr/local/bin:/usr/bin:/home/gjy"
            ${varName#matchstr}: 从左向右进行最短匹配删除(删除左侧，保留右侧)
                例: echo ${vartest#/*bin:}  --从左向右进行最短匹配删除("/usr/local/bin:"被删除). 输出"/usr/bin:/home/gjy"
            ${varName##matchstr}: 从左向右进行最长匹配删除
                例: echo ${vartest##/*bin:}  --从左向右进行最长匹配删除("/usr/local/bin:/usr/bin:"被删除). 输出"/home/gjy"
            
            ${varName%matchstr}: 从右向左进行最短匹配删除(保留左侧，删除右侧)
                例: echo ${vartest%:*gjy}  --从右向左进行最短匹配删除(":/home/gjy"被删除). 输出"/usr/local/bin:/usr/bin"
            ${varName%%matchstr}: 从右向左进行最长匹配删除
                例: echo ${vartest%%:*bin*}  --从右向左进行最长匹配删除(":/usr/bin:/home/gjy"被删除). 输出"/usr/local/bin"
        替换:
            ${变量名/旧字符串/新字符串}: 替换第一个匹配旧字符串为新字符串
                例: echo ${vartest/bin/BIN}  --输出: "/usr/local/BIN:/usr/bin:/home/gjy"
            ${变量名//旧字符串/新字符串}: 替换全部匹配的旧字符串为新字符串
                例: echo ${vartest//bin/BIN} --输出: "/usr/local/BIN:/usr/BIN:/home/gjy"
            ${变量名/#oldstr/newstr}: 替换开头一个
                例: echo ${vartest/#bin/BIN} --输出: "/usr/local/BIN:/usr/bin:/home/gjy"
            ${变量名/%oldstr/newstr}: 替换结尾一个
                例: echo ${vartest/#bin/BIN} --输出: "/usr/local/bin:/usr/BIN:/home/gjy"
    
    
    
    if语句:
        ```
        if condition; then
            commands;
        elif condition; then
            commands;
        else
            　commands;
        fi
        ```
    case语句:
        ```
        case 变量 in
        "取值1"){
            ...
        };;
        ...
        *){
            ...
        };;
        esac
        ```
    []测试(test):
        Note: 1. 建议对其内r 变量,字符串使用双引号包围.
              换句话说,能做字符串比较的时候,不要用数值比较.
              2. 多使用括号显示指示优先级
              3. 测试通过返回结果为0,测试未通过返回结果非0.
                 eg: [ ];echo $?
        []完全等价于test;  双中括号[[]]基本等价于[],[[]]支持更多的条件表达式,[[]]内可使用逻辑运算符"&&","||","!"和"()"
        
        支持测试的范围包括:字符串比较,算术比较,文件存在性,属性,类型等判断.
            eg:判断文件是否为空,文件是否存在,是否是目录,变量是否大于5,
               字符串是否等于"longshuai",字符串是否为空等等.
        常用的测试:
            文件相关:
                -e file: 文件存在(exist)
                -f file: 文件存在且为普通文件(file)
                -d file: 文件存在且为目录(directory)
                -r file: 文件存在且当前用户可读
                -w file: 文件存在且当前用户可写
                -x file: 文件存在且当前用户可执行
                file1 -nt file2: (newer than)file1比file2新
                file1 -ef file2: (equal file)两个文件均指向同一个分区上的同一个inode
            数值:
                int1 -eq int2: 两数值相等(equal)
                int1 -gt int2: n1大于n2(greater than)
            字符串:
                -z string: (zero)字符串为空
                -n string: 字符串非空
                string1 = string2: string1和string2相同
                str1 != str2: str1不等于str2
            逻辑运算符:
                -a或&&: 逻辑与.  "-a"只能在test或[]中使用,&&只能在[[]]中使用
                -o或||: 逻辑或.  "-o"只能在test或[]中使用,||只能在[[]]中使用
                !: 取反
                    
                    
sed命令:
    选项与参数: /REGEXP/:表示匹配正则的那一行,正则表达式放在//之间
    
    sed [-nefr] 动作
        -n: (安静模式) 只在终端显示出被处理的行
        -f: fileName: 可以运行fileName内的sed动作
        -r: sed动作的支持是延伸型的正则表示语法
        -i: 直接修改读取的文件内容
        
        动作说明: [n1[,n2]] function
            n1,n2: 可选,一般代表[选择进行动作的行范围],如需要在10到20行之间进行动作:[10, 20]
                a: 新增, a的后面可以接字串,而这些字串会在新的一行出现(当前的下一行).
                c: 行取代(替换), c的后面可以接字串,这些字串可以取代[n1, n2]之间的行
                d: 删除
                i: 插入, i的后面可以接字串,而这些字串会在新的一行出现(当前的上一行).
                p: 打印, 即将某个选择的数据印出, 通过p会与参数sed -n一起运行
                s: 匹配取代, 可以直接进行取代的工作, 通常这个s的动作可以搭配正则. eg: "1,20s/old/new/g"
                
                eg:
                #删除文件fileName中包含字符串"deleteStr"的行,并输出到新的文件newFileName
                    sed /"deleteStr"/d fileName > newFileName
                    
                #删除[1,5]行
                    sed 1.5d fileName > newFileName
                    
                #删除[5,$] --删除第5到最后一行
                    sed 5,\$d fileName
                    
                #将文件fileName中每一行中的"oldStr"字符串替换成"newStr".如果没有g命令,只替换每一行中的第一个匹配字符串
                    sed s/"oldStr"/"newStr"/g fileName > newFileName
                    
                #将[4,6]行替换为"newStr"
                    sed 4,6c\"newStr" fileName > newFileName
                    
                #将文件fileName中匹配"oldStr"的行替换成"newLineStr",并输出到新的文件newFileName
                    sed /"oldStr"/c\"newLineStr" fileName > newFileName
                    
                #将文件fileName第[4,6]行中匹配的"oldStr"替换成"newStr"
                    sed 4,6s/"oldStr"/"newStr"/g fileName > newFileName
                
                #输出fileName文件中包含字符串"subStr"的行号
                    sed -n /"subStr"/=fileName > newFileName
                    
                #输出模式REGEXP1和REGEXP2之间所有的行,如果REGEXP1出现在REGEXP2之后的某一行,则打印的范围从REGEXP1所有行开始到下一个出现REGEXP2的行或文件的末尾(如果REGEXP2未出现)
                    sed -n "/REGEXP1/,/REGEXP2/p" filename > newFileName
                
                #在文件fileName中与"subStr"匹配的行的上一行插入"newLineStr"
                    sed /"subStr"/i\"newLineStr" fileName
                
                #在文件fileName中与"subStr"匹配的行的下一行追加"newLineStr"
                    sed /"subStr"/a\"newLineStr" fileName
                
                #获取文件行数
                    sed -n "$=" fileName
                    
                #在第5行下面插入一行
                sed -i 5a\"xxx" fileName
                
                #将第6行替换成PS1='\u@docker:\w\$ '
                # '\x5c' <==> '\'
                sed -i "6,6c\PS1=\'\x5cu@docker:\x5cw\x5c$ \'" .bashrc

                
批量重命名(修改)文件名,去除文件名中的特定字符串:
    批量去除文件名中包含的'XXXXX':
    ## sort -r: 保证了子文件(目录)排在父目录之前,确保了删除顺序正确
    find . -regex "\(.*\)XXXXX\(.*\)" | sort -r | sed s/"\(.*\)XXXXX\(.*\)"/"mv \"\0\" \"\1\2\""/g >./tmp.sh && bash -x ./tmp.sh && rm ./tmp.sh

    我从网上下载的文件中有很多文件名中包含了"[瑞客论坛 www.ruike1.com]"字符串,如下命令可批量去除文件名中包含的该字符串:
    ## sort -r: 保证了子文件(目录)排在父目录之前,确保了删除顺序正确
    find . -regex "\(.*\)[瑞客论坛 www.ruike1.com]\(.*\)" | sort -r | sed s/"\(.*\)[瑞客论坛 www.ruike1.com]\(.*\)"/"mv \"\0\" \"\1\2\""/g >./tmp.sh && bash -x ./tmp.sh && rm ./tmp.sh
    
    将分散在不同目录下的'特定模式目录'中的文件统一拷贝出来:
        如下结构： 现要将'各周下的课堂作业'中的文件全部'顶层的课堂作业'目录中,并按周分好。
        ├─第01周—XXXX
        │  ├─视频
        │  │      ...
        │  └─课堂作业
        │          ...
        ├─第02周—XXX
        │  ├─视频
        │  │      ...
        │  └─课堂作业
        │          ...
        ├─第03周—XXX
        │     ├─第一部分XXXX
        │           │      ...
        │           └─课堂作业
        │                  ...
        └─课堂作业
        shell如下:
        find . -regex "\(.+\)第[0-9][0-9]\(.*\)/课堂作业" | sort -r | sed s/"\(.*第\([0-9][0-9]\).*\)"/"cp -r \"\0\/\" \"\.\/课堂作业\/\2\/\""/g >./tmp.sh && bash -x ./tmp.sh && rm ./tmp.sh
    
awk:
    逐行扫描文件,寻找匹配特定模式的行,并在这些行上进行你想要的操作.
    如果没有指定处理动作,则把匹配的行显示到标准输出;如果没有指定特定的模式,则所有被操作所指定的行都被处理.
    两种语法形式:
        awk [options] "script" var=value file(s)
        awk [options] -f scriptfile var=value file(s)
        
        -F: 指定分隔符 如:awk -F, "{print $1}" fileName #以","对文件fileName的行进行拆分,并输出每行的第一个拆分结果($0表示整行)
        

SSH登陆时出现"Host key verification failed."错误的处理方法:
    ssh-keygen -R 目标IP


安装gdb:
    rpm -i gdb-7.5.1-0.7.29.x86_64.rpm
gdb定位ap_proc挂死:
    方式一: 使用gdb拉起进程
        查询进程ID: ps -ef | grep "ap_proc"  --可以得到进程ID和进程参数
        杀死进程: kill -9 pid    只有一个ap_proc进行时: kill -9 `ps -ef | grep "ap_proc" | grep -v grep | awk "{print $2}"`
        使用gdb拉起进程: gdb /path/ap_proc
        set args xxxx //参数与 ps -ef | grep "ap_proc"查询到的结果一致
        set heigth 0
        handle SIG35 SIG36 SIGUSR2 noprint nostop
        r //运行
        bt //显示堆栈信息
    
    方式二(推荐): 将gdb附到指定的进程(指定进程ID,gdb会自动attach上去,并调试它)
        gdb /path/ap_proc --pid=xxxdir /opt/src_code //[可选]设置要调试的源代码路径
        set args xxx  //设置进程的启动参数
        set heigth 0 //设置屏幕打印不受限制,不然gdb要一直按enter确认
        handle SIG35 SIG36 SIGUSR2 noprint nostop //忽略某些信号
        c  //继续
        bt //显示堆栈信息
        


        
  
### 打包并压缩文件:
```
tar -zcvf 压缩包名 要打包压缩的文件/目录
其中:
    z: 调用gzip压缩命令进行压缩
    c: 打包文件
    v: 显示运行过程
    f: 指定文件名
    p: 保留权限
    比如: 加入test目录下有三个文件分别是:aaa.txt bbb.txt ccc.txt,如果我们要打包test目录并指定压缩后的压缩包名称为test.tar.gz.
    可以使用命令:tar -zcvf test.tar.gz aaa.txt bbb.txt ccc.txt
                    或:tar -zcvf test.tar.gz /test/
                      
    其它压缩:
        zip filaname.zip file_dir_name
        tar -zcvf filename.tar file_dir_name
        gtar -zcvf filename.tar.gz file_dir_name
        gzip filename //将产生文件filename.zip
```
### 解压文件:
```
tar [-xvf] 压缩文件
其中:x:代表解压
示例:
1 将/test下的test.tar.gz解压到当前目录下可以使用命令:tar -xvf test.tar.gz
2 将/test下的test.tar.gz解压到根目录/usr下:tar -xvf xxx.tar.gz -C /usr(- C代表指定解压的位置)

其它解压:
    unzip filename.zip -d out_dir
    tar -C out_dir -xvf filaname.tar
    gtar zxvf filaname.tar.gz
    gzip -d filename.gz
```
 
开机自启动配置:
    假如我们装了一个zookeeper,我们每次开机到要求其自动启动该怎么办?:
        1.新建一个脚本zookeeper
        2.为新建的脚本zookeeper添加可执行权限,命令是:chmod +x zookeeper
        3.把zookeeper这个脚本添加到开机启动项里面,命令是: chkconfig --add zookeeper
        4.如果想看看是否添加成功,命令是:chkconfig --list

日期操作:
    查看日期:
        date +"%Y/%m/%d %H:%M%s"
    设置日期:
        date -s "01:01:01 2012-05-23"

scp从本地拷贝文件到远端主机:
    scp 本地文件 用户名@远程主机IP:路径
    eg: scp fileName.dat gjy@186.15.11.10:/home/gjy

    
查看文件(目录)大小:
    du -h -max-depth=1 dirName
    
查看文件信息: //可以用来查看.so文件是32位还是64位
    file fileName
    
smb服务:
    rcsmb restart; rcnmb rcsmb
    当通过smb登陆服务器时,出现"不允许一个用户使用一个以上用户名与一个服务器或共享资源的多重连接"错误时:
    --处理方法: 
        net use 查看网络资源连接
        net use */del /y 断开所有连接
        再次登陆即可
    
路由操作:
    查看路由信息:
        route
    添加路由:
        eg: route add -net 167.200.100.0 netmask 255.255.255.0 gw 186.15.11.254 //linux添加路由
            route add -net 167.200.100.0 netmask 255.255.255.0 gw 186.15.11.254 dev eth2 //指定网卡
            问题: 如果出现"siocaddrt: no such device"错误
                先执行route add 186.15.11.254 dev eth0
                再执行route add -net 167.200.100.0 netmask 255.255.255.0 gw 186.15.11.254
    删除路由:
        route del 167.200.100.0 netmask 255.255.255.0
    
    windows添加路由:
        route add 167.0.0.0 mask 255.0.0.0 167.200.100.115 //windows添加路由
        -p参数: 添加永久路由
        
    linux IPV6路由:
        网卡信息: /etc/sysconfig/network
        查看路由: ip -6 route show dev eth0 //查看指定网卡的路由
                  route -A inet6 | grep -w "eth0" //查看指定的路由
        添加路由: 
            通过指定网关:
                //route -A　inet6 add 目标IP/前缀长度 gw 网关
                ip -6 route add fd0e:0000:0000:0000:0167:0102:0005:0000/112 gw fe0e:0000:0000:0000:0135:0114:0107:0254 
            通过指定网卡:
                ip -6 route add <ipv6network>/<prefixlength> dev <device> metric 1
                route -A inet6 add <ipv6network>/<prefixlength> dev <device>
                eg: ip -6 route add fd0e:0000:0000:0000:0167:0102:0005:0000/112 dev eth0 metric 1
                    route -A inet6 add fd0e:0000:0000:0000:0167:0102:0005:0000/112 dev eth0
        删除路由:
            通过指定网关:
                ip -6 route del fd0e:0000:0000:0000:0167:0102:0005:0000/112 via 2001:0db8:0:f101::1
                route -A inet6 del fd0e:0000:0000:0000:0167:0102:0005:0000/112 gw 2001:0db8:0:f101::1
            通过描写网卡:
                ip -6 route del <ipv6network>/<prefixlength> dev <device>
                route -A inet6 del <ipv6network>/<prefixlength> dev <device>
                eg: ip -6 route del fd0e:0000:0000:0000:0167:0102:0005:0000/112 dev eth0
                    route -A inet6 del fd0e:0000:0000:0000:0167:0102:0005:0000/112 dev eth0


IP冲突检测:
    IP冲突可以用ARPING命令检测,看是否返回的MAC地址有多个:
        arping -I eth85 167.1.130.201 //检测167.1.130.201IP是否冲突
    
    
windows下使用cl进行编译:
    1. 在cmd中运行"C:\Program Files (x86)\Microsoft Visual Studio 12.0\VC\bin\vcvars32.bat", 会自动配置相关环境变量
    2. 使用cl命令进行编译
            /I 指定头文件包含目录
 
WSL:
    '\\wsl$\': 访问wsl目录
 
用户管理:
    创建用户: useradd UserName
    设置用户密码: passwd UserName
    ...


ssh:
    ssh执行远程命令:
        # 通过ssh执行远程命令要注意:当命令多于一个时最好用引号括起来,否则在有的系统中除了第一个命令,其它都是在本地执行的.
        ssh root@10.10.10.23 'cd /my_wks && find "./log" -maxdepth 1 -type f -name "*.log" | xargs grep "Warning"'

    默认登陆用户配置:
        使用"ssh hostIP"登陆远程主机时,默认使用的是与当前用户名同名的用户进行登陆
        本地和远程登陆用户名不一致时,可通过"~/.ssh/config "文件进行远程登录用户配置.
        ```
        Host hostname
        user username_test

        Host hostname2
        user username_dev
        ```
        假设当前处于hostname的username_test用户下, 此时"ssh hostname2"等价于"ssh username_dev@hostname2".
        最后修改的最重要的一点记住权限问题:config文件的权限同组用户严格为不能写权限.

          
进入内核模块目录:
    cd /lib/modules/`uname -r`/kernel
    或cd /lib/modules/$(uname -r)/kernel
    
列出相关的文件名: locate
    ls -l `locate fileName`   
    ls -l $(locate fileName)
    文件个数:  ls -l *amazon* | grep "^-" | wc -l
    
Notepad++提取以"|"分隔的第Num个字段:
    ^(?:[^|]*\|){Num-1}(Regular)\|(?:[^|]*\|){MaxNum-Num}[^\r\n]$
    如: 4|0|34|||20181010|12:30:32|789|
        1|4|4||5|20181010|12:30:33|562|96
        匹配第5个字段非空的行: ^(?:[^|]*\|){4}([0-9]+)\|(?:[^|]*\|){3}[^\r\n]$
        匹配第6个字段非空的行: ^(?:[^|]*\|){5}([0-9]+)\|(?:[^|]*\|){2}[^\r\n]$
        
加驼峰写法中加入"-"分隔:
    如: serviceDeliveryStartTimeStamp --> service-Delivery-Start-Time-Stamp
    使用正则,选中匹配大小写,查找(([a-z]))(A-Z)替换为"\2-\3"

从指定文件${strFileName}的第${iStartLine}行开始查找首次出现${strKey}的行号:
    getlinenum()
    {
        local -i iStartLine=$1
        local strKey="$2"
        local strFileName="$3"
        keylinenum=`awk "NR>="${iStartLine}"{gsub(/,/,""); printf("%02d %s\n", NR, $0)}" ${strFileName} | grep ${strKey} | grep -v "^#" | head -1 | awk "{print{$1}" | sed "s/^[[:space:]]//g" | tr -d "\r"`
        if [-z "${keylinenum}"]; then
            echo ""
            Log "ERR" call getlinenum failure
            return 0
        fi
        echo "${keylinenum}"
        return 0
    }
    
    注:
        NR: 表示awk开始执行程序后所读取的数据行号
        
umask设置权限掩码:
    umask用来设置权限掩码．当新文件被创建时,其最初的权限由文件创建掩码决定．
    umask原来为022时,新创建文件权限默认为644,新创建目录权限默认为755.
    umask设置为027后,新创建文件权限默认为640,新创建目录权限默认为750.
    
    
    
    
    
windows命令:
    cmd历史命令快捷键: F7
```
    windows下类似grep的命令: findstr -- 在文件中寻找字符串
        findstr [/B] [/E] [/L] [/R] [/S] [/I] [/X] [/V] [/N] [/M] [/O] [/P] [/F:file]
                [/C:string] [/G:file] [/D:dir list] [/A:color attributes] [/OFF[LINE]]
                strings [[drive:][path]filename[ ...]]
            /B         在一行的开始配对模式.
            /E         在一行的结尾配对模式.
            /L         按字使用搜索字符串.
            /R         将搜索字符串作为正则表达式使用.
            /S         在当前目录和所有子目录中搜索匹配文件.
            /I         指定搜索不分大小写.
            /X         打印完全匹配的行.
            /V         只打印不包含匹配的行.
            /N         在匹配的每行前打印行号.
            /M         如果文件含有匹配项,只打印其文件名.
            /O         在每个匹配行前打印字符偏移量.
            /P         忽略有不可打印字符的文件.  
            /OFF[LINE] 不跳过带有脱机属性集的文件.
            /A:attr    指定有十六进位数字的颜色属性.请见 "color /?"
            /F:file    从指定文件读文件列表 (/ 代表控制台).
            /C:string  使用指定字符串作为文字搜索字符串.
            /G:file    从指定的文件获得搜索字符串. (/ 代表控制台).
            /D:dir     查找以分号为分隔符的目录列表
            strings    要查找的文字.
            [drive:][path]filename 指定要查找的文件.
            /?         在命令提示符显示帮助.
        注意: 除非参数有/C前缀, 否则空格用于隔开要搜索的多个字符串.
        例如: cmd: FINDSTR "findsubstr1 findsubstr2" filename  --在文件filename中寻找 "findsubstr1" 或 "findsubstr2".
              cmd: FINDSTR /C:"findsubstr1 findsubstr2" filename --文件filename寻找"findsubstr1 findsubstr2".

        正则表达式的快速参考:
          .        通配符: 任何字符
          *        重复: 以前字符或类出现零或零以上次数
          ^        行位置: 行的开始
          $        行位置: 行的终点
          [class]  字符类: 任何在字符集中的字符
          [^class] 补字符类: 任何不在字符集中的字符
          [x-y]    范围: 在指定范围内的任何字符
          /x       Escape: 元字符 x 的文字用法
          /<xyz    字位置: 字的开始
          xyz/>    字位置: 字的结束
        例: 查看conda中是否安装了pytorch包
            conda list | findstr /I "pytorch"
        例:
            在当前目录及所有子目录下的所有文件中查找"backup"这个字符串,*.*表示所有类型的文件.
            findstr /s /i "backup" *.* 
        使用正则匹配:
            conda list | findstr /R ".*(tensorflow|pytorch).*"
```

bat:
```
    ::延时5秒
    choice /t 5 /d y /n >null
    
    字符串截取&替换:
        截取方式:
            %strVarName:~startPos,Len%  --最后一个字符位置为-1,倒数第二个字符位置为-2
            eg: 
                set str_test="1234567890.asn"
                echo 第0个字符为: %str_test:0,1%          --1
                echo 第[0,5)个字符为: %str_test:-4,5%     --12345
                echo [倒数第4,倒数第3]个字符为: %str_test:-4,2%     --.a
                echo [倒数第4,倒数第1]个字符为: %str_test:-4,4%     --.asn
                echo [倒数第4,倒数第1]个字符为: %str_test:-4%       --.asn
        替换方式:
            %strVarName:oldStr=newStr% --将字符串变量%strVarName%中的所有oldStr替换为newStr．
            eg:
                set str_test="1234567890.asn"
                echo %str_test:.asn=.txt%  --1234567890.txt
                set str_test=1234567890.asn.asn
                echo %str_test:.asn=.txt%  --1234567890.txt.txt

    进入批处理所在目录: cd %~dp0 或  cd /D %~dp0 (/D表示盘符D)
        %~dp0表示当前批处理文件所在目录,以'/'结尾的.
```

C运行符优先级:
```
    sizeof("haha") == 5
    a % b != 0 <==> a & (b != 0)
    *pData->value <==> *(pData->value)
```
    
    
excel用法:
```
    通过两个页签中的行建立关联:
    用小表过滤大表:
        VLOOKUP(lookup_value, table_array, col_index_num, range_lookup)
        参数              简单说明
        lookup_value        要查找的值(数值,引用或文本)
        table_array         要查找的区域(数据表区域--建议使用绝对引用"$A$2:$F$12", 相对引用"A2:F12")
        col_index_num       返回数据在查找区域的第几列
        range_lookup        模糊匹配(TRUE)/精确匹配(FALSE或不填)
        
        # eg: 我要将页签1的B1单元格使用页签2的E列中满足条件的行来填充(要求:从页签2中的D列查找匹配页签1中的A1的单元格)
        在页签1的B1单元格中输入"VLOOKUP(A1, 页签2!$D$1:$E$10, 2, 0)" --因为第三个参数是数据在查找区域的第几列数,为了使用页签2的E列,所以第二个参数需要将E列包含进来
        
    超链接:
        在单元格输入 =HYPERLINK(XXX, YYY)
```