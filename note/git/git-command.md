…or create a new repository on the command line
echo "# elastic-job-lite-millis" >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/BoulCheng/elastic-job-lite-millis.git
git push -u origin master
…or push an existing repository from the command line
git remote add origin https://github.com/BoulCheng/elastic-job-lite-millis.git
git push -u origin master
…or import code from another repository
You can initialize this frepository with code from a Subversion, Mercurial, or TFS project.



===============


.
.
.
.
.
.
ng/elastic-job-lite-millis.git
fatal: remote origin already exists.
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ git push -u origin masterremote: Repository not found.
fatal: repository 'https://github.com/BoulCheng/elastic-job-lite-millis.git/' not found
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ git push -u origin master
Counting objects: 3, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (2/2), done.
Writing objects: 100% (3/3), 1.82 KiB | 1.82 MiB/s, done.
Total 3 (delta 0), reused 0 (delta 0)
To https://github.com/BoulCheng/elastic-job-lite-millis.git
 * [new branch]      master -> master
Branch 'master' set up to track remote branch 'master' from 'origin'.
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ git add ./elastic-job-lite
fatal: pathspec './elastic-job-lite' did not match any files
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ git add /Users/zlb/IdeaProjects/elastic-job-lite
warning: CRLF will be replaced by LF in .gitignore.
The file will have its original line endings in your working directory.
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ git commit -m "first commit"
[master afec9d1] first commit
 Committer: zhenglubiao <zlb@zhenglubiaodeMacBook-Pro.local>
Your name and email address were configured automatically based
on your username and hostname. Please check that they are accurate.
You can suppress this message by setting them explicitly:

    git config --global user.name "Your Name"
    git config --global user.email you@example.com

