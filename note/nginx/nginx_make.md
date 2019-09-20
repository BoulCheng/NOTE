

```


Last login: Tue Apr 24 11:45:12 on ttys000
zhenglubiaodeMacBook-Pro:~ zlb$ cd /Users/zlb/application/nginx-1.13.12 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ make
make: *** No rule to make target `build', needed by `default'.  Stop.
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ CD /Users/zlb/application/nginx-1.14.0 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ ./configure --prefix=/usr/local/nginx --pid-path=/var/run/nginx.pid --lock-path=/var/lock/nginx.lock --with-http_ssl_module --with-http_dav_module --with-http_flv_module --with-http_realip_module --with-http_gzip_static_module --with-http_stub_status_module --with-mail --with-mail_ssl_module --with-pcre=../pcre-8.37 --with-zlib=../zlib-1.2.11 --with-debug --http-client-body-temp-path=/var/tmp/nginx/client --http-proxy-temp-path=/var/tmp/nginx/proxy --http-fastcgi-temp-path=/var/tmp/nginx/fastcgi --http-uwsgi-temp-path=/var/tmp/nginx/uwsgi --http-scgi-temp-path=/var/tmp/nginx/scgi
checking for OS
 + Darwin 17.3.0 x86_64
checking for C compiler ... found
 + using Clang C compiler
 + clang version: 9.1.0 (clang-902.0.39.1)
checking for -Wl,-E switch ... not found
checking for gcc builtin atomic operations ... found
checking for C99 variadic macros ... found
checking for gcc variadic macros ... found
checking for gcc builtin 64 bit byteswap ... found
checking for unistd.h ... found
checking for inttypes.h ... found
checking for limits.h ... found
checking for sys/filio.h ... found
checking for sys/param.h ... found
checking for sys/mount.h ... found
checking for sys/statvfs.h ... found
checking for crypt.h ... not found
checking for Darwin specific features
 + kqueue found
checking for kqueue's EVFILT_TIMER ... found
checking for Darwin 64-bit kqueue millisecond timeout bug ... not found
checking for sendfile() ... found
checking for atomic(3) ... found
checking for nobody group ... found
checking for poll() ... found
checking for /dev/poll ... not found
checking for crypt() ... found
checking for F_READAHEAD ... not found
checking for posix_fadvise() ... not found
checking for O_DIRECT ... not found
checking for F_NOCACHE ... found
checking for directio() ... not found
checking for statfs() ... found
checking for statvfs() ... found
checking for dlopen() ... found
checking for sched_yield() ... found
checking for sched_setaffinity() ... not found
checking for SO_SETFIB ... not found
checking for SO_REUSEPORT ... found
checking for SO_ACCEPTFILTER ... not found
checking for SO_BINDANY ... not found
checking for IP_TRANSPARENT ... not found
checking for IP_BINDANY ... not found
checking for IP_BIND_ADDRESS_NO_PORT ... not found
checking for IP_RECVDSTADDR ... found
checking for IP_SENDSRCADDR ... not found
checking for IP_PKTINFO ... found
checking for IPV6_RECVPKTINFO ... found
checking for TCP_DEFER_ACCEPT ... not found
checking for TCP_KEEPIDLE ... not found
checking for TCP_FASTOPEN ... found
checking for TCP_INFO ... not found
checking for accept4() ... not found
checking for eventfd() ... not found
checking for eventfd() (SYS_eventfd) ... not found
checking for int size ... 4 bytes
checking for long size ... 8 bytes
checking for long long size ... 8 bytes
checking for void * size ... 8 bytes
checking for uint32_t ... found
checking for uint64_t ... found
checking for sig_atomic_t ... found
checking for sig_atomic_t size ... 4 bytes
checking for socklen_t ... found
checking for in_addr_t ... found
checking for in_port_t ... found
checking for rlim_t ... found
checking for uintptr_t ... uintptr_t found
checking for system byte ordering ... little endian
checking for size_t size ... 8 bytes
checking for off_t size ... 8 bytes
checking for time_t size ... 8 bytes
checking for AF_INET6 ... found
checking for setproctitle() ... not found
checking for pread() ... found
checking for pwrite() ... found
checking for pwritev() ... not found
checking for sys_nerr ... found
checking for localtime_r() ... found
checking for clock_gettime(CLOCK_MONOTONIC) ... found
checking for posix_memalign() ... found
checking for memalign() ... not found
checking for mmap(MAP_ANON|MAP_SHARED) ... found
checking for mmap("/dev/zero", MAP_SHARED) ... found but is not working
checking for System V shared memory ... found
checking for POSIX semaphores ... found but is not working
checking for POSIX semaphores in libpthread ... found but is not working
checking for POSIX semaphores in librt ... not found
checking for struct msghdr.msg_control ... found
checking for ioctl(FIONBIO) ... found
checking for struct tm.tm_gmtoff ... found
checking for struct dirent.d_namlen ... found
checking for struct dirent.d_type ... found
checking for sysconf(_SC_NPROCESSORS_ONLN) ... found
checking for sysconf(_SC_LEVEL1_DCACHE_LINESIZE) ... not found
checking for openat(), fstatat() ... found
checking for getaddrinfo() ... found
checking for OpenSSL library ... not found
checking for OpenSSL library in /usr/local/ ... not found
checking for OpenSSL library in /usr/pkg/ ... not found
checking for OpenSSL library in /opt/local/ ... not found

