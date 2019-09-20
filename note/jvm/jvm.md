
- pro
```
java -Xms1024m -Xmx4096m -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Duser.timezone=UTC -Djava.security.egd=file:/dev/./urandom -jar /application/fota-websocket.jar

```
- java
```


java -Xms1024m -Xmx4096m -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/Users/zlb/Downloads/mat.app/Contents/MacOS/dump -Duser.timezone=UTC -Djava.security.egd=file:/dev/./urandom -jar /application/fota-websocket.jar


daily@daily-jar-50-208:/opt/deploy/websocket$ nohup java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/deploy/websocket/logs -jar fota-websocket.jar >/opt/deploy/websocket/logs/a.out 2>&1 &
///

java.lang.OutOfMemoryError: GC overhead limit exceeded
Dumping heap to /opt/deploy/websocket/logs/java_pid17729.hprof ...
Heap dump file created [4046112260 bytes in 34.817 secs]


```
- jps
```
aily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$ jps -l
22065 fota-option-boss.jar
19810 fota-risk.jar
4882 fota-activity.jar
26558 fota-match.jar
13502 sun.tools.jps.Jps
14862 fota-websocket.jar
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$ jps -lv
22065 fota-option-boss.jar
19810 fota-risk.jar
4882 fota-activity.jar
16403 sun.tools.jps.Jps -Dapplication.home=/usr/lib/jvm/java-8-openjdk-amd64 -Xms8m
26558 fota-match.jar
14862 fota-websocket.jar
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$ ps -ef|grep java
daily     4882     1  0 Jan16 ?        15:32:10 java -jar fota-activity.jar
daily    14862     1  5 Apr29 ?        01:30:45 java -jar fota-websocket.jar
daily    19810     1  6 Apr24 ?        09:41:52 java -jar fota-risk.jar
daily    22065     1  0 Apr26 ?        00:35:13 java -jar fota-option-boss.jar
daily    24708  1776  0 07:58 pts/0    00:00:00 grep --color=auto java
daily    26558     1 11 Apr12 ?        2-04:20:08 java -jar fota-match.jar
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
```

- jmap (The release of JDK 8 introduced Java Mission Control, Java Flight Recorder, and jcmd utility for diagnosing problems with JVM and Java applications. It is suggested to use the latest utility, jcmd instead of the previous jmap utility for enhanced diagnostics and reduced performance overhead.)
- 
- The jmap command prints shared object memory maps or heap memory details of a specified process, core file, or remote debug server. If the specified process is running on a 64-bit Java Virtual Machine (JVM), then you might need to specify the -J-d64 option, for example: jmap -J-d64 -heap pid.
- jmap [ options ] pid
- pid
    - The process ID for which the memory map is to be printed. The process must be a Java process. To get a list of Java processes running on a machine, use the jps command.

```
daily@daily-jar-50-208:~$ jmap -heap 14862
daily@daily-jar-50-208:~$ jmap -J-d64 -heap 14862
daily@daily-jar-50-208:/opt/deploy/websocket$ jmap -dump:format=b,file=/opt/deploy/websocket/dump.hprof 14862
```

