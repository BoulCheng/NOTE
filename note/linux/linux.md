

- more 命令类似 cat ，不过会以一页一页的形式显示, 按空白键（space）就往下一页显示，按 b 键就会往回（back）一页显示

- tar
    - tar -zxvf ***.tar.gz -C directory
    - tar -zcvf new.tar.gz fileName
    
- ps(process之意)命令用于报告当前系统的进程状态
    - -A：显示所有程序
    - -e：此选项的效果和指定"A"选项相同。
    - -f：显示UID,PPIP,C与STIME栏位。
    - ps -ef|grep 

- grep 分析一行的信息，若当中有我们所需要的信息，就将该行显示出来，该命令通常与管道命令一起使用，用于对一些命令的输出进行筛选加工等等
- 管道“|”可将命令的结果输出给另一个命令作为输入之用

- &&               命令1&&命令2            逻辑与：当命令1正确执行后，命令2才会正确执行，否则命令2不会执行
- 重导向就是使命令改变它所认定的标准输出。“>”可将结果输出到文件中，该文件原有内容会被删除，“>>”则将结果附加到文件中，原文件内容不会被删除。“<”可以改变标准输入
- top
    - 性能分析工具，能够实时显示系统中各个进程的资源占用状况
    - 各列含义
    
    
- Linux Load Averages 平均负载

    