./configure: error: SSL modules require the OpenSSL library.
You can either do not enable the modules, or install the OpenSSL library
into the system, or build the OpenSSL library statically from the source
with nginx by using --with-openssl=<path> option.

zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 


=====加入openssl后编译成功：
Last login: Tue Apr 24 11:45:12 on ttys000
zhenglubiaodeMacBook-Pro:~ zlb$ cd /Users/zlb/application/nginx-1.13.12 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ make
make: *** No rule to make target `build', needed by `default'.  Stop.
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ CD /Users/zlb/application/nginx-1.14.0 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 
zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ ./configure --prefix=/usr/local/nginx --pid-path=/var/run/nginx.pid --lock-path=/var/lock/nginx.lock --with-http_ssl_module --with-http_dav_module --with-http_flv_module --with-http_realip_module --with-http_gzip_static_module --with-http_stub_status_module --with-mail --with-mail_ssl_module --with-pcre=../pcre-8.37 --with-zlib=../zlib-1.2.11 --with-debug --http-client-body-temp-path=/var/tmp/nginx/client --http-proxy-temp-path=/var/tmp/nginx/proxy --http-fastcgi-temp-path=/var/tmp/nginx/fastcgi --http-uwsgi-temp-path=/var/tmp/nginx/uwsgi --http-scgi-temp-path=/var/tmp/nginx/scgi
checking for OS
 + Darwin 17.3.0 x86_64
checking for C compiler ... found
 + using Clang C compiler
 + clang version: 9.1.0 (clang-902.0.39.1)
checking for -Wl,-E switch ... not found
checking for gcc builtin atomic operations ... found
checking for C99 variadic macros ... found
checking for gcc variadic macros ... found
checking for gcc builtin 64 bit byteswap ... found
checking for unistd.h ... found
checking for inttypes.h ... found
checking for limits.h ... found
checking for sys/filio.h ... found
checking for sys/param.h ... found
checking for sys/mount.h ... found
checking for sys/statvfs.h ... found
checking for crypt.h ... not found
checking for Darwin specific features
 + kqueue found
