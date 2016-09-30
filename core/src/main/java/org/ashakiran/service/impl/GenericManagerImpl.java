package org.ashakiran.service.impl;

import java.io.Serializable;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.ashakiran.dao.GenericDao;

import org.ashakiran.service.GenericManager;


/**
 * This class serves as the Base class for all other Managers - namely to hold common CRUD methods that they might all
 * use. You should only need to extend this class when your require custom CRUD logic.
 *
 * <p>
 * </p>
 *
 * <p>To register this class in your Spring context file, use the following XML.</p>
 *
 * <pre>
       &lt;bean id="userManager" class="org.ashakiran.service.impl.GenericManagerImpl"&gt;
           &lt;constructor-arg&gt;
               &lt;bean class="org.ashakiran.dao.hibernate.GenericDaoHibernate"&gt;
                   &lt;constructor-arg value="org.ashakiran.model.User"/&gt;
                   &lt;property name="sessionFactory" ref="sessionFactory"/&gt;
               &lt;/bean&gt;
           &lt;/constructor-arg&gt;
       &lt;/bean&gt;
 * </pre>
 *
 * <p>
 * </p>
 *
 * <p>If you're using iBATIS instead of Hibernate, use:</p>
 *
 * <pre>
       &lt;bean id="userManager" class="org.ashakiran.service.impl.GenericManagerImpl"&gt;
           &lt;constructor-arg&gt;
               &lt;bean class="org.ashakiran.dao.ibatis.GenericDaoiBatis"&gt;
                   &lt;constructor-arg value="org.ashakiran.model.User"/&gt;
                   &lt;property name="dataSource" ref="dataSource"/&gt;
                   &lt;property name="sqlMapClient" ref="sqlMapClient"/&gt;
               &lt;/bean&gt;
           &lt;/constructor-arg&gt;
       &lt;/bean&gt;
 * </pre>
 *
 * @param    <T>   a type variable
 * @param    <PK>  the primary key for that type
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Updated by jgarcia: added full text search +
 *           reindexing
 * @version  $Revision$, $Date$
 */
public class GenericManagerImpl<T, PK extends Serializable> implements GenericManager<T, PK> {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  /** GenericDao instance, set by constructor of child classes. */
  protected GenericDao<T, PK> dao;

  /** Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging */
  protected final Log log = LogFactory.getLog(getClass());

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new GenericManagerImpl object.
   */
  public GenericManagerImpl() { }

  /**
   * Creates a new GenericManagerImpl object.
   *
   * @param  genericDao  DOCUMENT ME!
   */
  public GenericManagerImpl(GenericDao<T, PK> genericDao) {
    this.dao = genericDao;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public boolean exists(PK id) {
    return dao.exists(id);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public T get(PK id) {
    return dao.get(id);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public List<T> getAll() {
    return dao.getAll();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public void reindex() {
    dao.reindex();
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public void reindexAll(boolean async) {
    dao.reindexAll(async);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public void remove(T object) {
    dao.remove(object);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public void remove(PK id) {
    dao.remove(id);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   */
  public T save(T object) {
    return dao.save(object);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * {@inheritDoc}
   *
   * <p>Search implementation using Hibernate Search.</p>
   */
  @SuppressWarnings("unchecked")
  public List<T> search(String q, Class clazz) {
    if ((q == null) || "".equals(q.trim())) {
      return getAll();
    }

    return dao.search(q);
  }
} // end class GenericManagerImpl
