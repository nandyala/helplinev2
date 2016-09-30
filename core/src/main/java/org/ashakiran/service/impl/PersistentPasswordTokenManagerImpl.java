/**
 *
 */
package org.ashakiran.service.impl;

import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;

import org.ashakiran.model.User;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;


/**
 * Provides {@link PasswordTokenManager} functionality generating and persisting random tokens to the db as an extra
 * security check.
 *
 * <p>You will need to create a db table with the following structure:</p>
 *
 * <pre>
   <code>
   create table password_reset_token (
       username varchar(50) NOT NULL,
       token varchar(255) NOT NULL,
       expiration_time timestamp NOT NULL,
       PRIMARY KEY (username, token)
   )
   </code>
 * </pre>
 *
 * <p>and configure this alternative PasswordTokenManager in the spring BeanFactory.</p>
 *
 * @author   ivangsa
 * @version  $Revision$, $Date$
 */
public class PersistentPasswordTokenManagerImpl implements PasswordTokenManager {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private String deleteTokenSql = "delete from password_reset_token where username=?";
  private String insertTokenSql =
    "insert into password_reset_token (username, token, expiration_time) values (?, ?, ?)";

  private JdbcTemplate jdbcTemplate;
  private String       selectTokenSql =
    "select count(token) from password_reset_token where username=? and token=? and expiration_time > NOW()";

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * @see  org.ashakiran.service.impl.PasswordTokenManager#generateRecoveryToken(org.ashakiran.model.User)
   */
  @Override public String generateRecoveryToken(final User user) {
    int    length = RandomUtils.nextInt(16) + 16;
    String token  = RandomStringUtils.randomAlphanumeric(length);
    persistToken(user, token);

    return token;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.ashakiran.service.impl.PasswordTokenManager#invalidateRecoveryToken(User, String)
   */
  @Override public void invalidateRecoveryToken(User user, String token) {
    jdbcTemplate.update(deleteTokenSql, user.getUsername());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * @see  org.ashakiran.service.impl.PasswordTokenManager#isRecoveryTokenValid(org.ashakiran.model.User, java.lang.String)
   */
  @Override public boolean isRecoveryTokenValid(final User user, final String token) {
    return isRecoveryTokenPersisted(user, token);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  dataSource  DOCUMENT ME!
   */
  @Autowired public void setDataSource(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  deleteTokenSql  DOCUMENT ME!
   */
  public void setDeleteTokenSql(String deleteTokenSql) {
    this.deleteTokenSql = deleteTokenSql;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  insertTokenSql  DOCUMENT ME!
   */
  public void setInsertTokenSql(String insertTokenSql) {
    this.insertTokenSql = insertTokenSql;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  selectTokenSql  DOCUMENT ME!
   */
  public void setSelectTokenSql(String selectTokenSql) {
    this.selectTokenSql = selectTokenSql;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param   user   DOCUMENT ME!
   * @param   token  DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  protected boolean isRecoveryTokenPersisted(final User user, final String token) {
    Number count = jdbcTemplate.queryForObject(
        selectTokenSql,
        new Object[] { user.getUsername(), token }, Integer.class);

    return (count != null) && (count.intValue() == 1);
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  user   DOCUMENT ME!
   * @param  token  DOCUMENT ME!
   */
  protected void persistToken(User user, String token) {
    jdbcTemplate.update(deleteTokenSql, user.getUsername());
    jdbcTemplate.update(insertTokenSql, user.getUsername(), token, getExpirationTime());
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Return tokens expiration time, now + 1 day.
   *
   * @return  return tokens expiration time, now + 1 day.
   */
  private Date getExpirationTime() {
    return DateUtils.addDays(new Date(), 1);
  }
} // end class PersistentPasswordTokenManagerImpl
