package org.ashakiran.webapp.controller;


/**
 * Command class to handle uploading of a file.
 *
 * <p><a href="FileUpload.java.html"><i>View Source</i></a></p>
 *
 * @author   <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @version  $Revision$, $Date$
 */
public class FileUpload {
  //~ Instance fields --------------------------------------------------------------------------------------------------

  private byte[] file;
  private String name;

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @return  DOCUMENT ME!
   */
  public byte[] getFile() {
    return file;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * Returns the name.
   *
   * @return  Returns the name.
   */
  public String getName() {
    return name;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * DOCUMENT ME!
   *
   * @param  file  DOCUMENT ME!
   */
  public void setFile(byte[] file) {
    this.file = file;
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   *
   * DOCUMENT ME!
   *
   * @param  name  The name to set.
   */
  public void setName(String name) {
    this.name = name;
  }
} // end class FileUpload