```
daily@daily-jar-50-208:~$ java -v
Unrecognized option: -v
Error: Could not create the Java Virtual Machine.
Error: A fatal exception has occurred. Program will exit.
daily@daily-jar-50-208:~$ java -version
openjdk version "1.8.0_181"
OpenJDK Runtime Environment (build 1.8.0_181-8u181-b13-0ubuntu0.16.04.1-b13)
OpenJDK 64-Bit Server VM (build 25.181-b13, mixed mode)
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$ jps -l
22065 fota-option-boss.jar
19810 fota-risk.jar
4882 fota-activity.jar
30005 sun.tools.jps.Jps
26558 fota-match.jar
14862 fota-websocket.jar
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$ jmap -heap 14862
Attaching to process ID 14862, please wait...
Error attaching to process: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 14862: Operation not permitted
sun.jvm.hotspot.debugger.DebuggerException: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 14862: Operation not permitted
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread.execute(LinuxDebuggerLocal.java:163)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.attach(LinuxDebuggerLocal.java:278)
	at sun.jvm.hotspot.HotSpotAgent.attachDebugger(HotSpotAgent.java:671)
	at sun.jvm.hotspot.HotSpotAgent.setupDebuggerLinux(HotSpotAgent.java:611)
	at sun.jvm.hotspot.HotSpotAgent.setupDebugger(HotSpotAgent.java:337)
	at sun.jvm.hotspot.HotSpotAgent.go(HotSpotAgent.java:304)
	at sun.jvm.hotspot.HotSpotAgent.attach(HotSpotAgent.java:140)
	at sun.jvm.hotspot.tools.Tool.start(Tool.java:185)
	at sun.jvm.hotspot.tools.Tool.execute(Tool.java:118)
	at sun.jvm.hotspot.tools.HeapSummary.main(HeapSummary.java:49)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at sun.tools.jmap.JMap.runTool(JMap.java:201)
	at sun.tools.jmap.JMap.main(JMap.java:130)
Caused by: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 14862: Operation not permitted
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.attach0(Native Method)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.access$100(LinuxDebuggerLocal.java:62)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$1AttachTask.doit(LinuxDebuggerLocal.java:269)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread.run(LinuxDebuggerLocal.java:138)

daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$ jmap -J-d64 -heap 14862
Attaching to process ID 14862, please wait...
Error attaching to process: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 14862: Operation not permitted
sun.jvm.hotspot.debugger.DebuggerException: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 14862: Operation not permitted
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread.execute(LinuxDebuggerLocal.java:163)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.attach(LinuxDebuggerLocal.java:278)
	at sun.jvm.hotspot.HotSpotAgent.attachDebugger(HotSpotAgent.java:671)
	at sun.jvm.hotspot.HotSpotAgent.setupDebuggerLinux(HotSpotAgent.java:611)
	at sun.jvm.hotspot.HotSpotAgent.setupDebugger(HotSpotAgent.java:337)
	at sun.jvm.hotspot.HotSpotAgent.go(HotSpotAgent.java:304)
	at sun.jvm.hotspot.HotSpotAgent.attach(HotSpotAgent.java:140)
	at sun.jvm.hotspot.tools.Tool.start(Tool.java:185)
	at sun.jvm.hotspot.tools.Tool.execute(Tool.java:118)
	at sun.jvm.hotspot.tools.HeapSummary.main(HeapSummary.java:49)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at sun.tools.jmap.JMap.runTool(JMap.java:201)
	at sun.tools.jmap.JMap.main(JMap.java:130)
Caused by: sun.jvm.hotspot.debugger.DebuggerException: Can't attach to the process: ptrace(PTRACE_ATTACH, ..) failed for 14862: Operation not permitted
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.attach0(Native Method)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal.access$100(LinuxDebuggerLocal.java:62)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$1AttachTask.doit(LinuxDebuggerLocal.java:269)
	at sun.jvm.hotspot.debugger.linux.LinuxDebuggerLocal$LinuxDebuggerLocalWorkerThread.run(LinuxDebuggerLocal.java:138)

daily@daily-jar-50-208:~$ cd /opt/deploy/websocket/
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$ cd ~
daily@daily-jar-50-208:~$ jmap -dump:format=b,file=/opt/deploy/websocket/
Usage:
    jmap [option] <pid>
        (to connect to running process)
    jmap [option] <executable <core>
        (to connect to a core file)
    jmap [option] [server_id@]<remote server IP or hostname>
        (to connect to remote debug server)

where <option> is one of:
    <none>               to print same info as Solaris pmap
    -heap                to print java heap summary
    -histo[:live]        to print histogram of java object heap; if the "live"
                         suboption is specified, only count live objects
    -clstats             to print class loader statistics
    -finalizerinfo       to print information on objects awaiting finalization
    -dump:<dump-options> to dump java heap in hprof binary format
                         dump-options:
                           live         dump only live objects; if not specified,
                                        all objects in the heap are dumped.
                           format=b     binary format
                           file=<file>  dump heap to <file>
                         Example: jmap -dump:live,format=b,file=heap.bin <pid>
    -F                   force. Use with -dump:<dump-options> <pid> or -histo
                         to force a heap dump or histogram when <pid> does not
                         respond. The "live" suboption is not supported
                         in this mode.
    -h | -help           to print this help message
    -J<flag>             to pass <flag> directly to the runtime system
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$ jmap -dump:format=b,file=/opt/deploy/websocket/ 14862
Dumping heap to /opt/deploy/websocket ...
File exists
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$
daily@daily-jar-50-208:~$ cd /opt/deploy/websocket/
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$ ls -l
total 64972
-rw-rw-r-- 1 daily daily   309716 Nov  2 06:35 1
-rw-rw-r-- 1 daily daily  3870647 Apr 29 02:41 a.out
drwxrwxr-x 2 daily daily     4096 Apr 29 05:48 BAK
-rw-r--r-- 1 daily daily 62327634 Apr 29 05:48 fota-websocket.jar
drwxrwxr-x 3 daily daily     4096 Apr 30 02:00 logs
drwxrwxr-x 2 daily daily     4096 Jan 21 02:42 NEW
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$ jmap -dump:format=b,file=/opt/deploy/websocket/dump.hprof 14862
Dumping heap to /opt/deploy/websocket/dump.hprof ...
Heap dump file created
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$ ls -l
total 902220
-rw-rw-r-- 1 daily daily    309716 Nov  2 06:35 1
-rw-rw-r-- 1 daily daily   3870647 Apr 29 02:41 a.out
drwxrwxr-x 2 daily daily      4096 Apr 29 05:48 BAK
-rw------- 1 daily daily 857336705 Apr 30 08:31 dump.hprof
-rw-r--r-- 1 daily daily  62327634 Apr 29 05:48 fota-websocket.jar
drwxrwxr-x 3 daily daily      4096 Apr 30 02:00 logs
drwxrwxr-x 2 daily daily      4096 Jan 21 02:42 NEW
daily@daily-jar-50-208:/opt/deploy/websocket$ ls -lh
total 882M
-rw-rw-r-- 1 daily daily 303K Nov  2 06:35 1
-rw-rw-r-- 1 daily daily 3.7M Apr 29 02:41 a.out
drwxrwxr-x 2 daily daily 4.0K Apr 29 05:48 BAK
-rw------- 1 daily daily 818M Apr 30 08:31 dump.hprof
-rw-r--r-- 1 daily daily  60M Apr 29 05:48 fota-websocket.jar
drwxrwxr-x 3 daily daily 4.0K Apr 30 02:00 logs
drwxrwxr-x 2 daily daily 4.0K Jan 21 02:42 NEW
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$
daily@daily-jar-50-208:/opt/deploy/websocket$
```