After doing this, you may fix the identity used for this commit with:

    git commit --amend --reset-author

 499 files changed, 52310 insertions(+)
 create mode 100644 .gitignore
 create mode 100644 .travis.yml
 create mode 100644 LICENSE
 create mode 100644 README_ZH.md
 create mode 100644 RELEASE-NOTES.md
 create mode 100644 ROADMAP.md
 create mode 100644 elastic-job-lite-console/pom.xml
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/ConsoleBootstrap.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/domain/EventTraceDataSource.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/domain/EventTraceDataSourceConfiguration.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/domain/EventTraceDataSourceConfigurations.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/domain/EventTraceDataSourceFactory.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/domain/GlobalConfiguration.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/domain/RegistryCenterConfiguration.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/domain/RegistryCenterConfigurations.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/exception/JobConsoleException.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/filter/GlobalConfigurationFilter.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/repository/ConfigurationsXmlRepository.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/repository/XmlRepository.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/repository/impl/AbstractXmlRepositoryImpl.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/repository/impl/ConfigurationsXmlRepositoryImpl.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/restful/EventTraceHistoryRestfulApi.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/restful/JobOperationRestfulApi.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/restful/ServerOperationRestfulApi.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/restful/config/EventTraceDataSourceRestfulApi.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/restful/config/LiteJobConfigRestfulApi.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/restful/config/RegistryCenterRestfulApi.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/service/EventTraceDataSourceConfigurationService.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/service/JobAPIService.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/service/RegistryCenterConfigurationService.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/service/impl/EventTraceDataSourceConfigurationServiceImpl.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/service/impl/JobAPIServiceImpl.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/service/impl/RegistryCenterConfigurationServiceImpl.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/util/HomeFolderUtils.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/util/SessionEventTraceDataSourceConfiguration.java
 create mode 100644 elastic-job-lite-console/src/main/java/io/elasticjob/lite/console/util/SessionRegistryCenterConfiguration.java
 create mode 100644 elastic-job-lite-console/src/main/resources/assembly/assembly.xml
 create mode 100644 elastic-job-lite-console/src/main/resources/bin/start.bat
 create mode 100644 elastic-job-lite-console/src/main/resources/bin/start.sh
 create mode 100644 elastic-job-lite-console/src/main/resources/conf/auth.properties
 create mode 100644 elastic-job-lite-console/src/main/resources/console/css/index.css
 create mode 100644 elastic-job-lite-console/src/main/resources/console/favicon.ico
 create mode 100644 elastic-job-lite-console/src/main/resources/console/html/global/event_trace_data_source.html
 create mode 100644 elastic-job-lite-console/src/main/resources/console/html/global/registry_center.html
 create mode 100644 elastic-job-lite-console/src/main/resources/console/html/help/help.html
 create mode 100644 elastic-job-lite-console/src/main/resources/console/html/history/job_event_trace_history.html
 create mode 100644 elastic-job-lite-console/src/main/resources/console/html/history/job_status_history.html
 create mode 100644 elastic-job-lite-console/src/main/resources/console/html/status/job/job_config.html
 create mode 100644 elastic-job-lite-console/src/main/resources/console/html/status/job/job_status_detail.html
 create mode 100644 elastic-job-lite-console/src/main/resources/console/html/status/job/jobs_status_overview.html
 create mode 100644 elastic-job-lite-console/src/main/resources/console/html/status/server/server_status_detail.html
 create mode 100644 elastic-job-lite-console/src/main/resources/console/html/status/server/servers_status_overview.html
 create mode 100644 elastic-job-lite-console/src/main/resources/console/i18n/message.properties
 create mode 100644 elastic-job-lite-console/src/main/resources/console/i18n/message_en.properties
 create mode 100644 elastic-job-lite-console/src/main/resources/console/i18n/message_zh.properties
 create mode 100644 elastic-job-lite-console/src/main/resources/console/index.html
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/global/event_trace_data_source.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/global/registry_center.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/history/job_event_trace_history.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/history/job_status_history.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/index.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/status/job/job_config.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/status/job/job_status_detail.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/status/job/jobs_status_overview.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/status/server/server_status_detail.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/status/server/servers_status_overview.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/util/common.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/util/dashboard.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/js/util/formatter.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/AdminLTE/css/AdminLTE.min.css
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/AdminLTE/css/skins/_all-skins.min.css
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/AdminLTE/js/app.min.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/BootstrapValidator/js/bootstrapValidator.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/BootstrapValidator/js/bootstrapValidator_zh_CN.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/bootstrap-table/bootstrap-table.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/bootstrap-table/bootstrap-table.min.css
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/bootstrap/css/bootstrap-theme.min.css
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/bootstrap/css/bootstrap.min.css
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/bootstrap/fonts/glyphicons-halflings-regular.eot
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/bootstrap/fonts/glyphicons-halflings-regular.svg
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/bootstrap/fonts/glyphicons-halflings-regular.ttf
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/bootstrap/fonts/glyphicons-halflings-regular.woff
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/bootstrap/fonts/glyphicons-halflings-regular.woff2
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/bootstrap/js/bootstrap.min.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/daterangepicker/daterangepicker.css
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/daterangepicker/daterangepicker.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/daterangepicker/moment.min.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/font-awesome-4.5.0/css/font-awesome.min.css
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/font-awesome-4.5.0/fonts/FontAwesome.otf
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/font-awesome-4.5.0/fonts/fontawesome-webfont.eot
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/font-awesome-4.5.0/fonts/fontawesome-webfont.svg
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/font-awesome-4.5.0/fonts/fontawesome-webfont.ttf
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/font-awesome-4.5.0/fonts/fontawesome-webfont.woff
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/font-awesome-4.5.0/fonts/fontawesome-webfont.woff2
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/input-mask/jquery.inputmask.date.extensions.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/input-mask/jquery.inputmask.extensions.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/input-mask/jquery.inputmask.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/input-mask/jquery.inputmask.numeric.extensions.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/input-mask/jquery.inputmask.phone.extensions.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/input-mask/jquery.inputmask.regex.extensions.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/input-mask/phone-codes/phone-be.json
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/input-mask/phone-codes/phone-codes.json
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/input-mask/phone-codes/readme.txt
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/jquery/jquery-2.1.4.min.js
 create mode 100644 elastic-job-lite-console/src/main/resources/console/lib/jquery/jquery.i18n.properties-min.js
 create mode 100644 elastic-job-lite-console/src/main/resources/logback.xml
 create mode 100644 elastic-job-lite-console/src/test/java/io/elasticjob/lite/console/AllLiteConsoleTests.java
 create mode 100644 elastic-job-lite-console/src/test/java/io/elasticjob/lite/console/util/HomeFolderUtilsTest.java
 create mode 100644 elastic-job-lite-core/pom.xml
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/ElasticJob.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/JobScheduler.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/JobType.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/ShardingContext.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/dataflow/DataflowJob.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/listener/AbstractDistributeOnceElasticJobListener.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/listener/ElasticJobListener.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/script/ScriptJob.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/simple/SimpleJob.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/strategy/JobInstance.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/strategy/JobShardingStrategy.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/strategy/JobShardingStrategyFactory.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/strategy/impl/AverageAllocationJobShardingStrategy.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/strategy/impl/OdevitySortByNameJobShardingStrategy.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/api/strategy/impl/RotateServerByNameJobShardingStrategy.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/config/JobCoreConfiguration.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/config/JobRootConfiguration.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/config/JobTypeConfiguration.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/config/LiteJobConfiguration.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/config/dataflow/DataflowJobConfiguration.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/config/script/ScriptJobConfiguration.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/config/simple/SimpleJobConfiguration.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/context/ExecutionType.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/context/TaskContext.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/JobEvent.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/JobEventBus.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/JobEventConfiguration.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/JobEventIdentity.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/JobEventListener.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/JobEventListenerConfigurationException.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/rdb/DatabaseType.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/rdb/JobEventRdbConfiguration.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/rdb/JobEventRdbIdentity.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/rdb/JobEventRdbListener.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/rdb/JobEventRdbSearch.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/rdb/JobEventRdbStorage.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/type/JobExecutionEvent.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/type/JobExecutionEventThrowable.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/event/type/JobStatusTraceEvent.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/exception/AppConfigurationException.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/exception/ExceptionUtil.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/exception/JobConfigurationException.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/exception/JobExecutionEnvironmentException.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/exception/JobStatisticException.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/exception/JobSystemException.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/AbstractElasticJobExecutor.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/JobExecutorFactory.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/JobFacade.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/ShardingContexts.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/handler/ExecutorServiceHandler.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/handler/ExecutorServiceHandlerRegistry.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/handler/JobExceptionHandler.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/handler/JobProperties.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/handler/impl/DefaultExecutorServiceHandler.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/handler/impl/DefaultJobExceptionHandler.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/type/DataflowJobExecutor.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/type/ScriptJobExecutor.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/executor/type/SimpleJobExecutor.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/config/ConfigurationNode.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/config/ConfigurationService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/config/LiteJobConfigurationConstants.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/config/LiteJobConfigurationGsonFactory.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/config/RescheduleListenerManager.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/election/ElectionListenerManager.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/election/LeaderNode.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/election/LeaderService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/failover/FailoverListenerManager.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/failover/FailoverNode.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/failover/FailoverService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/guarantee/GuaranteeListenerManager.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/guarantee/GuaranteeNode.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/guarantee/GuaranteeService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/instance/InstanceNode.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/instance/InstanceOperation.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/instance/InstanceService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/instance/ShutdownListenerManager.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/instance/TriggerListenerManager.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/listener/AbstractJobListener.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/listener/AbstractListenerManager.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/listener/ListenerManager.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/listener/RegistryCenterConnectionStateListener.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/monitor/MonitorService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/reconcile/ReconcileService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/schedule/JobRegistry.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/schedule/JobScheduleController.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/schedule/JobShutdownHookPlugin.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/schedule/JobTriggerListener.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/schedule/LiteJob.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/schedule/LiteJobFacade.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/schedule/SchedulerFacade.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/server/ServerNode.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/server/ServerService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/server/ServerStatus.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/sharding/ExecutionContextService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/sharding/ExecutionService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/sharding/MonitorExecutionListenerManager.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/sharding/ShardingListenerManager.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/sharding/ShardingNode.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/sharding/ShardingService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/storage/JobNodePath.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/storage/JobNodeStorage.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/storage/LeaderExecutionCallback.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/storage/TransactionExecutionCallback.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/internal/util/SensitiveInfoUtils.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/reg/base/CoordinatorRegistryCenter.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/reg/base/ElectionCandidate.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/reg/base/RegistryCenter.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/reg/exception/RegException.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/reg/exception/RegExceptionHandler.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/reg/zookeeper/ZookeeperConfiguration.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/reg/zookeeper/ZookeeperElectionService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/reg/zookeeper/ZookeeperRegistryCenter.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/statistics/StatisticInterval.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/statistics/rdb/StatisticRdbRepository.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/statistics/type/job/JobExecutionTypeStatistics.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/statistics/type/job/JobRegisterStatistics.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/statistics/type/job/JobRunningStatistics.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/statistics/type/job/JobTypeStatistics.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/statistics/type/task/TaskResultStatistics.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/statistics/type/task/TaskRunningStatistics.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/util/concurrent/BlockUtils.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/util/concurrent/ExecutorServiceObject.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/util/config/ShardingItemParameters.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/util/config/ShardingItems.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/util/digest/Encryption.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/util/env/HostException.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/util/env/IpUtils.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/util/env/TimeService.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/util/json/AbstractJobConfigurationGsonTypeAdapter.java
 create mode 100644 elastic-job-lite-core/src/main/java/io/elasticjob/lite/util/json/GsonFactory.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/AllLiteCoreTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/AllApiTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/JobSchedulerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/ShardingContextTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/listener/DistributeOnceElasticJobListenerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/listener/fixture/ElasticJobListenerCaller.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/listener/fixture/TestDistributeOnceElasticJobListener.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/listener/fixture/TestElasticJobListener.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/strategy/AllStrategyTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/strategy/JobInstanceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/strategy/JobShardingStrategyFactoryTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/strategy/fixture/InvalidJobShardingStrategy.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/strategy/impl/AverageAllocationJobShardingStrategyTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/strategy/impl/OdevitySortByNameJobShardingStrategyTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/api/strategy/impl/RotateServerByNameJobShardingStrategyTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/config/AllConfigTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/config/JobCoreConfigurationTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/config/LiteJobConfigurationTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/context/AllContextTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/context/TaskContextTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/AllEventTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/JobEventBusTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/JobExecutionEventTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/fixture/JobEventCaller.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/fixture/TestJobEventConfiguration.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/fixture/TestJobEventFailureConfiguration.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/fixture/TestJobEventIdentity.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/fixture/TestJobEventListener.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/rdb/JobEventRdbConfigurationTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/rdb/JobEventRdbIdentityTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/rdb/JobEventRdbListenerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/rdb/JobEventRdbSearchTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/event/rdb/JobEventRdbStorageTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/exception/AllExceptionTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/exception/ExceptionUtilTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/exception/JobConfigurationExceptionTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/exception/JobExecutionEnvironmentExceptionTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/exception/JobStatisticExceptionTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/exception/JobSystemExceptionTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/executor/AllExecutorTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/executor/JobExecutorFactoryTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/executor/handler/ExecutorServiceHandlerRegistryTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/executor/handler/JobPropertiesTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/executor/handler/impl/DefaultJobExceptionHandlerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/executor/type/DataflowJobExecutorTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/executor/type/ElasticJobVerify.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/executor/type/ScriptJobExecutorTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/executor/type/SimpleJobExecutorTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/executor/type/WrongJobExecutorTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/APIJsonConstants.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/EmbedTestingServer.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/LiteJsonConstants.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/ShardingContextsBuilder.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/TestDataflowJob.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/TestSimpleJob.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/config/TestDataflowJobConfiguration.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/config/TestJobRootConfiguration.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/config/TestScriptJobConfiguration.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/config/TestSimpleJobConfiguration.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/context/TaskNode.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/handler/IgnoreJobExceptionHandler.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/handler/ThrowJobExceptionHandler.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/job/JobCaller.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/job/OtherJob.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/job/TestDataflowJob.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/job/TestSimpleJob.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/job/TestWrongJob.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/util/JobConfigurationUtil.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/fixture/util/ScriptElasticJobUtil.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/AbstractBaseStdJobAutoInitTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/AbstractBaseStdJobTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/AllIntegrateTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/WaitingUtils.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/fixture/IgnoreJobExceptionHandler.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/fixture/dataflow/OneOffDataflowElasticJob.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/fixture/dataflow/StreamingDataflowElasticJob.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/fixture/dataflow/StreamingDataflowElasticJobForExecuteFailure.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/fixture/dataflow/StreamingDataflowElasticJobForExecuteThrowsException.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/fixture/simple/FooSimpleElasticJob.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/std/dataflow/OneOffDataflowElasticJobTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/std/dataflow/StreamingDataflowElasticJobForExecuteFailureTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/std/dataflow/StreamingDataflowElasticJobForExecuteThrowsExceptionTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/std/dataflow/StreamingDataflowElasticJobForMultipleThreadsTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/std/dataflow/StreamingDataflowElasticJobForNotMonitorTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/std/dataflow/StreamingDataflowElasticJobTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/std/script/ScriptElasticJobTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/std/simple/DisabledJobTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/integrate/std/simple/SimpleElasticJobTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/AllInternalTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/config/ConfigurationNodeTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/config/ConfigurationServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/config/LiteJobConfigurationGsonFactoryTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/config/RescheduleListenerManagerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/election/ElectionListenerManagerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/election/LeaderNodeTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/election/LeaderServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/failover/FailoverListenerManagerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/failover/FailoverNodeTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/failover/FailoverServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/guarantee/GuaranteeListenerManagerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/guarantee/GuaranteeNodeTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/guarantee/GuaranteeServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/instance/InstanceNodeTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/instance/InstanceServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/instance/ShutdownListenerManagerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/instance/TriggerListenerManagerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/listener/JobListenerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/listener/ListenerManagerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/listener/RegistryCenterConnectionStateListenerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/listener/fixture/FooJobListener.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/monitor/MonitorServiceDisableTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/monitor/MonitorServiceEnableTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/monitor/SocketUtils.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/reconcile/ReconcileServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/schedule/JobRegistryTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/schedule/JobScheduleControllerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/schedule/JobTriggerListenerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/schedule/LiteJobFacadeTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/schedule/SchedulerFacadeTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/server/ServerNodeTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/server/ServerServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/sharding/ExecutionContextServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/sharding/ExecutionServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/sharding/MonitorExecutionListenerManagerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/sharding/ShardingListenerManagerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/sharding/ShardingNodeTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/sharding/ShardingServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/storage/JobNodePathTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/storage/JobNodeStorageTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/internal/util/SensitiveInfoUtilsTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/reg/AllRegTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/reg/exception/RegExceptionHandlerTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/reg/zookeeper/ZookeeperConfigurationTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/reg/zookeeper/ZookeeperElectionServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/reg/zookeeper/ZookeeperRegistryCenterForAuthTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/reg/zookeeper/ZookeeperRegistryCenterInitFailureTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/reg/zookeeper/ZookeeperRegistryCenterMiscellaneousTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/reg/zookeeper/ZookeeperRegistryCenterModifyTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/reg/zookeeper/ZookeeperRegistryCenterQueryWithCacheTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/reg/zookeeper/ZookeeperRegistryCenterQueryWithoutCacheTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/reg/zookeeper/util/ZookeeperRegistryCenterTestUtil.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/statistics/AllStatisticsTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/statistics/rdb/StatisticRdbRepositoryTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/util/AllUtilTests.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/util/concurrent/ExecutorServiceObjectTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/util/config/ShardingItemParametersTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/util/config/ShardingItemsTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/util/digest/EncryptionTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/util/env/HostExceptionTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/util/env/IpUtilsTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/util/env/TimeServiceTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/util/json/GsonFactoryTest.java
 create mode 100644 elastic-job-lite-core/src/test/java/io/elasticjob/lite/util/json/JobConfigurationGsonTypeAdapterTest.java
 create mode 100644 elastic-job-lite-core/src/test/resources/conf.reg/local.properties
 create mode 100644 elastic-job-lite-core/src/test/resources/conf.reg/local_overwrite.properties
 create mode 100644 elastic-job-lite-core/src/test/resources/logback-test.xml
 create mode 100644 elastic-job-lite-core/src/test/resources/script/test.bat
 create mode 100755 elastic-job-lite-core/src/test/resources/script/test.sh
 create mode 100644 elastic-job-lite-lifecycle/pom.xml
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/api/JobAPIFactory.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/api/JobOperateAPI.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/api/JobSettingsAPI.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/api/JobStatisticsAPI.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/api/ServerStatisticsAPI.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/api/ShardingOperateAPI.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/api/ShardingStatisticsAPI.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/domain/JobBriefInfo.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/domain/JobSettings.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/domain/ServerBriefInfo.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/domain/ShardingInfo.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/internal/operate/JobOperateAPIImpl.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/internal/operate/ShardingOperateAPIImpl.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/internal/reg/RegistryCenterFactory.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/internal/settings/JobSettingsAPIImpl.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/internal/statistics/JobStatisticsAPIImpl.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/internal/statistics/ServerStatisticsAPIImpl.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/internal/statistics/ShardingStatisticsAPIImpl.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/restful/GsonJsonProvider.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/restful/RestfulException.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/restful/RestfulExceptionMapper.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/restful/RestfulServer.java
 create mode 100644 elastic-job-lite-lifecycle/src/main/java/io/elasticjob/lite/lifecycle/security/WwwAuthFilter.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/AbstractEmbedZookeeperBaseTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/AllLiteLifecycleTests.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/api/JobAPIFactoryTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/domain/ShardingStatusTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/fixture/LifecycleJsonConstants.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/fixture/TestDataflowJob.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/fixture/TestSimpleJob.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/internal/operate/JobOperateAPIImplTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/internal/operate/ShardingOperateAPIImplTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/internal/reg/RegistryCenterFactoryTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/internal/settings/JobSettingsAPIImplTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/internal/statistics/JobStatisticsAPIImplTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/internal/statistics/ServerStatisticsAPIImplTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/internal/statistics/ShardingStatisticsAPIImplTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/restful/AllRestfulTests.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/restful/RestfulExceptionTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/restful/RestfulServerTest.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/restful/fixture/Caller.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/restful/fixture/TestFilter.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/java/io/elasticjob/lite/lifecycle/restful/fixture/TestRestfulApi.java
 create mode 100644 elastic-job-lite-lifecycle/src/test/resources/logback-test.xml
 create mode 100644 elastic-job-lite-spring/pom.xml
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/api/SpringJobScheduler.java
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/job/handler/JobNamespaceHandler.java
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/job/parser/common/AbstractJobBeanDefinitionParser.java
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/job/parser/common/BaseJobBeanDefinitionParserTag.java
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/job/parser/dataflow/DataflowJobBeanDefinitionParser.java
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/job/parser/dataflow/DataflowJobBeanDefinitionParserTag.java
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/job/parser/script/ScriptJobBeanDefinitionParser.java
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/job/parser/script/ScriptJobBeanDefinitionParserTag.java
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/job/parser/simple/SimpleJobBeanDefinitionParser.java
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/job/util/AopTargetUtils.java
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/reg/handler/RegNamespaceHandler.java
 create mode 100644 elastic-job-lite-spring/src/main/java/io/elasticjob/lite/spring/reg/parser/ZookeeperBeanDefinitionParser.java
 create mode 100644 elastic-job-lite-spring/src/main/resources/META-INF/namespace/job.xsd
 create mode 100644 elastic-job-lite-spring/src/main/resources/META-INF/namespace/reg.xsd
 create mode 100644 elastic-job-lite-spring/src/main/resources/META-INF/spring.handlers
 create mode 100644 elastic-job-lite-spring/src/main/resources/META-INF/spring.schemas
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/AllLiteSpringTests.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/aspect/SimpleAspect.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/handler/SimpleExecutorServiceHandler.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/handler/SimpleJobExceptionHandler.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/job/DataflowElasticJob.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/job/FooSimpleElasticJob.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/job/ref/RefFooDataflowElasticJob.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/job/ref/RefFooSimpleElasticJob.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/listener/SimpleCglibListener.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/listener/SimpleJdkDynamicProxyListener.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/listener/SimpleListener.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/listener/SimpleOnceListener.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/service/FooService.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/fixture/service/FooServiceImpl.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/job/AbstractJobSpringIntegrateTest.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/job/AllSpringIntegrateTests.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/job/JobSpringNamespaceWithEventTraceRdbTest.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/job/JobSpringNamespaceWithJobPropertiesTest.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/job/JobSpringNamespaceWithListenerAndCglibTest.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/job/JobSpringNamespaceWithListenerAndJdkDynamicProxyTest.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/job/JobSpringNamespaceWithListenerTest.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/job/JobSpringNamespaceWithRefTest.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/job/JobSpringNamespaceWithoutListenerTest.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/test/AbstractZookeeperJUnit4SpringContextTests.java
 create mode 100644 elastic-job-lite-spring/src/test/java/io/elasticjob/lite/spring/test/EmbedZookeeperTestExecutionListener.java
 create mode 100644 elastic-job-lite-spring/src/test/resources/META-INF/job/base.xml
 create mode 100644 elastic-job-lite-spring/src/test/resources/META-INF/job/withEventTraceRdb.xml
 create mode 100644 elastic-job-lite-spring/src/test/resources/META-INF/job/withJobProperties.xml
 create mode 100644 elastic-job-lite-spring/src/test/resources/META-INF/job/withJobRef.xml
 create mode 100644 elastic-job-lite-spring/src/test/resources/META-INF/job/withListener.xml
 create mode 100644 elastic-job-lite-spring/src/test/resources/META-INF/job/withListenerAndCglib.xml
 create mode 100644 elastic-job-lite-spring/src/test/resources/META-INF/job/withListenerAndJdkDynamicProxy.xml
 create mode 100644 elastic-job-lite-spring/src/test/resources/META-INF/job/withoutListener.xml
 create mode 100644 elastic-job-lite-spring/src/test/resources/META-INF/reg/regContext.xml
 create mode 100644 elastic-job-lite-spring/src/test/resources/conf/job/conf.properties
 create mode 100644 elastic-job-lite-spring/src/test/resources/conf/reg/conf.properties
 create mode 100644 elastic-job-lite-spring/src/test/resources/logback-test.xml
 create mode 100644 pom.xml
 create mode 100644 src/main/resources/dd_checks.xml
 create mode 100644 src/main/resources/dd_pmd.xml
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ git push -u origin master
Counting objects: 769, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (706/706), done.
Writing objects: 100% (769/769), 1.12 MiB | 5.00 KiB/s, done.
Total 769 (delta 280), reused 0 (delta 0)








