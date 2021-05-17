```

root@daily-nginx-50-201:/etc/nginx/sites-enabled#
root@daily-nginx-50-201:/etc/nginx/sites-enabled#
root@daily-nginx-50-201:/etc/nginx/sites-enabled#
root@daily-nginx-50-201:/etc/nginx/sites-enabled#
root@daily-nginx-50-201:/etc/nginx/sites-enabled# cd /etc/nginx/sites-enabled
root@daily-nginx-50-201:/etc/nginx/sites-enabled#
root@daily-nginx-50-201:/etc/nginx/sites-enabled#
root@daily-nginx-50-201:/etc/nginx/sites-enabled#
root@daily-nginx-50-201:/etc/nginx/sites-enabled# cat coinbay_web.conf
map $http_upgrade $connection_upgrade {
    default upgrade;
    ''      close;
}
limit_req_zone $cookie_SESSION zone=persession:10m rate=1r/s;
limit_req_zone $server_name zone=perserver:20m rate=3000r/s;
limit_req_log_level notice;
#upstream fota-svr{
#    server 172.16.50.179:8083 weight=5;
#    server 172.16.50.182:8083 weight=5;
#}

#upstream fota-wbsocket-svr{
#    server 172.16.50.179:8090 weight=5;
#    server 172.16.50.182:8090 weight=5;
#}

#upstream fota-app-svr{
#    server 172.16.50.198:8084 weight=5;
#}

#upstream option{
#	server 172.16.50.200:8896;
#}

#upstream option-wbsocket{
#	server 172.16.50.200:8091;
#}
upstream fota-wbsocket-http{
#    server 172.16.50.200:8079 weight=5;
    server 172.16.50.208:8079 weight=5;
}
log_format  debug  '"cookie:$cookie_SESSION"';
server {
        listen       8090 ;
        server_name  _;
	client_max_body_size 2m;
        root /var/www/coinbay_front_web/;
        error_log /var/log/nginx/coinbay_web_error.log error;
	access_log /var/log/nginx/coinbay_web_access.log main;

	location /wapa {
		alias /var/www/coinbay_front_webapp/;
		try_files $uri $uri/ /index.html;
		break;
	}
        location / {
                add_header Access-Control-Allow-Origin *;
                add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
                add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization';

                if ($request_method = 'OPTIONS') {
                        return 204;
                }

                try_files $uri @fallback;
        }

	        location /test {
                add_header Access-Control-Allow-Origin *;
                add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
                add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization';

                if ($request_method = 'OPTIONS') {
                        return 204;
                }

                try_files $uri @fallback;
        }

        location /mobile/ {
                alias /var/www/fota_plist/;
                break;
        }

	location /option {
		alias /var/www/coinbay_front_option_web/;
		try_files $uri $uri/ /index.html;
		break;
	}
        location /apioption {
                rewrite ^/apioption/(.*)$ /$1 break;
                # API Server
                proxy_set_header  X-Real-IP  $http_x_forwarded_for;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header REMOTE-HOST $remote_addr;
                proxy_set_header Connection $connection_upgrade;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_pass http://option;
                proxy_set_header Host $host;
        }
        location /apioption/wsoption {
                rewrite ^/apioption/(.*)$ /$1 break;
                # API Server
                proxy_set_header  X-Real-IP  $http_x_forwarded_for;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection $connection_upgrade;
                proxy_pass http://option-wbsocket;
        }

	location /app {
		alias /var/www/fota_front_app/;
		#try_files $uri $uri/ /app/index.html;
		break;
	}

	location /mobile/download {
		alias /var/www/coinbay_front_app_download/;
		try_files $uri $uri/ @router;
		index index.html index htm;
		break;
	}
	location /trade {
#		rewrite ^/trade/(.*)$ /$1 break;
		alias /var/www/fota_front_trade_web/;
		try_files $uri $uri/ /index.html;
                break;
		#add_header Access-Control-Allow-Origin *;
                #add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
                #add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization';

                #if ($request_method = 'OPTIONS') {
                #        return 204;
                #}

               # try_files $uri @fallback;
        }

        location /borrow {
                alias /var/www/fota_front_borrow_web/;
                try_files $uri $uri/ /index.html;
                break;
                #add_header Access-Control-Allow-Origin *;
                #add_header Access-Control-Allow-Methods 'GET, POST, OPTIONS';
                #add_header Access-Control-Allow-Headers 'DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization';

                #if ($request_method = 'OPTIONS') {
                #        return 204;
                #}

               # try_files $uri @fallback;
        }
        location /api/ {
                rewrite ^/api/(.*)$ /$1 break;
                # API Server
                proxy_set_header  X-Real-IP  $http_x_forwarded_for;
                proxy_set_header Upgrade $http_upgrade;
        	proxy_set_header REMOTE-HOST $remote_addr;
                proxy_set_header Connection $connection_upgrade;
        	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_pass http://fota-svr;
		proxy_set_header Host $host;
        }

	location /api/websocket-exchange {
                rewrite ^/api/(.*)$ /$1 break;
                # API Server
                proxy_set_header  X-Real-IP  $http_x_forwarded_for;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection $connection_upgrade;
                proxy_pass http://172.16.50.208:8079;
        }


        location /api/websocket-exchange/purchase/seckill {
		rewrite ^/api/(.*)$ /$1 break;
                # API Server
                proxy_set_header  X-Real-IP  $http_x_forwarded_for;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection $connection_upgrade;
                proxy_pass http://fota-wbsocket-http;
                limit_req zone=persession burst=2 nodelay;
                limit_req zone=perserver burst=1000;
        }

        location /api/websocket {
                rewrite ^/api/(.*)$ /$1 break;
                # API Server
                proxy_set_header  X-Real-IP  $http_x_forwarded_for;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection $connection_upgrade;
                proxy_pass http://172.16.50.208:8092;
	}

        location /mapi/ {
                rewrite ^/mapi/(.*)$ /$1 break;
                # API Server
                proxy_set_header  X-Real-IP  $http_x_forwarded_for;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header REMOTE-HOST $remote_addr;
                proxy_set_header Connection $connection_upgrade;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_pass http://fota-app-svr;
                proxy_set_header Host $host:8089;
        }

        location @fallback {
            rewrite .* /index.html break;
        }

 	location @router {
		rewrite ^.*$ /index.html last;
	}
        error_page  400 402 403 404  /404.html;
        location = /404.html {
            root /var/www/common/;
        }

    #    error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
}
root@daily-nginx-50-201:/etc/nginx/sites-enabled#
root@daily-nginx-50-201:/etc/nginx/sites-enabled#
root@daily-nginx-50-201:/etc/nginx/sites-enabled#
root@daily-nginx-50-201:/etc/nginx/sites-enabled#


```