#### jstack 
- top命令查看各个进程的cpu
- jstack pid命令查看当前java进程的堆栈状态
- top -Hp 可以查看该进程下各个线程的cpu使用情况
- 获取到了占用cpu资源较高的线程pid，将该pid转成16进制的值，在jstack thread dump中每个线程都有一个nid，找到对应的nid即可

```
daily@daily-jar-50-208:/opt/deploy/websocket/logs$ jstack 9371 > 9371.thread4
daily@daily-jar-50-208:/opt/deploy/websocket/logs$ ls -lh
total 104M
drwxrwxr-x 9 daily daily 4.0K Jul  4 04:00 7z
-rw-rw-r-- 1 daily daily 152K Jul  4 06:20 9371.thread
-rw-rw-r-- 1 daily daily 192K Jul  4 06:21 9371.thread2
-rw-rw-r-- 1 daily daily 152K Jul  4 06:26 9371.thread3
-rw-rw-r-- 1 daily daily 372K Jul  4 06:31 9371.thread4
-rw-rw-r-- 1 daily daily 1.2M Apr 28 03:21 a.out
-rw-rw-r-- 1 daily daily 9.4M Jul  4 06:31 error.log
-rw-rw-r-- 1 daily daily 5.8M Jun  6 02:39 exchange.log
-rw-rw-r-- 1 daily daily  33M Jul  4 06:31 info.log
-rw-rw-r-- 1 daily daily 2.4M Nov 23  2018 monitor2
-rw-rw-r-- 1 daily daily 3.9M Jul  4 06:31 monitor.log
-rw-rw-r-- 1 daily daily  49M Jul  4 06:31 webSocket.log
daily@daily-jar-50-208:/opt/deploy/websocket/logs$
```

- docker
```
root@test-jar-50-191:~#
root@test-jar-50-191:~# docker ps
CONTAINER ID        IMAGE                                            COMMAND                  CREATED             STATUS              PORTS               NAMES
f8208b76ebb4        harbor.emc.top/test-fota/fota-commander:latest   "java -Duser.timezon…"   5 hours ago         Up 5 hours                              fota-commander
daab63767745        harbor.emc.top/test-fota/fota-asset:latest       "java -Duser.timezon…"   20 hours ago        Up 20 hours                             fota-asset
c90dc7b5931f        harbor.emc.top/test-fota/fota-market:latest      "java -Duser.timezon…"   29 hours ago        Up 29 hours                             fota-market
44ebb36e3894        harbor.emc.top/test-we/we-option-boss:latest     "java -Duser.timezon…"   2 weeks ago         Up 2 weeks                              we-option-boss
d7a709d819a7        ubuntu:latest                                    "/bin/bash"              7 weeks ago         Up 7 weeks                              eloquent_torvalds
cb6fb3e2eb9b        harbor.emc.top/test-fota/fota-data:latest        "java -Duser.timezon…"   2 months ago        Up 8 weeks                              fota-data
6ccc2a269c47        harbor.emc.top/test-fota/fota-loan:latest        "java -Duser.timezon…"   3 months ago        Up 3 months                             fota-loan
root@test-jar-50-191:~# docker exec -it c90dc7b5931f  ps -ef
PID   USER     TIME  COMMAND
    1 root     10h30 java -Duser.timezone=UTC -Djava.security.egd=file:/dev/./u
 2953 root      0:00 ps -ef
root@test-jar-50-191:~#
root@test-jar-50-191:~#
root@test-jar-50-191:~#
root@test-jar-50-191:~# docker exec -it c90dc7b5931f  /bin/sh
/application # jps
1 fota-market.jar
2974 Jps
/application # jps -l
1 /application/fota-market.jar
2986 sun.tools.jps.Jps
/application # jstack 1
1: Unable to get pid of LinuxThreads manager thread
/application # jps -l
1 /application/fota-market.jar
3012 sun.tools.jps.Jps
/application #
/application #
/application # jstack 1
1: Unable to get pid of LinuxThreads manager thread
/application #
/application #
/application #
/application # ^C
/application # quit
/bin/sh: quit: not found
/application # exit
root@test-jar-50-191:~#
root@test-jar-50-191:~#
```

