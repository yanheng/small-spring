package com.bugai.springframework;


import com.alibaba.druid.pool.DruidDataSource;
import com.bugai.springframework.aop.AdvisedSupport;
import com.bugai.springframework.aop.TargetSource;
import com.bugai.springframework.aop.framework.Cglib2AopProxy;
import com.bugai.springframework.context.support.ClassPathXmlApplicationContext;
import com.bugai.springframework.jdbc.datasource.DataSourceTransactionManager;
import com.bugai.springframework.jdbc.suport.JdbcTemplate;
import com.bugai.springframework.test.JdbcService;
import com.bugai.springframework.test.impl.JdbcServiceImpl;
import com.bugai.springframework.tx.transaction.annotation.AnnotationTransactionAttributeSource;
import com.bugai.springframework.tx.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;
import com.bugai.springframework.tx.transaction.interceptor.TransactionAttribute;
import com.bugai.springframework.tx.transaction.interceptor.TransactionInterceptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.sql.DataSource;
import java.lang.reflect.Method;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiTest {

  private JdbcTemplate jdbcTemplate;
  private DataSource dataSource;

  @BeforeAll
  public void init() {
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
    jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
    dataSource = applicationContext.getBean(DruidDataSource.class);
  }

  @Test
  public void matchTransactionAnnotationTest() {
    JdbcService jdbcService = new JdbcServiceImpl();
    AnnotationTransactionAttributeSource transactionAttributeSource = new AnnotationTransactionAttributeSource();
    Method[] methods = jdbcService.getClass().getMethods();
    Method targetMethod = null;
    for (Method method : methods) {
      if (method.getName().equals("saveData")) {
        targetMethod = method;
        break;
      }
    }
    TransactionAttribute transactionAttribute = transactionAttributeSource.getTransactionAttribute(targetMethod, jdbcService.getClass());
    System.out.println(transactionAttribute.getName());
  }

  @Test
  public void jdbcWithTransaction() {
    JdbcService jdbcService = new JdbcServiceImpl();

    AnnotationTransactionAttributeSource transactionAttributeSource = new AnnotationTransactionAttributeSource();
    transactionAttributeSource.findTransactionAttribute(jdbcService.getClass());

    DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
    TransactionInterceptor interceptor = new TransactionInterceptor(transactionManager, transactionAttributeSource);

    BeanFactoryTransactionAttributeSourceAdvisor btas = new BeanFactoryTransactionAttributeSourceAdvisor();

    btas.setAdvice(interceptor);


    AdvisedSupport advisedSupport = new AdvisedSupport();
    advisedSupport.setTargetSource(new TargetSource(jdbcService));
    advisedSupport.setMethodInterceptor(interceptor);
    advisedSupport.setMethodMatcher(btas.getPointcut().getMethodMatcher());
    advisedSupport.setProxyTargetClass(false);

    JdbcService proxyCglib = (JdbcServiceImpl) new Cglib2AopProxy(advisedSupport).getProxy();


    proxyCglib.saveData(jdbcTemplate);
  }
}
