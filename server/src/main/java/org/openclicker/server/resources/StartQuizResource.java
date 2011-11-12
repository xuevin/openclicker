package org.openclicker.server.resources;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.openclicker.server.domain.Class;
import org.openclicker.server.domain.Quiz;
import org.openclicker.server.util.EmptyValueException;
import org.openclicker.server.util.HibernateUtil;
import org.openclicker.server.util.SetStatusException;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartQuizResource extends ServerResource {
  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  public static final Collection<ClassQuizIdentifier> activeClassQuizContainer = new HashSet<ClassQuizIdentifier>();
  
  @Get
  public String retrieve() {
    // For now, there is no getting allowed.
    throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED);
    
  }
  
  @Post
  public Representation acceptJsonRepresentation(Representation entity) {
    
    try {
      if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
        
        Integer class_uid = Integer.parseInt((String) getRequestAttributes()
            .get("class_uid"));
        
        Integer quiz_uid = Integer.parseInt((String) getRequestAttributes()
            .get("quiz_uid"));
        JSONObject json = (JSONObject) JSONSerializer.toJSON(entity.getText());
        
        boolean acceptAnswers;
        if (json.get("acceptAnswers").equals("true")) {
          acceptAnswers = true;
        } else {
          acceptAnswers = false;
        }
        
        setStatusOfQuiz(class_uid, quiz_uid, acceptAnswers, true);
        setStatus(Status.SUCCESS_ACCEPTED);
        
        Representation rep = new StringRepresentation("Quiz status changed",
            MediaType.TEXT_PLAIN);
        return rep;
      } else {
        throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
      }
    } catch (JSONException e) {
      throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
    } catch (IOException e) {
      logger.warn(e.getMessage());
      throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
    } catch (NumberFormatException e) {
      throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
    } catch (EmptyValueException e) {
      throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
    } catch (SetStatusException e) {
      logger.warn(e.getMessage());
      throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST);
    }
    
  }
  
  protected void setStatusOfQuiz(Integer class_uid, Integer quiz_uid,
      boolean acceptResponses, boolean commit) throws EmptyValueException,
      SetStatusException {
    
    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
    try {
      session.beginTransaction();
      
      // Attempt to get the class and quiz
      Class tempClass = (Class) session.get(Class.class, class_uid);
      Quiz tempQuiz = (Quiz) session.get(Quiz.class, quiz_uid);
      if (tempClass == null) {
        throw new EmptyValueException("Class is not available");
      } else if (tempQuiz == null) {
        throw new EmptyValueException("Quiz is not available");
      }
      
      ClassQuizIdentifier classQuizPair = new ClassQuizIdentifier(class_uid,
          quiz_uid);
      
      if (activeClassQuizContainer.contains(classQuizPair)) {
        // The only thing you can do is stop the quiz!
        if (acceptResponses == false) {
          activeClassQuizContainer.remove(classQuizPair);
          logger.info("Quiz " + quiz_uid + " successfully stopped for "
              + class_uid);
          return;
        } else {
          throw new SetStatusException("Quiz " + quiz_uid + " in class "
              + class_uid + " is already active!");
        }
      }
      
      // Situation when the quiz in question has already been asked
      if (tempClass.getQuizzes_Unmodifiable().contains(tempQuiz)) {
        throw new SetStatusException("Quiz " + quiz_uid
            + " is no longer accepting responses for class " + class_uid);
      }
      
      // Time to add a new quiz to the class
      if (acceptResponses == false) {
        throw new SetStatusException("Cannot stop a quiz which is not active!");
      } else {
        // Add the quiz to the class
        tempClass.addQuizes(tempQuiz);
        session.save(tempClass);// Not sure if this will work
        if (commit) {
          session.getTransaction().commit();
        }else{
          session.close();
        }
        logger.info("Quiz " + quiz_uid + " successfully started for Class "
            + class_uid);
        activeClassQuizContainer.add(classQuizPair);
      }
    } catch (HibernateException e) {
      session.close();
      throw new ResourceException(Status.CLIENT_ERROR_CONFLICT);
    }
  }
  
  class ClassQuizIdentifier {
    private int class_uid;
    private int quiz_uid;
    
    public ClassQuizIdentifier(int class_uid, int quiz_uid) {
      this.class_uid = class_uid;
      this.quiz_uid = quiz_uid;
    }
    
    public int getClassUID() {
      return class_uid;
    }
    
    public int getQuizUID() {
      return quiz_uid;
    }
    
    @Override
    public boolean equals(Object other) {
      if (this == other) return true;
      if (!(other instanceof ClassQuizIdentifier)) return false;
      
      final ClassQuizIdentifier tempID = (ClassQuizIdentifier) other;
      if (tempID.getClassUID() == getClassUID()
          && tempID.getQuizUID() == getQuizUID()) {
        return true;
      }
      return false;
    }
    
    @Override
    public int hashCode() {
      
      return getClassUID() * 127 + getQuizUID();
    }
  }
}
