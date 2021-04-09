
#### AOP的BeanPostProcessor
3个AOP相关的BeanPostProcessor
容器中只会注册以下三个中的一个 且无论注册哪个beanName 都是 "org.springframework.aop.config.internalAutoProxyCreator"
当重复注册 AOP相关的BeanPostProcessor 会注册优先级最高的那个 APC_PRIORITY_LIST数组索引越大优先级越高

当应用使用了切面，那容器中一定是 AnnotationAwareAspectJAutoProxyCreator
- AopConfigUtils
```
	static {
		// Set up the escalation list...
		APC_PRIORITY_LIST.add(InfrastructureAdvisorAutoProxyCreator.class); //事务
		APC_PRIORITY_LIST.add(AspectJAwareAdvisorAutoProxyCreator.class); //切面
		APC_PRIORITY_LIST.add(AnnotationAwareAspectJAutoProxyCreator.class); //注解切面
	}
```
	
#### Advisor链
Advisor链的排序: 
order的值越小，优先级越高
AnnotationAwareAspectJAutoProxyCreator#sortAdvisors(List)
BeanFactoryTransactionAttributeSourceAdvisor -> TransactionInterceptor 使用默认排序值即int最大值
InstantiationModelAwarePointcutAdvisor ->  AspectJAroundAdvice 使用默认或者在切面(@Aspect)自定义order值
1.切面(@Aspect)的未自定义order排序值: AnnotationAwareAspectJAutoProxyCreator#findCandidateAdvisors()总是先调用父类方法即AbstractAdvisorAutoProxyCreator#findCandidateAdvisors()获得BeanFactoryTransactionAttributeSourceAdvisor再调用AnnotationAwareAspectJAutoProxyCreator#findCandidateAdvisors()获得InstantiationModelAwarePointcutAdvisor，因此BeanFactoryTransactionAttributeSourceAdvisor总是排在InstantiationModelAwarePointcutAdvisor前面，排序并不会在相同order值时对它们产生的顺序性的改变即排序是稳定的
事务开始 -> 切面开始 -> 被代理方法 -> 切面结束 -> 事务结束

2.切面(@Aspect)自定义order排序值
那么按各自order值排序，BeanFactoryTransactionAttributeSourceAdvisor可能排序在InstantiationModelAwarePointcutAdvisor后面，此时事务并不会包裹该切面，事务在一个更小的范围被执行，该切面不会对事务产生任何影响
切面指定了order 且 > {@link Integer#MAX_VALUE}:
那么执行顺序为 切面开始 -> 事务开始 -> 被代理方法 -> 事务结束 -> 切面结束

#### MethodInterceptor链
Advisor链 最终转化为 MethodInterceptor链 
责任链模式处理 切面(可能多个)和事务