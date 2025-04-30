## 使用远程分支覆盖本地
```
# 使用远程develop分支代码覆盖自己的本地开发分支gjy_dev
0. git branch gjy_dev
1. git fetch --all # 获取分支信息
2. git reset --hard origin/develop
3. git pull origin develop
```

## 拉取远程分支代码到本地
```
# 拉取远程分支到本地 (关联)
git branch -a # 查看分支
git checkout -b 本地分支名 origin/远程分支名


# 使用当前分支创建一个新的分支
git checkout -b debug_branch
git push --set-upstream origin debug_branch
```

## 开发流程(推荐)
```
使用rebase使用主分支提交记录保持线性
在个人分支完成开发后合并到master

在本地开发分支测试完成后，提交到远程分支进行正式测试
# 创建并切换到本地开发分支debug
git checkout -b debug

# 本地开发并测试后提交修改
git commit -m 'xxxx'

# 如果此时远程main有变化,且希望提测时包含该变化
git pull --rebase origin main

# 将debug推送到远程origin/debug,以便提测
git push --set-upstream origin debug   # push到远程分支提测
提测通过后, 将debug合入master并保持提交记录线性
# 获取最新的远程main修改并保持提交记录线性
git pull --rebase origin main

# === 如果需要将debug推送到远程debug ===
git push  # 经常会因为远程无法进行Fast-forward而导致push被拒绝
如果只有自己使用debug分支, 可强制推送/或重建远程分支 !!!
    *. 强制推送: git push -f
    *. 先删除远程debug分支再重新关联
        git push --delete origin debug  # 删除远程分支debug
        git push --set-upstream origin debug #  关联远程分支并推送
        
**合并debug分支修改到本地main，并推送到远程main**
git checkout main
git merge debug  # 合并本地debug分支到本地master, 此时应是Fast-forward.
git push  # 如果此时提示无法Fast-forward，可先git pull --rebase
删除远程debug分支
git push --delete origin debug
 
删除本地&远程分支:
    删除本地分支: git branch -d localBranchName
    删除远程分支: git push origin --delete remoteBranchName
    分支重命名: git branch -m old-branch new-branch 
    

```   

## 合并分支
```
# 示例: 将远程develop分支合并到本地test分支
无冲突时:
    1. git checkout test
    2. git merge origin/develop # 无冲突时'Fast-forward'
    3. git commit -m "merge develop->test"
    4. git push
有冲突时:
    1. git checkout test
    2. git merge origin/develop # 有冲突时会提示报错
    3. git status
        Unmerged paths: # 有冲突的文件,未合并
            (use "git add/rm <file>...") # 使用'git add/rm <file>' 标记已解决冲突/删除的文件
                both modified: 有冲突的文件
        Chanages not staged for commit: # 未提交的更改
            (use "git add  <file>..." to update what will be committed) # 使用'git add <file>'提交更改
            (use "git checkout -- <file>..." to discard changes in working directory) # 他用"git checkout -- <file>"丢弃工作目录中的更改
                modified: 修改但还未提交的文件
       
        Untracked files: 未跟踪的文件
            (use "git add  <file>..." to include in what will be committed) # 使用'git add <file>'将文件添加进仓库
    # 手动解决冲突后,依次对每个解决冲突的文件执行add命令来标记已解决冲突
    3. git add <file> # 注意: 不要使用'git add .'标记已解决冲突,这样会引入无关文件进来
    4. git commit -m "merge develop->test"
    5. git push
```

## 暂存stash
```
git stash save "暂存当前分支,先去xxx分支完成xxxx"
git stash pop [stash@N] # 将指定的堆栈内容(默认最新)弹出到当前分支(会将弹出的标记从堆栈中删除)
git stash apply [stash@N] # 将指定的堆栈内容(默认最新)应用到当前分支(不会将弹出的标记从堆栈中删除)
git stash list
git stash show
git stash clear
git stash drop 
git stash branch

## 建议
1. 最好只在一个分支上有一个stash, 每次都使用'git stash pop'
2. 当需要在多个分支上都stash时, 最好每个分支只保留一个stash.
   流程: `git stash pop stash@{X}` ==> 修改 ==> `git stash`
   
总结: 
1. git stash 之后版本库状态会恢复到最近一次提交完的状态; 新的修改会被保存起来, 将可以切换到其它分支
2. git stash pop 之前的修改,在pop之后会保留, 合并时有冲突会提示.
3. git stash 默认只会保存已经加入到版本管理中的文件.
```


## 压缩多个commit
![](vx_images/582962320220961.png)

1. 执行`git log`找到要压缩的最早commit的上一条COMMIT_HASH

2. 执行`git rebase --interactive COMMIT_HASH`进行压缩操作.
命令说明: 该命令会压缩COMMIT_HASH之后(不包含这个COMMIT_HASH)的所有commit; 即压缩范围为(COMMIT_HASH, 当前最新]
eg: git rebase --interactive 6394dc
3. 选择与压缩
执行上述命令后在编辑窗口中对提交列表进行合并操作, 展示的提交列表中最新提交在最下面.
squash [s]: 保留提交的修改, 压缩commit log.
**f: 保留提交的修改, 删除commit log.**
drop [d]: 删除提交的修改(相当有没有进行此次提交).
 