### 查看 JVM options 
- -XX:+PrintFlagsFinal
```

daily@daily-jar-50-200:/opt/deploy/websocket/NEW$ java -XX:+PrintFlagsFinal -jar fota-websocket.jar
[Global flags]
    uintx AdaptiveSizeDecrementScaleFactor          = 4                                   {product}
    uintx AdaptiveSizeMajorGCDecayTimeScale         = 10                                  {product}
    uintx AdaptiveSizePausePolicy                   = 0                                   {product}
    uintx AdaptiveSizePolicyCollectionCostMargin    = 50                                  {product}
    uintx AdaptiveSizePolicyInitializingSteps       = 20                                  {product}

```

- jinfo
- 

```
nohup java -Xms1024m -Xmx4096m -XX:+PrintGCDetails -Xloggc:/opt/deploy/websocket/logs/gc.log -XX:+PrintGCDateStamps -XX:+PrintFlagsFinal -Denv=dev -jar fota-websocket.jar >/dev/null 2>&1 &
```

```
nohup java -Xms1024m -Xmx4096m -XX:+PrintGCDetails -Xloggc:/opt/deploy/websocket/logs/gc.log -XX:+PrintGCDateStamps -XX:+PrintFlagsFinal -Denv=dev -jar fota-websocket.jar >/opt/deploy/websocket/logs/java.log 2>&1 &
```
```
nohup java -Xms1024m -Xmx4096m -XX:+PrintGCDetails -Xloggc:/opt/deploy/websocket/logs/gc.log -XX:+PrintGCDateStamps -XX:+PrintFlagsFinal -Denv=dev -Dcom.sun.management.jmxremote.port=9988 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false  -jar fota-websocket.jar >/opt/deploy/websocket/logs/java.log 2>&1 &
```

- VisualVM (JMX配置) 需要JDK
```
-Dcom.sun.management.jmxremote.port=9988 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false
```

