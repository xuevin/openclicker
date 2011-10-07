package org.openclicker.server.domain;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class AvailableChoiceTest {
  
  private AvailableChoice choice1;
  private AvailableChoice choice2;
  
  @Before
  public void setUp() {
    choice1 = new AvailableChoice();
    choice2 = new AvailableChoice("FooBar");
  }
  
  @Test
  public void testAvailableChoice() {
    assertNotNull(choice1);
  }
  
  @Test
  public void testAvailableChoiceString() {
    assertNotNull(choice2);
  }
  
  @Test
  public void testGetChoice_uid() {
    assertEquals(0,choice1.getChoice_uid());
  }
  
  @Test
  public void testSetDescription() {
    assertEquals(null,choice1.getDescription());
    choice1.setDescription("happy");
    assertEquals("happy",choice1.getDescription());
  }
  
  @Test
  public void testGetDescription() {
    assertEquals(null,choice1.getDescription());
    assertEquals("FooBar",choice2.getDescription());
  }
  
  @Test
  public void testSetQuizesAsChoice() {
    Set<Quiz> setOfChoices = new HashSet<Quiz>();
    choice1.setQuizesAsChoice(setOfChoices);
    assertEquals(setOfChoices, choice1.getQuizesAsChoice());
  }
  
  @Test
  public void testGetQuizesAsChoice() {
    assertEquals(0, choice1.getQuizesAsChoice().size());
    Set<Quiz> setOfChoices = new HashSet<Quiz>();
    choice1.setQuizesAsChoice(setOfChoices);
    assertEquals(setOfChoices, choice1.getQuizesAsChoice());
  }
  
  @Test
  public void testSetQuizesAsAnswer() {
    Set<Quiz> setOfChoices = new HashSet<Quiz>();
    choice1.setQuizesAsAnswer(setOfChoices);
    assertEquals(setOfChoices, choice1.getQuizesAsAnswer());
  }
  
  @Test
  public void testGetQuizesAsAnswer() {
    assertEquals(0, choice1.getQuizesAsAnswer().size());
    Set<Quiz> setOfChoices = new HashSet<Quiz>();
    choice1.setQuizesAsAnswer(setOfChoices);
    assertEquals(setOfChoices, choice1.getQuizesAsAnswer());
  }
  
}