remote: Resolving deltas: 100% (280/280), done.
To https://github.com/BoulCheng/elastic-job-lite-millis.git
   939bf49..afec9d1  master -> master
Branch 'master' set up to track remote branch 'master' from 'origin'.
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 
zhenglubiaodeMacBook-Pro:elastic-job-lite zlb$ 



==1
zhenglubiaodeMacBook-Pro:fota-margin zlb$ git remote set-url origin http://172.16.50.209/fota/fota-margin
http://172.16.50.209/fota/fota-margin.git
git remote set-url origin http://172.16.50.209/fota/fota-margin



==2
具体步骤如下：

前提：在github上手动创建仓库gitRepo。（并且存在其他文件如README.md）

在本地按照如下的命令进行

1、 mkdir gitRepo #如果是已存在的工程项目，则直接cd到项目根目录下，不需要新建。

2、 cd gitRepo

3、 git init #初始化本地仓库

4、 git add xxx #添加要push到远程仓库的文件或文件夹

5、 git commit -m ‘first commit’

6、 git remote add origin https://github.com/yourgithubID/gitRepo.git #建立远程仓库

7、 git push -u origin master #将本地仓库push到远程仓库

需要注意的是：一定要在github上手动创建仓库gitRepo，否则会出现如下的错误。
解决方法为：
第一步：可以通过如下命令进行代码合并【注：pull=fetch+merge]
git pull --rebase origin master
执行上面代码后可以看到本地代码库中多了REAroDME.md文件
第二步：此时再执行语句 git push -u origin master即可完成代码上传到github
小结
以上就是在本地创建新的仓库并上传到远程仓库的相关命令操作。