### 字节码指令(the instructions that comprise the Java bytecodes)
- javap
```
Last login: Sun Jul 14 09:53:23 on ttys000
zhenglubiaodeMacBook-Pro:~ zlb$ cd /Users/zlb/Downloads/
zhenglubiaodeMacBook-Pro:Downloads zlb$
zhenglubiaodeMacBook-Pro:Downloads zlb$ javac DocFooter.java
zhenglubiaodeMacBook-Pro:Downloads zlb$ javap DocFooter.class
Compiled from "DocFooter.java"
public class DocFooter extends java.applet.Applet {
  java.lang.String date;
  java.lang.String email;
  public DocFooter();
  public void init();
  public void paint(java.awt.Graphics);
}
zhenglubiaodeMacBook-Pro:Downloads zlb$ cat DocFooter.java
import java.awt.*;
import java.applet.*;

public class DocFooter extends Applet {
        String date;
        String email;

        public void init() {
                resize(500,100);
                date = getParameter("LAST_UPDATED");
                email = getParameter("EMAIL");
        }

        public void paint(Graphics g) {
                g.drawString(date + " by ",100, 15);
                g.drawString(email,290,15);
        }
zhenglubiaodeMacBook-Pro:Downloads zlb$
zhenglubiaodeMacBook-Pro:Downloads zlb$ javap -c DocFooter.class
Compiled from "DocFooter.java"
public class DocFooter extends java.applet.Applet {
  java.lang.String date;

  java.lang.String email;

  public DocFooter();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/applet/Applet."<init>":()V
       4: return

  public void init();
    Code:
       0: aload_0
       1: sipush        500
       4: bipush        100
       6: invokevirtual #2                  // Method resize:(II)V
       9: aload_0
      10: aload_0
      11: ldc           #3                  // String LAST_UPDATED
      13: invokevirtual #4                  // Method getParameter:(Ljava/lang/String;)Ljava/lang/String;
      16: putfield      #5                  // Field date:Ljava/lang/String;
      19: aload_0
      20: aload_0
      21: ldc           #6                  // String EMAIL
      23: invokevirtual #4                  // Method getParameter:(Ljava/lang/String;)Ljava/lang/String;
      26: putfield      #7                  // Field email:Ljava/lang/String;
      29: return

  public void paint(java.awt.Graphics);
    Code:
       0: aload_1
       1: new           #8                  // class java/lang/StringBuilder
       4: dup
       5: invokespecial #9                  // Method java/lang/StringBuilder."<init>":()V
       8: aload_0
       9: getfield      #5                  // Field date:Ljava/lang/String;
      12: invokevirtual #10                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      15: ldc           #11                 // String  by
      17: invokevirtual #10                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      20: invokevirtual #12                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
      23: bipush        100
      25: bipush        15
      27: invokevirtual #13                 // Method java/awt/Graphics.drawString:(Ljava/lang/String;II)V
      30: aload_1
      31: aload_0
      32: getfield      #7                  // Field email:Ljava/lang/String;
      35: sipush        290
      38: bipush        15
      40: invokevirtual #13                 // Method java/awt/Graphics.drawString:(Ljava/lang/String;II)V
      43: return
}
zhenglubiaodeMacBook-Pro:Downloads zlb$
```
```
zhenglubiaodeMacBook-Pro:Downloads zlb$
zhenglubiaodeMacBook-Pro:Downloads zlb$
zhenglubiaodeMacBook-Pro:Downloads zlb$
zhenglubiaodeMacBook-Pro:Downloads zlb$ cd ~
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$ cd /Users/zlb/Downloads/
zhenglubiaodeMacBook-Pro:Downloads zlb$
zhenglubiaodeMacBook-Pro:Downloads zlb$ cat DocFooter.java
import java.awt.*;
import java.applet.*;

public class DocFooter extends Applet {
        String date;
        String email;

        public void init() {
                resize(500,100);
                date = getParameter("LAST_UPDATED");
                email = getParameter("EMAIL");
        }

        public void paint(Graphics g) {
                g.drawString(date + " by ",100, 15);
                g.drawString(email,290,15);
        }

        public void test(){
                int zlb = 4;

                int bb = zlb + 9;
        }
}
zhenglubiaodeMacBook-Pro:Downloads zlb$
zhenglubiaodeMacBook-Pro:Downloads zlb$ javac DocFooter.java
zhenglubiaodeMacBook-Pro:Downloads zlb$
zhenglubiaodeMacBook-Pro:Downloads zlb$ javap DocFooter.java
错误: 找不到类: DocFooter.java
zhenglubiaodeMacBook-Pro:Downloads zlb$
zhenglubiaodeMacBook-Pro:Downloads zlb$ javap DocFooter.class
Compiled from "DocFooter.java"
public class DocFooter extends java.applet.Applet {
  java.lang.String date;
  java.lang.String email;
  public DocFooter();
  public void init();
  public void paint(java.awt.Graphics);
  public void test();
}
zhenglubiaodeMacBook-Pro:Downloads zlb$
zhenglubiaodeMacBook-Pro:Downloads zlb$
zhenglubiaodeMacBook-Pro:Downloads zlb$ javap -c DocFooter.class
Compiled from "DocFooter.java"
public class DocFooter extends java.applet.Applet {
  java.lang.String date;

  java.lang.String email;

  public DocFooter();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/applet/Applet."<init>":()V
       4: return

  public void init();
    Code:
       0: aload_0
       1: sipush        500
       4: bipush        100
       6: invokevirtual #2                  // Method resize:(II)V
       9: aload_0
      10: aload_0
      11: ldc           #3                  // String LAST_UPDATED
      13: invokevirtual #4                  // Method getParameter:(Ljava/lang/String;)Ljava/lang/String;
      16: putfield      #5                  // Field date:Ljava/lang/String;
      19: aload_0
      20: aload_0
      21: ldc           #6                  // String EMAIL
      23: invokevirtual #4                  // Method getParameter:(Ljava/lang/String;)Ljava/lang/String;
      26: putfield      #7                  // Field email:Ljava/lang/String;
      29: return

  public void paint(java.awt.Graphics);
    Code:
       0: aload_1
       1: new           #8                  // class java/lang/StringBuilder
       4: dup
       5: invokespecial #9                  // Method java/lang/StringBuilder."<init>":()V
       8: aload_0
       9: getfield      #5                  // Field date:Ljava/lang/String;
      12: invokevirtual #10                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      15: ldc           #11                 // String  by
      17: invokevirtual #10                 // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      20: invokevirtual #12                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
      23: bipush        100
      25: bipush        15
      27: invokevirtual #13                 // Method java/awt/Graphics.drawString:(Ljava/lang/String;II)V
      30: aload_1
      31: aload_0
      32: getfield      #7                  // Field email:Ljava/lang/String;
      35: sipush        290
      38: bipush        15
      40: invokevirtual #13                 // Method java/awt/Graphics.drawString:(Ljava/lang/String;II)V
      43: return

  public void test();
    Code:
       0: iconst_4
       1: istore_1
       2: iload_1
       3: bipush        9
       5: iadd
       6: istore_2
       7: return
}
zhenglubiaodeMacBook-Pro:Downloads zlb$
```