![](vx_images/510862920239387.png)

## 修改commit
```
修改最后一次commit
git commit --amend
```


## 代码导出操作
### 导出修改但未提交的代码文件
1. 查看当前修改但还没提交(commit)的文件
```
$ git diff --name-only
# 输出如下
extra/tmp.txt
t/unit/test_canvas.py
```

2. 导出当前修改但还没提交(commit)的代码
```
# cp --parents参数会在目标目录创建对应的子目录
git diff --name-only | xargs -I {} cp --parents {} D:/temp/code/
```

3. 查看两次commit之间修改的文件
 
```
$ git log
commit 1cd7be70d15d3b82c09525a7a58eb074ec6dc225 (HEAD -> master)
Date:   Wed May 10 21:26:43 2023 +0800
my-test02 添加文件: data/data01.txt, 修改文件: file01.txt

commit 5fbc93e66ab62ccfcb6f6428bf181189e15975ea
Date:   Wed May 10 21:25:28 2023 +0800
my-test01 添加文件: file01.txt

commit 691d305398c59966d7cad428e2afcceb67a52aab (origin/master, origin/HEAD)
Date:   Thu May 26 12:07:06 2022 +0300
base-test 添加文件: file00.txt
   
# 获取(691d3053, 1cd7be70]之间的commit修改的文件名(半开半闭, 不包括COMMITXX修改的文件)
$ git diff --name-only 691d3053 1cd7be70
# 输出如下
file01.txt
data/data01.txt
```

git-diff 参数说明:
--diff-filter=[(A|C|D|M|R|T|U|X|B)... [*]]
大写字母表示正向选择: 仅选择 已添加(A)、已复制(C)、已删除(D)、已修改(M)、已重命名(R)、类型(即常规文件、符号链接、子模块等)已更改(T)、未合并(U)、是未知(X)，或者他们的配对已损坏(B). 
可以使用过滤字符的任意组合（包括无）。*将(All-or-none)添加到组合中时，如果比较中有任何文件与其他条件匹配，则选择所有路径；如果没有符合其他条件的文件，则不会选择任何内容。
此外，**这些大写字母可以小写以排除**。例如 --diff-filter=ad排除添加和删除的路径.


## 归档指定分支或commit的代码
git-archive命令语法
```
git archive [--format=<fmt>] [--list] [--prefix=<prefix>/] [<extra>]
	      [-o <file> | --output=<file>] [--worktree-attributes]
	      [--remote=<repo> [--exec=<git-upload-archive>]] <tree-ish>
	      [<path>…​]
```
	      
### 示例
1. 归档当前分支指定commit的完整代码

```shell
# --format指定打包格式,不指定时根据'--output'指定的文件名推断
git archive --format tar.gz --output "./output.tar.gz" HEAD

# 通过指定path可以只归档指定的部分文件(示例只归档两个文件)
git archive --format tar.gz --output "./output.tar.gz" HEAD file01.txt data/data01.txt
```

2. **归档 COMMITXX~COMMITYY之间所有修改的前后代码**

```
# 获取(COMMITXX, COMMITYY]之间修改的文件(半开半闭, 不包括COMMITXX修改的文件)
git diff --name-only COMMITXX COMMITYY

# 归档COMMITXX的相关文件(需要排除掉COMMITXX~COMMITYY之间添加的文件--否则会报错' fatal：pathspec 'xx' did not match any files')
git archive --output "./COMMITXX.zip" COMMITXX  $(git diff --diff-filter=a --name-only COMMITXX COMMITYY)

# 归档COMMITYY的相关文件(需要排除掉COMMITXX~COMMITYY之间删除的文件)
git archive --output "./COMMITYY.zip" COMMITYY  $(git diff --diff-filter=d --name-only COMMITXX COMMITYY)

```

3. **导出某次commit修改文件的前后代码**
```
git archive --output "./before.zip" COMMIT_ID^1  $(git diff --diff-filter=a --name-only COMMIT_ID^1 COMMIT_ID)
git archive --output "./after.zip" COMMIT_ID  $(git diff --diff-filter=d --name-only COMMIT_ID^1 COMMIT_ID)
```

## clone&回退 操作
```
克隆一个远程库到本地:
    git clone git@github.com:jiaGuYuan/learngit.git

创建git仓库: 
    在指定的目录下执行 git init

将文件加入版本库: 
    git add 文件名(可以是目录,或多个文件名)

提交到仓库: 
    git commit -m "关于提交的说明"

回退到上一个版本: 
    git reset --hard HEAD^  (HEAD^^上上版本,HEAD~n前n个版本)
```
     
## git使用本地分支(tmp_dev)强制覆盖(develop)  -- 不推荐
```
# 注: 使用该方式覆盖develop后，原develop分支的合入记录将消失(只能看到tmp_dev分支的log)
1. git checkout develop  # 切换到develop分支
2. git reset --hard tmp_dev(本地分支名)  # 将本地的develop分支重置成tmp_dev分支
3. git push origin develop --force  # 将develop推送到远程仓库
```
 
