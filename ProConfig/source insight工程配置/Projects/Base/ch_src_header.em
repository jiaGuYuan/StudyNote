

// https://blog.csdn.net/sinat_32596537/article/details/53544723
// 切换源文件 与 头文件 ==> 可为"Macro: SwitchCppAndHpp"设置快捷键(如：Ctrl+B)
macro SwitchCppAndHpp()
{
hwnd = GetCurrentWnd()
hCurOpenBuf = GetCurrentBuf()
if (hCurOpenBuf == 0)// empty buffer
stop 


// 文件类型临时缓冲区
strFileExt = NewBuf("strFileExtBuf")
ClearBuf(strFileExt)

// 头文件
index_hpp_begin = 0 // 头文件开始索引
AppendBufLine(strFileExt, ".h")
AppendBufLine(strFileExt, ".hpp")
AppendBufLine(strFileExt, ".hxx")


index_hpp_end = GetBufLineCount(strFileExt) // 头文件结束索引

// 源文件
index_cpp_begin = index_hpp_end // 源文件开始索引
AppendBufLine(strFileExt, ".c")
AppendBufLine(strFileExt, ".cpp")
AppendBufLine(strFileExt, ".cc")
AppendBufLine(strFileExt, ".cx")
AppendBufLine(strFileExt, ".cxx")
index_cpp_end = GetBufLineCount(strFileExt) // 源文件结束索引




curOpenFileName = GetBufName(hCurOpenBuf)
curOpenFileName = ParseFilenameWithExt(curOpenFileName) // 获得不包括路径的文件名
curOpenFileNameWithoutExt = ParseFilenameWithoutExt(curOpenFileName)
curOpenFileNameLen = strlen(curOpenFileName)
//Msg(cat("current opened no ext filename:", curOpenFileNameWithoutExt))


isCppFile = 0 // 0：未知 1：头文件 2：源文件，默认未知扩展名
curOpenFileExt = "" // 当前打开文件的扩展名
index = index_hpp_begin 
// 遍历文件，判断文件类型
while(index < index_cpp_end) 
{
curExt = GetBufLine(strFileExt, index) 


if(isFileType(curOpenFileName, curExt) == True)// 匹配成功
{
if (index < index_hpp_end)
isCppFile = 1 // 当前打开文件是头文件
else
isCppFile = 2 // 源文件
break 
}
index = index + 1 
}// while(index < index_cpp_end)

// 调试
// AppendBufLine(debugBuf, isCppFile)


index_replace_begin = index_hpp_begin 
index_replace_end = index_hpp_end 

if (isCppFile == 1) // 当前打开文件是头文件 
{
index_replace_begin = index_cpp_begin 
index_replace_end = index_cpp_end 
}
else if(isCppFile == 2) // 当前打开文件是源文件
{
index_replace_begin = index_hpp_begin 
index_replace_end = index_hpp_end 
}
else // 未知类型
{
index_replace_begin = 9999 
index_replace_end = index_replace_begin // 下面循环不会执行
}

index = index_replace_begin 
while(index < index_replace_end)
{
destExt = GetBufLine(strFileExt, index) 
// 尝试当前目标扩展名是否能够打开
destFilename = AddFilenameExt(curOpenFileNameWithoutExt, destExt)


//Msg(destFilename)


hCurOpenBuf = OpenBuf(destFilename)
if(hCurOpenBuf != hNil)
{
SetCurrentBuf(hCurOpenBuf)
break 
}
else
{
//Msg("打开失败")
}

index = index + 1 
}
CloseBuf(strFileExt) // 关闭缓冲区
}




