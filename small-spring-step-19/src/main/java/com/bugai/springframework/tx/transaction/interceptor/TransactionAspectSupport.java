package com.bugai.springframework.tx.transaction.interceptor;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.threadlocal.NamedThreadLocal;
import com.bugai.springframework.beans.BeansException;
import com.bugai.springframework.beans.factory.BeanFactory;
import com.bugai.springframework.beans.factory.BeanFactoryAware;
import com.bugai.springframework.beans.factory.support.InitializingBean;
import com.bugai.springframework.tx.transaction.PlatformTransactionManager;
import com.bugai.springframework.tx.transaction.TransactionStatus;
import com.bugai.springframework.utils.ClassUtils;

import java.lang.reflect.Method;

public class TransactionAspectSupport implements BeanFactoryAware, InitializingBean {

  private static final ThreadLocal<TransactionInfo> transactionInfoHolder =
          new NamedThreadLocal<>("Current aspect-driven transaction");

  private BeanFactory beanFactory;

  private TransactionAttributeSource transactionAttributeSource;

  private PlatformTransactionManager transactionManager;


  protected Object invokeWithinTransaction(Method method, Class<?> targetClass, InvocationCallback invocation) throws Throwable {
    TransactionAttributeSource tas = getTransactionAttributeSource();
    TransactionAttribute txAttr = tas != null ? tas.getTransactionAttribute(method, targetClass) : null;

    PlatformTransactionManager tm = determineTransactionManager();
    String joinpointIdentification = methodIdentification(method, targetClass);
    TransactionInfo txInfo = createTransactionIfNecessary(tm, txAttr, joinpointIdentification);

    Object retVal = null;
    try {
      retVal = invocation.proceedWithInvocation();
    } catch (Throwable e) {
      completeTransactionAfterThrowing(txInfo, e);
      throw e;
    } finally {
      cleanupTransactionInfo(txInfo);
    }
    commitTransactionAfterReturning(txInfo);
    return retVal;
  }


  public TransactionAttributeSource getTransactionAttributeSource() {
    return transactionAttributeSource;
  }

  public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource) {
    this.transactionAttributeSource = transactionAttributeSource;
  }

  public PlatformTransactionManager getTransactionManager() {
    return transactionManager;
  }

  public void setTransactionManager(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  /**
   * 当前使用DataSourceTransactionManager
   */
  protected PlatformTransactionManager determineTransactionManager() {
    return getTransactionManager();
  }

  /**
   * 获取目标方法的唯一标识
   */
  private String methodIdentification(Method method, Class<?> targetClass) {
    return ClassUtils.getQualifiedMethodName(method, targetClass);
  }

  /**
   * 必要时创建事务
   *
   * @param tm                      事务管理器
   * @param txAttr                  事务属性
   * @param joinpointIdentification 事务作用的方法
   * @return
   */
  protected TransactionInfo createTransactionIfNecessary(PlatformTransactionManager tm, TransactionAttribute txAttr,
                                                         String joinpointIdentification) {
    if (txAttr != null && txAttr.getName() == null) {
      txAttr = new DelegatingTransactionAttribute(txAttr) {
        @Override
        public String getName() {
          return joinpointIdentification;
        }
      };
    }
    TransactionStatus status = null;
    if (txAttr != null) {
      if (tm != null) {
        status = tm.getTransaction(txAttr);
      }
    }

    return prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
  }

  protected TransactionInfo prepareTransactionInfo(PlatformTransactionManager tm, TransactionAttribute txAttr, String joinpointIdentification, TransactionStatus status) {
    TransactionInfo txInfo = new TransactionInfo(tm, txAttr, joinpointIdentification);
    if (txAttr != null) {
      txInfo.newTransactionStatus(status);
    }
    txInfo.bindToThread();
    return txInfo;
  }

  protected void completeTransactionAfterThrowing(TransactionInfo txInfo, Throwable ex) {
    if (null != txInfo && null != txInfo.getTransactionStatus()) {
      if (txInfo.transactionAttribute != null && txInfo.transactionAttribute.rollbackOn(ex)) {
        try {
          txInfo.getPlatformTransactionManager().rollback(txInfo.transactionStatus);
        } catch (RuntimeException | Error ex2) {
          throw ex2;
        }
      } else {
        try {
          txInfo.getPlatformTransactionManager().commit(txInfo.transactionStatus);
        } catch (RuntimeException | Error ex2) {
          throw ex2;
        }
      }
    }
  }

  protected void cleanupTransactionInfo(TransactionInfo txInfo) {
    if (null != txInfo) {
      txInfo.restoreThreadLocalStatus();
    }
  }


  protected void commitTransactionAfterReturning(TransactionInfo txInfo) {
    if (txInfo != null && null != txInfo.transactionStatus) {
      txInfo.getPlatformTransactionManager().commit(txInfo.transactionStatus);
    }
  }

  protected interface InvocationCallback {
    Object proceedWithInvocation() throws Throwable;
  }


  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  @Override
  public void afterPropertySet() throws Exception {

  }

  protected final class TransactionInfo {
    private final PlatformTransactionManager transactionManager;
    private final TransactionAttribute transactionAttribute;
    private final String joinpointIdentification;
    private TransactionStatus transactionStatus;
    private TransactionInfo oldTransactionInfo;

    public TransactionInfo(PlatformTransactionManager platformTransactionManager, TransactionAttribute transactionAttribute, String joinpointIdentification) {
      this.transactionManager = platformTransactionManager;
      this.transactionAttribute = transactionAttribute;
      this.joinpointIdentification = joinpointIdentification;
    }

    public PlatformTransactionManager getPlatformTransactionManager() {
      Assert.state(this.transactionManager != null, "No PlatformTransactionManager set");
      return transactionManager;
    }

    public String getJoinpointIdentification() {
      return joinpointIdentification;
    }

    public TransactionAttribute getTransactionAttribute() {
      return transactionAttribute;
    }

    public void newTransactionStatus(TransactionStatus status) {
      this.transactionStatus = status;
    }

    public TransactionStatus getTransactionStatus() {
      return this.transactionStatus;
    }

    public boolean hasTransaction() {
      return null != this.transactionStatus;
    }

    private void bindToThread() {
      TransactionInfo transactionInfo = transactionInfoHolder.get();
      transactionInfoHolder.set(this);
    }

    private void restoreThreadLocalStatus() {
      transactionInfoHolder.set(this.oldTransactionInfo);
    }
  }
}
