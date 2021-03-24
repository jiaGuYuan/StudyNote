#!/bin/bash
# 查找class类所在jar包

if [ $# -lt 3 ]; then
    echo "使用方法: findJarClass.sh path jarFileRegx searchClassName"
    echo "例如要从当前目录的满足'*yarn*.jar'条件的jar包中查找ExecutorLauncher类: findJarClass.sh ./ \"*yarn*.jar\" \"ExecutorLauncher\""
    exit 0
fi

searchpath=$1
jarFileRegx=$2
searchClassName=$3
#echo "$searchpath, $jarFileRegx, $searchClassName"
#echo "find ${searchpath} -type f -name \"${jarFileRegx}\""

#遍历find查找到的文件
for jarfile in `find ${searchpath} -type f -name "${jarFileRegx}"`; do
    jar -tf "${jarfile}" | grep  -i "${searchClassName}" 1>/dev/null 2>&1
    if [ $? -eq 0 ]; then #符合查找要求
        echo "${jarfile}" #输出所在的jar包
        jar -tf "${jarfile}" | grep  -i ${searchClassName} | xargs -I {} echo "    {}" #输出匹配的类名
        echo -e "\n"
    fi
done