===3
前提：在github上手动创建仓库jdk （并且不存在任何其他文件）

echo "# jdk" >> README.md
git init
git add README.md
git commit -m "first commit"
git remote add origin https://github.com/BoulCheng/jdk.git
git push -u origin master

////
zhenglubiaodeMacBook-Pro:~ zlb$ cd /Users/zlb/IdeaProjects/lb/JVM-S 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ git init
Initialized empty Git repository in /Users/zlb/IdeaProjects/lb/JVM-S/.git/
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ git add src/
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ git add pom.xml
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ git commit -m 'first commit'
[master (root-commit) 1279135] first commit
 Committer: zhenglubiao <zlb@zhenglubiaodeMacBook-Pro.local>
Your name and email address were configured automatically based
on your username and hostname. Please check that they are accurate.
You can suppress this message by setting them explicitly:

    git config --global user.name "Your Name"
    git config --global user.email you@example.com

After doing this, you may fix the identity used for this commit with:

    git commit --amend --reset-author

 3 files changed, 108 insertions(+)
 create mode 100644 pom.xml
 create mode 100644 src/main/java/com/zlb/App.java
 create mode 100644 src/test/java/com/zlb/AppTest.java
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ git config --global user.name "BoulCheng"
zhenglubiaodeMacBook-Pro:JVM-S zlb$ git config --global user.email 1241292849@qq.com
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ git commit --amend --reset-author
[master 5cb17b9] Initial commit
 3 files changed, 108 insertions(+)
 create mode 100644 pom.xml
 create mode 100644 src/main/java/com/zlb/App.java
 create mode 100644 src/test/java/com/zlb/AppTest.java
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ git remote add origin https://github.com/BoulCheng/JVM-S.git
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
zhenglubiaodeMacBook-Pro:JVM-S zlb$ git push -u origin master
Username for 'https://github.com': BoulCheng
Password for 'https://BoulCheng@github.com': 
Counting objects: 14, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (6/6), done.
Writing objects: 100% (14/14), 1.67 KiB | 855.00 KiB/s, done.
Total 14 (delta 0), reused 0 (delta 0)
To https://github.com/BoulCheng/JVM-S.git
 * [new branch]      master -> master
