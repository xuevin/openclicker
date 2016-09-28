package org.openclicker.server.resources;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.SetStatusException;

public class QuizResponseResourceTest {
  
  private StartQuizResource foo;
  @Before
  public void setUp() throws Exception {
    foo = new StartQuizResource();
  }
  
  @Test
  public void testStartNullClass() throws SetStatusException {
    try{
      foo.setStatusOfQuiz(0, 1, false, false);
    }catch(EmptyValueException e){
      assertEquals("Class is not available",e.getMessage());
    }
  }
  @Test
  public void testStartNullQuiz() throws SetStatusException {
    try{
      foo.setStatusOfQuiz(1, 0, true, false);
    }catch(EmptyValueException e){
      assertEquals("Quiz is not available",e.getMessage());
    }
  }
  
  @Test
  public void testStartOldQuiz()
      throws EmptyValueException, SetStatusException {
    try {
      foo.setStatusOfQuiz(1, 1, true, false);
    } catch (SetStatusException e) {
      assertEquals("Quiz 1 is no longer accepting responses for class 1", e.getMessage());
    }
  }
  
  @Test
  public void testStopOldQuiz() throws EmptyValueException, SetStatusException{
    try {
      foo.setStatusOfQuiz(1, 1, false, false);
    } catch (SetStatusException e) {
      assertEquals("Quiz 1 is no longer accepting responses for class 1", e.getMessage());
    }
  }
  @Test
  public void testStartStartNewQuiz() throws EmptyValueException, SetStatusException{
    try{
      foo.setStatusOfQuiz(1, 2, true, false);
      foo.setStatusOfQuiz(1, 2, true, false);
    }catch (SetStatusException e){
      assertEquals("Quiz 2 in class 1 is already active!", e.getMessage());
    }
  }
  @Test
  public void testEndNewQuiz() throws EmptyValueException, SetStatusException{
    try{
      foo.setStatusOfQuiz(1, 2, false, false);
    }catch (SetStatusException e){
      assertEquals("Cannot stop a quiz which is not active!", e.getMessage());
    }
  }
  @Test
  public void testStartEndNewQuiz() throws EmptyValueException, SetStatusException{
      foo.setStatusOfQuiz(1, 2, true, false);
      foo.setStatusOfQuiz(1, 2, false, false);
      //TODO missing an assert
  }
}