checking for kqueue's EVFILT_TIMER ... found
checking for Darwin 64-bit kqueue millisecond timeout bug ... not found
checking for sendfile() ... found
checking for atomic(3) ... found
checking for nobody group ... found
checking for poll() ... found
checking for /dev/poll ... not found
checking for crypt() ... found
checking for F_READAHEAD ... not found
checking for posix_fadvise() ... not found
checking for O_DIRECT ... not found
checking for F_NOCACHE ... found
checking for directio() ... not found
checking for statfs() ... found
checking for statvfs() ... found
checking for dlopen() ... found
checking for sched_yield() ... found
checking for sched_setaffinity() ... not found
checking for SO_SETFIB ... not found
checking for SO_REUSEPORT ... found
checking for SO_ACCEPTFILTER ... not found
checking for SO_BINDANY ... not found
checking for IP_TRANSPARENT ... not found
checking for IP_BINDANY ... not found
checking for IP_BIND_ADDRESS_NO_PORT ... not found
checking for IP_RECVDSTADDR ... found
checking for IP_SENDSRCADDR ... not found
checking for IP_PKTINFO ... found
checking for IPV6_RECVPKTINFO ... found
checking for TCP_DEFER_ACCEPT ... not found
checking for TCP_KEEPIDLE ... not found
checking for TCP_FASTOPEN ... found
checking for TCP_INFO ... not found
checking for accept4() ... not found
checking for eventfd() ... not found
checking for eventfd() (SYS_eventfd) ... not found
checking for int size ... 4 bytes
checking for long size ... 8 bytes
checking for long long size ... 8 bytes
checking for void * size ... 8 bytes
checking for uint32_t ... found
checking for uint64_t ... found
checking for sig_atomic_t ... found
checking for sig_atomic_t size ... 4 bytes
checking for socklen_t ... found
checking for in_addr_t ... found
checking for in_port_t ... found
checking for rlim_t ... found
checking for uintptr_t ... uintptr_t found
checking for system byte ordering ... little endian
checking for size_t size ... 8 bytes
checking for off_t size ... 8 bytes
checking for time_t size ... 8 bytes
checking for AF_INET6 ... found
checking for setproctitle() ... not found
checking for pread() ... found
checking for pwrite() ... found
checking for pwritev() ... not found
checking for sys_nerr ... found
checking for localtime_r() ... found
checking for clock_gettime(CLOCK_MONOTONIC) ... found
checking for posix_memalign() ... found
checking for memalign() ... not found
checking for mmap(MAP_ANON|MAP_SHARED) ... found
checking for mmap("/dev/zero", MAP_SHARED) ... found but is not working
checking for System V shared memory ... found
checking for POSIX semaphores ... found but is not working
checking for POSIX semaphores in libpthread ... found but is not working
checking for POSIX semaphores in librt ... not found
checking for struct msghdr.msg_control ... found
checking for ioctl(FIONBIO) ... found
checking for struct tm.tm_gmtoff ... found
checking for struct dirent.d_namlen ... found
checking for struct dirent.d_type ... found
checking for sysconf(_SC_NPROCESSORS_ONLN) ... found
checking for sysconf(_SC_LEVEL1_DCACHE_LINESIZE) ... not found
checking for openat(), fstatat() ... found
checking for getaddrinfo() ... found
checking for OpenSSL library ... not found
checking for OpenSSL library in /usr/local/ ... not found
checking for OpenSSL library in /usr/pkg/ ... not found
checking for OpenSSL library in /opt/local/ ... not found

./configure: error: SSL modules require the OpenSSL library.
You can either do not enable the modules, or install the OpenSSL library
into the system, or build the OpenSSL library statically from the source
with nginx by using --with-openssl=<path> option.

zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ ./configure --prefix=/usr/local/nginx --pid-path=/var/run/nginx.pid --lock-path=/var/lock/nginx.lock --with-http_ssl_module --with-http_dav_module --with-http_flv_module --with-http_realip_module --with-http_gzip_static_module --with-http_stub_status_module --with-mail --with-mail_ssl_module --with-pcre=../pcre-8.37 --with-zlib=../zlib-1.2.11 --with-debug --http-client-body-temp-path=/var/tmp/nginx/client --http-proxy-temp-path=/var/tmp/nginx/proxy --http-fastcgi-temp-path=/var/tmp/nginx/fastcgi --http-uwsgi-temp-path=/var/tmp/nginx/uwsgi --http-scgi-temp-path=/var/tmp/nginx/scgi --with-openssl=../openssl-1.1.0h
checking for OS
 + Darwin 17.3.0 x86_64
checking for C compiler ... found
 + using Clang C compiler
 + clang version: 9.1.0 (clang-902.0.39.1)
checking for -Wl,-E switch ... not found
checking for gcc builtin atomic operations ... found
checking for C99 variadic macros ... found
checking for gcc variadic macros ... found
checking for gcc builtin 64 bit byteswap ... found
checking for unistd.h ... found
checking for inttypes.h ... found
checking for limits.h ... found
checking for sys/filio.h ... found
checking for sys/param.h ... found
checking for sys/mount.h ... found
checking for sys/statvfs.h ... found
checking for crypt.h ... not found
checking for Darwin specific features
 + kqueue found