Branch 'master' set up to track remote branch 'master' from 'origin'.
zhenglubiaodeMacBook-Pro:JVM-S zlb$ 
/////


zhenglubiaodeMacBook-Pro:test13 zlb$
zhenglubiaodeMacBook-Pro:test13 zlb$ cd /Users/zlb/IdeaProjects/six/jdk
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$ ls -l
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$ git init
Initialized empty Git repository in /Users/zlb/IdeaProjects/six/jdk/.git/
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$ echo "# jdk" >> README.md
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$ echo "# jdk" >> README.md
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$ git add
.git/      README.md
zhenglubiaodeMacBook-Pro:jdk zlb$ git add README.md
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$ git commit -m "first commit"
[master (root-commit) 96d42fb] first commit
 Committer: zhenglubiao <zlb@zhenglubiaodeMacBook-Pro.local>
Your name and email address were configured automatically based
on your username and hostname. Please check that they are accurate.
You can suppress this message by setting them explicitly:

    git config --global user.name "Your Name"
    git config --global user.email you@example.com

After doing this, you may fix the identity used for this commit with:

    git commit --amend --reset-author

 1 file changed, 2 insertions(+)
 create mode 100644 README.md
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$ git remote add origin https://github.com/BoulCheng/jdk.git
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$ git push -u origin master

Counting objects: 3, done.
Writing objects: 100% (3/3), 236 bytes | 236.00 KiB/s, done.
Total 3 (delta 0), reused 0 (delta 0)
To https://github.com/BoulCheng/jdk.git
 * [new branch]      master -> master
Branch 'master' set up to track remote branch 'master' from 'origin'.
zhenglubiaodeMacBook-Pro:jdk zlb$
zhenglubiaodeMacBook-Pro:jdk zlb$