macro switch_cpp_hpp()
{
hwnd = GetCurrentWnd()
hCurOpenBuf = GetCurrentBuf()
if (hCurOpenBuf == hNil)// empty buffer
stop 


curOpenFileName = GetBufName(hCurOpenBuf)
curOpenFileNameLen = strlen(curOpenFileName)
// Msg(cat("current opened filename:", curOpenFileName))

// 文件类型临时缓冲区
strFileExt = NewBuf("strFileExtBuf")
ClearBuf(strFileExt)

// 头文件
index_hpp_begin = 0 // 头文件开始索引
AppendBufLine(strFileExt, ".h")
AppendBufLine(strFileExt, ".hpp")
index_hpp_end = GetBufLineCount(strFileExt) // 头文件结束索引

// 源文件
index_cpp_begin = index_hpp_end // 源文件开始索引
AppendBufLine(strFileExt, ".c")
AppendBufLine(strFileExt, ".cpp")
AppendBufLine(strFileExt, ".cc")
AppendBufLine(strFileExt, ".cx")
AppendBufLine(strFileExt, ".cxx")
index_cpp_end = GetBufLineCount(strFileExt) // 源文件结束索引


isCppFile = 0 // 0：未知 1：头文件 2：源文件，默认未知扩展名
curOpenFileExt = "" // 当前打开文件的扩展名
index = index_hpp_begin 
// 遍历头文件，判断是否当前打开文件是头文件类型
while(index < index_cpp_end) 
{
curExt = GetBufLine(strFileExt, index) 
curExtLen = strlen(curExt) 
curOpenFileExt = strmid(curOpenFileName, curOpenFileNameLen-curExtLen, curOpenFileNameLen) // 当前打开文件的扩展名

// 调试
// AppendBufLine(debugBuf, curExt)
// AppendBufLine(debugBuf, curOpenFileExt)



if(curOpenFileExt == curExt) // 匹配成功
{
if (index < index_hpp_end)
isCppFile = 1 // 当前打开文件是头文件
else
isCppFile = 2 // 源文件
break 
}
index = index + 1 
}// while(index < index_cpp_end)

// 调试
// AppendBufLine(debugBuf, isCppFile)


index_replace_begin = index_hpp_begin 
index_replace_end = index_hpp_end 

if (isCppFile == 1) // 当前打开文件是头文件 
{
index_replace_begin = index_cpp_begin 
index_replace_end = index_cpp_end 
}
else if(isCppFile == 2) // 当前打开文件是源文件
{
index_replace_begin = index_hpp_begin 
index_replace_end = index_hpp_end 

// 调试
// AppendBufLine(debugBuf, "cpp")
}
else // 未知类型
{
//CloseBuf(strFileExt) // 关闭缓冲区


//stop 

index_replace_begin = 9999 
index_replace_end = index_replace_begin // 下面循环不会执行
}

index = index_replace_begin 
while(index < index_replace_end)
{
destExt = GetBufLine(strFileExt, index) 
destFileName = strmid(curOpenFileName, 0, curOpenFileNameLen-strlen(curOpenFileExt)) // 不包括扩展名，绝对路径

// 尝试当前目标扩展名是否能够打开
destFilePath = cat(destFileName, destExt) // 文件名（包括扩展名）

// 调试
// AppendBufLine(debugBuf, destFilePath)


hCurOpenBuf = OpenBuf(destFilePath)
if(hCurOpenBuf != 0)
{
SetCurrentBuf(hCurOpenBuf)
break 
}

// 尝试进行目录替换，看能否打开文件（如何设计：支持多个目录）
// ...



index = index + 1 
}
CloseBuf(strFileExt) // 关闭缓冲区
// 调试
// AppendBufLine(debugBuf, "finished")

}


macro ParseFilenameWithExt(longFilename)
{
shortFilename = longFilename
len = strlen(longFilename)-1
if(len > 0)
{
while(True)
{
if(strmid(longFilename, len, len+1) == "\\")
break


len = len - 1
if(len <= 0)
break 
}
}
shortFilename = strmid(longFilename, len+1, strlen(longFilename))

return shortFilename
}
macro ParseFilenameWithoutExt(longFilename)
{
shortFilename = longFilename
len = strlen(longFilename)
dotPos = len
if(len > 0)
{
while(True)
{
len = len - 1
if(strmid(longFilename, len, len+1) == ".")
{
dotPos = len
break
}
if(len <= 0)
break 
}
}
shortFilename = strmid(longFilename, 0, dotPos)

return shortFilename
}
macro AddFilenameExt(filename, ext)
{
return cat(filename, ext)
}


macro isFileType(shortFilename, ext)
{
extLen = strlen(ext)
lastExtFilename = strmid(shortFilename, strlen(shortFilename)-extLen, strlen(shortFilename))
if(toupper(lastExtFilename) == toupper(ext))
return True


return False
}