- Dead Lock
```
zhenglubiaodeMacBook-Pro:six zlb$
zhenglubiaodeMacBook-Pro:six zlb$
zhenglubiaodeMacBook-Pro:six zlb$ cat LockDemo.java
/**
 * @author Yuanming Tao
 * Created on 2019/9/12
 * Description
 */
public class LockDemo {
    public static void main(String[] args) {

        final Object a = new Object();
        final Object b = new Object();
        Thread threadA = new Thread(new Runnable() {
            public void run() {
                synchronized (a) {
                    try {
                        System.out.println("now i in threadA-locka");
                        Thread.sleep(1000l);
                        synchronized (b) {
                            System.out.println("now i in threadA-lockb");
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        });

        Thread threadB = new Thread(new Runnable() {
            public void run() {
                synchronized (b) {
                    try {
                        System.out.println("now i in threadB-lockb");
                        Thread.sleep(1000l);
                        synchronized (a) {
                            System.out.println("now i in threadB-locka");
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        });

        threadA.start();
        threadB.start();
    }
}

zhenglubiaodeMacBook-Pro:six zlb$
zhenglubiaodeMacBook-Pro:six zlb$ ls -l
total 32
-rw-r--r--   1 zlb  staff   996  9 12 15:32 LockDemo$1.class
-rw-r--r--   1 zlb  staff   996  9 12 15:32 LockDemo$2.class
-rw-r--r--   1 zlb  staff   549  9 12 15:32 LockDemo.class
-rw-r--r--   1 zlb  staff  1376  9 12 15:32 LockDemo.java
zhenglubiaodeMacBook-Pro:six zlb$
zhenglubiaodeMacBook-Pro:six zlb$ java LockDemo
now i in threadA-locka
now i in threadB-lockb



```
```
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$ jps
4736
4758 RemoteMavenServer
9207 LockDemo
4747 Launcher
9213 Jps
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$ jstack 9207
2019-09-12 15:35:54
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.171-b11 mixed mode):

"Attach Listener" #12 daemon prio=9 os_prio=31 tid=0x00007fc2d0800000 nid=0xc07 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"DestroyJavaVM" #11 prio=5 os_prio=31 tid=0x00007fc2cf887000 nid=0x2703 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Thread-1" #10 prio=5 os_prio=31 tid=0x00007fc2d0024800 nid=0x4303 waiting for monitor entry [0x000070000557c000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at LockDemo$2.run(LockDemo.java:34)
	- waiting to lock <0x00000007955f0f68> (a java.lang.Object)
	- locked <0x00000007955f0f78> (a java.lang.Object)
	at java.lang.Thread.run(Thread.java:748)

"Thread-0" #9 prio=5 os_prio=31 tid=0x00007fc2d0023800 nid=0x3c03 waiting for monitor entry [0x0000700005479000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at LockDemo$1.run(LockDemo.java:18)
	- waiting to lock <0x00000007955f0f78> (a java.lang.Object)
	- locked <0x00000007955f0f68> (a java.lang.Object)
	at java.lang.Thread.run(Thread.java:748)

"Service Thread" #8 daemon prio=9 os_prio=31 tid=0x00007fc2d001e800 nid=0x3a03 runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C1 CompilerThread2" #7 daemon prio=9 os_prio=31 tid=0x00007fc2d001e000 nid=0x4703 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread1" #6 daemon prio=9 os_prio=31 tid=0x00007fc2d001d000 nid=0x4903 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" #5 daemon prio=9 os_prio=31 tid=0x00007fc2cf022800 nid=0x4a03 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Signal Dispatcher" #4 daemon prio=9 os_prio=31 tid=0x00007fc2d0019000 nid=0x360b runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"Finalizer" #3 daemon prio=8 os_prio=31 tid=0x00007fc2cf813000 nid=0x5103 in Object.wait() [0x0000700004d64000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x0000000795588ed0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:143)
	- locked <0x0000000795588ed0> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:164)
	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:212)

"Reference Handler" #2 daemon prio=10 os_prio=31 tid=0x00007fc2d0015000 nid=0x5203 in Object.wait() [0x0000700004c61000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x0000000795586bf8> (a java.lang.ref.Reference$Lock)
	at java.lang.Object.wait(Object.java:502)
	at java.lang.ref.Reference.tryHandlePending(Reference.java:191)
	- locked <0x0000000795586bf8> (a java.lang.ref.Reference$Lock)
	at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:153)

"VM Thread" os_prio=31 tid=0x00007fc2d0010800 nid=0x2d03 runnable

"GC task thread#0 (ParallelGC)" os_prio=31 tid=0x00007fc2d000d800 nid=0x2307 runnable

"GC task thread#1 (ParallelGC)" os_prio=31 tid=0x00007fc2d000e000 nid=0x2203 runnable

"GC task thread#2 (ParallelGC)" os_prio=31 tid=0x00007fc2cf003000 nid=0x5403 runnable

"GC task thread#3 (ParallelGC)" os_prio=31 tid=0x00007fc2cf804800 nid=0x2b03 runnable

"VM Periodic Task Thread" os_prio=31 tid=0x00007fc2cf886800 nid=0x4503 waiting on condition

JNI global references: 5


Found one Java-level deadlock:
=============================
"Thread-1":
  waiting to lock monitor 0x00007fc2cf856ab8 (object 0x00000007955f0f68, a java.lang.Object),
  which is held by "Thread-0"
"Thread-0":
  waiting to lock monitor 0x00007fc2cf859558 (object 0x00000007955f0f78, a java.lang.Object),
  which is held by "Thread-1"

Java stack information for the threads listed above:
===================================================
"Thread-1":
	at LockDemo$2.run(LockDemo.java:34)
	- waiting to lock <0x00000007955f0f68> (a java.lang.Object)
	- locked <0x00000007955f0f78> (a java.lang.Object)
	at java.lang.Thread.run(Thread.java:748)
"Thread-0":
	at LockDemo$1.run(LockDemo.java:18)
	- waiting to lock <0x00000007955f0f78> (a java.lang.Object)
	- locked <0x00000007955f0f68> (a java.lang.Object)
	at java.lang.Thread.run(Thread.java:748)

Found 1 deadlock.

zhenglubiaodeMacBook-Pro:~ zlb$
```