checking for kqueue's EVFILT_TIMER ... found
checking for Darwin 64-bit kqueue millisecond timeout bug ... not found
checking for sendfile() ... found
checking for atomic(3) ... found
checking for nobody group ... found
checking for poll() ... found
checking for /dev/poll ... not found
checking for crypt() ... found
checking for F_READAHEAD ... not found
checking for posix_fadvise() ... not found
checking for O_DIRECT ... not found
checking for F_NOCACHE ... found
checking for directio() ... not found
checking for statfs() ... found
checking for statvfs() ... found
checking for dlopen() ... found
checking for sched_yield() ... found
checking for sched_setaffinity() ... not found
checking for SO_SETFIB ... not found
checking for SO_REUSEPORT ... found
checking for SO_ACCEPTFILTER ... not found
checking for SO_BINDANY ... not found
checking for IP_TRANSPARENT ... not found
checking for IP_BINDANY ... not found
checking for IP_BIND_ADDRESS_NO_PORT ... not found
checking for IP_RECVDSTADDR ... found
checking for IP_SENDSRCADDR ... not found
checking for IP_PKTINFO ... found
checking for IPV6_RECVPKTINFO ... found
checking for TCP_DEFER_ACCEPT ... not found
checking for TCP_KEEPIDLE ... not found
checking for TCP_FASTOPEN ... found
checking for TCP_INFO ... not found
checking for accept4() ... not found
checking for eventfd() ... not found
checking for eventfd() (SYS_eventfd) ... not found
checking for int size ... 4 bytes
checking for long size ... 8 bytes
checking for long long size ... 8 bytes
checking for void * size ... 8 bytes
checking for uint32_t ... found
checking for uint64_t ... found
checking for sig_atomic_t ... found
checking for sig_atomic_t size ... 4 bytes
checking for socklen_t ... found
checking for in_addr_t ... found
checking for in_port_t ... found
checking for rlim_t ... found
checking for uintptr_t ... uintptr_t found
checking for system byte ordering ... little endian
checking for size_t size ... 8 bytes
checking for off_t size ... 8 bytes
checking for time_t size ... 8 bytes
checking for AF_INET6 ... found
checking for setproctitle() ... not found
checking for pread() ... found
checking for pwrite() ... found
checking for pwritev() ... not found
checking for sys_nerr ... found
checking for localtime_r() ... found
checking for clock_gettime(CLOCK_MONOTONIC) ... found
checking for posix_memalign() ... found
checking for memalign() ... not found
checking for mmap(MAP_ANON|MAP_SHARED) ... found
checking for mmap("/dev/zero", MAP_SHARED) ... found but is not working
checking for System V shared memory ... found
checking for POSIX semaphores ... found but is not working
checking for POSIX semaphores in libpthread ... found but is not working
checking for POSIX semaphores in librt ... not found
checking for struct msghdr.msg_control ... found
checking for ioctl(FIONBIO) ... found
checking for struct tm.tm_gmtoff ... found
checking for struct dirent.d_namlen ... found
checking for struct dirent.d_type ... found
checking for sysconf(_SC_NPROCESSORS_ONLN) ... found
checking for sysconf(_SC_LEVEL1_DCACHE_LINESIZE) ... not found
checking for openat(), fstatat() ... found
checking for getaddrinfo() ... found
creating objs/Makefile

Configuration summary
  + using PCRE library: ../pcre-8.37
  + using OpenSSL library: ../openssl-1.1.0h
  + using zlib library: ../zlib-1.2.11

  nginx path prefix: "/usr/local/nginx"
  nginx binary file: "/usr/local/nginx/sbin/nginx"
  nginx modules path: "/usr/local/nginx/modules"
  nginx configuration prefix: "/usr/local/nginx/conf"
  nginx configuration file: "/usr/local/nginx/conf/nginx.conf"
  nginx pid file: "/var/run/nginx.pid"
  nginx error log file: "/usr/local/nginx/logs/error.log"
  nginx http access log file: "/usr/local/nginx/logs/access.log"
  nginx http client request body temporary files: "/var/tmp/nginx/client"
  nginx http proxy temporary files: "/var/tmp/nginx/proxy"
  nginx http fastcgi temporary files: "/var/tmp/nginx/fastcgi"
  nginx http uwsgi temporary files: "/var/tmp/nginx/uwsgi"
  nginx http scgi temporary files: "/var/tmp/nginx/scgi"

zhenglubiaodeMacBook-Pro:nginx-1.13.12 zlb$ 

```