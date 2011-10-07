package org.openclicker.server.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class ClassTest {
  
  private Class englishClass1;
  private Class englishClass2;

  @Before
  public void setUp() throws Exception {
    englishClass1 = new Class();
    englishClass2 = new Class("English");
  }
  
  @Test
  public void testClass() {
    assertNotNull(englishClass1);
  }
  
  @Test
  public void testClassString() {
    assertNotNull(englishClass2);
  }
  
  @Test
  public void testGetClass_uid() {
    assertEquals(0, englishClass1.getClass_uid());
  }
  
  @Test
  public void testSetClass_name() {
    englishClass1.setClass_name("Foo");
    assertEquals("Foo",englishClass1.getClass_name());
  }
  
  @Test
  public void testGetClass_name() {
    assertEquals(null,englishClass1.getClass_name());
    assertEquals("English", englishClass2.getClass_name());
  }
  
  @Test
  public void testAddStudent() {
    assertEquals(0, englishClass1.getStudents().size());
    Student student = new Student();
    englishClass1.addStudent(student);
    assertEquals(1, englishClass1.getStudents().size());
  }
  
  @Test
  public void testRemoveStudent() {
    assertEquals(0, englishClass1.getStudents().size());
    Student student = new Student();
    englishClass1.addStudent(student);
    assertEquals(1, englishClass1.getStudents().size());
    englishClass1.removeStudent(student);
    assertEquals(0, englishClass1.getStudents().size());
  }
  
  @Test
  public void testAddQuizes() {
    assertEquals(0, englishClass1.getQuizes().size());
    Quiz quiz = new Quiz();
    englishClass1.addQuizes(quiz);
    assertEquals(1, englishClass1.getQuizes().size());
  }
  
  @Test
  public void testRemoveQuizes() {
    assertEquals(0, englishClass1.getQuizes().size());
    Quiz quiz = new Quiz();
    englishClass1.addQuizes(quiz);
    assertEquals(1, englishClass1.getQuizes().size());
    englishClass1.removeQuizes(quiz);
    assertEquals(0, englishClass1.getQuizes().size());
    
  }
  
  @Test
  public void testSetStudents() {
    Set<Student> students = new HashSet<Student>();
    englishClass1.setStudents(students);
    assertEquals(students, englishClass1.getStudents());
  }
  
  @Test
  public void testGetStudents() {
    assertEquals(0, englishClass1.getStudents().size());
  }
  
  @Test
  public void testSetQuizes() {
    Set<Quiz> foo = new HashSet<Quiz>();
    englishClass1.setQuizes(foo);
    assertEquals(foo,englishClass1.getQuizes());
  }
  
  @Test
  public void testGetQuizes() {
    assertEquals(0, englishClass1.getQuizes().size());
  }
  
  @Test
  public void testEquals(){
    Class student = new Class();
    student.setClass_uid(100);
    
    Class class2 = new Class();
    class2.setClass_uid(200);

    assertThat(student,not(equalTo(class2)));
    
    class2.setClass_uid(100);
    assertEquals(student, class2);
  }
  
}
