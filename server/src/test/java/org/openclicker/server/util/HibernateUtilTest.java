package org.openclicker.server.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class HibernateUtilTest {
  
  @Before
  public void setUp() throws Exception {}
  
  @Test
  public void testGetSessionFactory() {
    assertNotNull(HibernateUtil.getSessionFactory());
  }
  
}