- jmap
```
zhenglubiaodeMacBook-Pro:six zlb$ java threadPool
start f1

Killed: 9
zhenglubiaodeMacBook-Pro:six zlb$
zhenglubiaodeMacBook-Pro:six zlb$ java threadPool
start f1
zhenglubiaodeMacBook-Pro:six zlb$
zhenglubiaodeMacBook-Pro:six zlb$ ls -l
total 32
drwxr-xr-x   7 zlb  staff   224  5 12 17:32 elastic-job-study
drwxr-xr-x  14 zlb  staff   448  2 15  2019 fota-account
drwxr-xr-x  10 zlb  staff   320  1 24  2019 fota-commander
drwxr-xr-x  10 zlb  staff   320  4  4 11:50 fota-option
drwxr-xr-x   9 zlb  staff   288  2 11  2019 fota-policy
drwxr-xr-x  10 zlb  staff   320  8 13 14:13 fota-wbsocket
drwxr-xr-x  14 zlb  staff   448  1 24  2019 fota-web
drwxr-xr-x  14 zlb  staff   448 12 25  2018 fota_monitor
drwxr-xr-x   4 zlb  staff   128 12 27  2018 jdk
-rw-r--r--   1 zlb  staff   914  9 12 15:57 threadPool$1$1.class
-rw-r--r--   1 zlb  staff  1536  9 12 15:57 threadPool$1.class
-rw-r--r--   1 zlb  staff   717  9 12 15:57 threadPool.class
-rw-r--r--   1 zlb  staff  1129  9 12 15:57 threadPool.java
drwxr-xr-x   8 zlb  staff   256 12 27  2018 tools
zhenglubiaodeMacBook-Pro:six zlb$
zhenglubiaodeMacBook-Pro:six zlb$ sudo -s
Password:
bash-3.2# ls -l
total 56
-rw-r--r--@  1 zlb  staff  10244  9 12 18:21 .DS_Store
drwxr-xr-x   7 zlb  staff    224  5 12 17:32 elastic-job-study
drwxr-xr-x  14 zlb  staff    448  2 15  2019 fota-account
drwxr-xr-x  10 zlb  staff    320  1 24  2019 fota-commander
drwxr-xr-x  10 zlb  staff    320  4  4 11:50 fota-option
drwxr-xr-x   9 zlb  staff    288  2 11  2019 fota-policy
drwxr-xr-x  10 zlb  staff    320  8 13 14:13 fota-wbsocket
drwxr-xr-x  14 zlb  staff    448  1 24  2019 fota-web
drwxr-xr-x  14 zlb  staff    448 12 25  2018 fota_monitor
drwxr-xr-x   4 zlb  staff    128 12 27  2018 jdk
-rw-r--r--   1 zlb  staff    914  9 12 15:57 threadPool$1$1.class
-rw-r--r--   1 zlb  staff   1536  9 12 15:57 threadPool$1.class
-rw-r--r--   1 zlb  staff    717  9 12 15:57 threadPool.class
-rw-r--r--   1 zlb  staff   1129  9 12 15:57 threadPool.java
drwxr-xr-x   8 zlb  staff    256 12 27  2018 tools
bash-3.2#
bash-3.2# java threadPool
start f1

Killed: 9
bash-3.2#
bash-3.2#
bash-3.2#
bash-3.2# exit
exit
zhenglubiaodeMacBook-Pro:six zlb$
```
```
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBo323ok-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$ jps
12337 Launcher
12327
13483 threadPool
12347 RemoteMavenServer
12668 Launcher
13486 Jps
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$ java -version
java version "1.8.0_171"
Java(TM) SE Runtime Environment (build 1.8.0_171-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.171-b11, mixed mode)
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$ jmap -J-d64 -heap 13483
Attaching to process ID 13483, please wait...
Error attaching to process: sun.jvm.hotspot.debugger.DebuggerException: Can't attach symbolicator to the process
sun.jvm.hotspot.debugger.DebuggerException: sun.jvm.hotspot.debugger.DebuggerException: Can't attach symbolicator to the process
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal$BsdDebuggerLocalWorkerThread.execute(BsdDebuggerLocal.java:169)
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal.attach(BsdDebuggerLocal.java:287)
	at sun.jvm.hotspot.HotSpotAgent.attachDebugger(HotSpotAgent.java:671)
	at sun.jvm.hotspot.HotSpotAgent.setupDebuggerDarwin(HotSpotAgent.java:659)
	at sun.jvm.hotspot.HotSpotAgent.setupDebugger(HotSpotAgent.java:341)
	at sun.jvm.hotspot.HotSpotAgent.go(HotSpotAgent.java:304)
	at sun.jvm.hotspot.HotSpotAgent.attach(HotSpotAgent.java:140)
	at sun.jvm.hotspot.tools.Tool.start(Tool.java:185)
	at sun.jvm.hotspot.tools.Tool.execute(Tool.java:118)
	at sun.jvm.hotspot.tools.HeapSummary.main(HeapSummary.java:49)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at sun.tools.jmap.JMap.runTool(JMap.java:201)
	at sun.tools.jmap.JMap.main(JMap.java:130)
Caused by: sun.jvm.hotspot.debugger.DebuggerException: Can't attach symbolicator to the process
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal.attach0(Native Method)
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal.access$100(BsdDebuggerLocal.java:65)
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal$1AttachTask.doit(BsdDebuggerLocal.java:278)
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal$BsdDebuggerLocalWorkerThread.run(BsdDebuggerLocal.java:144)

zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$ jps
12337 Launcher
13491 threadPool
13492 Jps
12327
12347 RemoteMavenServer
12668 Launcher
zhenglubiaodeMacBook-Pro:~ zlb$
zhenglubiaodeMacBook-Pro:~ zlb$ sudo -s
bash-3.2#
bash-3.2# jps
13496 Jps
bash-3.2# jps
13502 threadPool
13503 Jps
bash-3.2#
bash-3.2# jmap -J-d64 -heap 13502
Attaching to process ID 13502, please wait...
Error attaching to process: sun.jvm.hotspot.debugger.DebuggerException: Can't attach symbolicator to the process
sun.jvm.hotspot.debugger.DebuggerException: sun.jvm.hotspot.debugger.DebuggerException: Can't attach symbolicator to the process
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal$BsdDebuggerLocalWorkerThread.execute(BsdDebuggerLocal.java:169)
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal.attach(BsdDebuggerLocal.java:287)
	at sun.jvm.hotspot.HotSpotAgent.attachDebugger(HotSpotAgent.java:671)
	at sun.jvm.hotspot.HotSpotAgent.setupDebuggerDarwin(HotSpotAgent.java:659)
	at sun.jvm.hotspot.HotSpotAgent.setupDebugger(HotSpotAgent.java:341)
	at sun.jvm.hotspot.HotSpotAgent.go(HotSpotAgent.java:304)
	at sun.jvm.hotspot.HotSpotAgent.attach(HotSpotAgent.java:140)
	at sun.jvm.hotspot.tools.Tool.start(Tool.java:185)
	at sun.jvm.hotspot.tools.Tool.execute(Tool.java:118)
	at sun.jvm.hotspot.tools.HeapSummary.main(HeapSummary.java:49)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at sun.tools.jmap.JMap.runTool(JMap.java:201)
	at sun.tools.jmap.JMap.main(JMap.java:130)
Caused by: sun.jvm.hotspot.debugger.DebuggerException: Can't attach symbolicator to the process
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal.attach0(Native Method)
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal.access$100(BsdDebuggerLocal.java:65)
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal$1AttachTask.doit(BsdDebuggerLocal.java:278)
	at sun.jvm.hotspot.debugger.bsd.BsdDebuggerLocal$BsdDebuggerLocalWorkerThread.run(BsdDebuggerLocal.java:144)

bash-3.2#
bash-3.2#
bash-3.2#
bash-3.2# exit
exit
zhenglubiaodeMacBook-Pro:~ zlb$
```