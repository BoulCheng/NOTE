- 响应首部 Set-Cookie 被用来由服务器端向客户端发送cookie
- Cookie技术是客户端的解决方案，由服务器发送给客户端的特殊信息，存放在response的header中，这些信息以文本文件方式存放在客户端，由客户端每次向服务器发送请求时带上，此时是存放在request的header中
客户端向服务端Request请求。
服务器在response的header中设置Cookie，发送给客户端。
客户端会将请求request和Cookie一起打包发送给服务端。
服务端会根据Cookie判断将response返回给客户端。
因为HTTP协议“无状态”的特点，在请求完毕后会关闭连接，再次交换数据需要建立新的连接，无法跟踪会话。Cookie机制的引入正好弥补了HTTP协议“无状态”的缺陷。



Session是另一种记录客户状态的机制，不同的是Cookie保存在客户端浏览器中，而Session保存在服务器行。客户端浏览器访问的时候，服务器把客户端信息以某种形式记录在服务器上。

注意：当客户端浏览器再次请求服务器时是不需要携带信息的，在服务器上已有记录
在服务端运行程序时创建Session
在创建Session的同时，服务器会为该Session生成唯一的Session ID
在Session被创建后，可以调用相关方法往Session中添加内容，注意发送到客户端的只有Session ID
当客户端再次发送请求时，会将Session ID带上，服务器接受到请求之后就会依据ID确认用户身份，找到相应的Session。




在HTTP1.0中默认采用的是短连接，即浏览器和服务器每进行一次HTTP操作需要进行一次连接，任务结束时中断
次遇到以一个Web资源都会建立一个HTTP会话，进行三次握手，十分耗费资源。因此，通过在Request中增加“Connection：keep-alive”可支持长连接
客户端发出request，其中该版本号为1.0，并且在request中包含了一个header：“Connection：keep-alive”。
服务器收到该请求的长连接后，将会在response加上“Connection：keep-alive”，同时不会关闭已建立的TCP连接。
同时，客户端收到response中的header，发现是一个长连接，同样不会关闭已建立的TCP连接
在HTTP1.1起，默认使用长连接，来保持连接特性，即在请求头和响应头中默认加入“Connection：keep-alive” 。HTTP长连接利用同一个TCP连接处理多个HTTP请求和响应。

Keep-Alive不会永久保持连接，有一个保持时间，可以在不同的服务器中设定时间，实现长连接要求客户端和服务器都支持长连接




content-length: 44
content-type: application/json; charset=utf-8

GET /index.html HTTP/1.1
Host: www.example.com
Connection: upgrade
Upgrade: example/1, foo/2

首部字段类型
HTTP首部字段根据实际用途分为4中类型。
通用首部字段： 请求报文和响应报文两方都会使用到的首部。
Connection
Upgrade
Date

Cache-Control	

请求首部字段： 从客户端向服务器发送请求报文时使用的首部，补充了请求的附加内容、客户端信息、响应内容相关优先级等信息。
Accept: text/html,application/json;q=0.9, application/xml;q=0.8 (或者 */*)  ，通知服务器，用户代理能够处理的媒体类型及媒体类型的相对优先级
Accept-Charset
Accept-Encoding 
Accept-Language
Host 指明请求服务器的域名， 及服务器所监听的Tcp端口号，如果没有给定端口，会自动使用被请求服务的默认端口， 用于告知服务器请求资源所处的服务器域名及端口号
    - 描述请求将被发送的目的地，包括，且仅仅包括域名和端口号。在任何类型请求中，request都会包含此header信息
Origin 用来说明请求从哪里发起的，包括，且仅仅包括协议和域名
User-Agent 将创建请求的浏览器和用户代理信息等名称传达给服务器

Referer 可以根据Referer查看请求资源的是从哪个页面发起的，告知服务器请求的原始资源的URI，其用于所有类型的请求，并且包括：协议+域名+查询参数    


响应首部字段： 从服务器端向客户端返回响应报文时使用的首部，补充了响应时的附加内容，也会要求客户端附加额外的内容信息。
Server 首部字段Server告知客户端当前服务器上安装的HTTP服务器应用程序的信息
Access-Control-Allow-Origin:: https://juejin.cn

实体首部字段： 针对请求报文和响应报文的实体部分使用到的首部，补充了资源内容更新时间等与实体有关的信息
Content-Length 实体主体部分的大小（bites）
content-type: application/json; charset=utf-8
Expires