## 撤销修改
```
1. 你对工作区的修改已经提交到了版本库(执行了git commit),这时需要进行版本回退操作.
2. 你对工作区的文件的修改已经添加到版本库的暂存区时(执行了git add),但还没有提交到版本库时(没有git commit);
    git reset HEAD file可以把暂存区的修改撤销掉(unstage),重新放回工作区.
    git checkout -- file可以丢弃工作区的修改,使工作区(或工作区中的某些文件)恢复到上一次git add或git commit时的状态.
3. 对于修改后还没有被放到暂存区(没有执行 git add)的文件,只要执行git checkout -- file就可以了.
```


## 分支操作
```
# Git鼓励大量使用分支: 
查看分支: git branch
创建分支: git branch <name>
切换分支: git checkout <name>
创建+切换分支: git checkout -b <name>
合并某分支到当前分支: git merge <name>
删除分支: git branch -d <name>
```

## 清理历史记录(只保留最后一个版本):
```
git checkout --orphan latest_branch # 将当前分支切换到 latest_branch
git add -A                          # 把所有文件添加到当前分支
git commit -am "commit message"     # 设置commit注释
git branch -D master                # 删除主分支master(此时只剩下latest_branch)
git branch -m master                # 将latest_branch重命名为master
git push -f origin master           # 强制push,用当前内容冲刷掉git的内容(此处无法反悔)
git branch --set-upstream-to origin/master master # 重新初始化分支结构
```


## 查看信息:
```
git:
    直接输入git将提示git的使用信息.

git status:
    查看仓库的状态,它会告诉我们什么文件被修改了,什么文件修改了但是还没有提交,什么文件是刚添加进来的等等.

git diff:
    查看difference,它能告诉我们我们在某个文件上所做的修改.
    git diff readme.txt ---将显示我们在readme.txt上所做的修改

git log:
    查看历史记录,它能告诉我们从最近到最远的提交日志.
    --pretty=oneline参数: 简化显示的内容.
    git log命令显示的内容有commit id(版本号),作者,日期,及该版本的修改.
    用带参数的git log也可以看到分支的合并情况:  git log --graph --pretty=oneline --abbrev-commit

git reflog:
    git reflog命令能够查看我们执行的每一次命令.

git branch:
    查看分支信息

git stash list:
    查看工作现场的信息

git remote:
    查看远程库的信息
    -v显示更详细的信息

git tag:
    查看所有标签

git show <tagname>:
    查看标签信息

信息: 
Fast-forward信息,Git告诉我们,这次合并是"快进模式",也就是直接把一个分支指向另一个分支的当前提交,所以合并速度非常快.
```

## 稀疏检出
```
echo "clone start time: `date +'%Y-%m-%d %H:%M:%S'`"
mkdir ./code  && cd ./code
git config --global http.proxy http代理IP
git config --global https.proxy https代理IP
git config --add core.compression -1

# 稀疏检出, 排除不需要的目录
git clone -b 分支名 --filter=blob:none --no-checkout GIT仓库地址
git config core.sparsecheckout true

cat > ./git/info/sparse-checkout <<-EOF
/*              # 要保留的文件
!/test_case/    # 不要的文件
/test_case/data/data_demo/  # 要保留的文件
EOF

git checkout  # 检出
echo "clone start time: `date +'%Y-%m-%d %H:%M:%S'`"
```


## .gitignore文件规则
### 语法规范（熟悉正则很容易理解）
```
    空行或是以#开头的行即注释行将被忽略；
    以斜杠 '/' 结尾表示目录；
    以星号 '*' 通配多个字符；
    以问号 '?' 通配单个字符
    以方括号 '[]' 包含单个字符的匹配列表；
    以叹号 '!' 表示不忽略(跟踪)匹配到的文件或目录；
    可以在前面添加斜杠 '/' 来避免递归,下面的例子中可以很明白的看出来与下一条的区别。
```

### 配置文件示例
```
    # 忽略后缀为.a的文件
    *.a
    # 但不忽略 lib.a, 尽管已经在前面忽略了.a文件
    !lib.a
    
    # 仅在当前目录下忽略 TODO.txt 文件,但不忽略子目录下的 subdir/TODO.txt
    /TODO.txt

    # 忽略 build/ 文件夹下的所有文件
    build/

    # 忽略 doc/notes.txt, 不忽略 doc/server/arch.txt
    doc/*.txt

    # 忽略在doc/下的所有.pdf 文件 
    doc/**/*.pdf

配置文件模板
Github 上为开发者提供了各种环境以及各种编程语言的 gitignore 文件配置模板:
    https://github.com/github/gitignore
python模板:
    url:https://github.com/github/gitignore/blob/master/Python.gitignore
```
    
    
# 问题:
## 报错: error SSL_connect: SSL_ERROR_SYSCALL in connection to github.com:443
出现这种情况多半是因为设置了系统代理, 取消全局代理即可
```
git config --global --unset http.proxy
git config --global --unset https.proxy
```

