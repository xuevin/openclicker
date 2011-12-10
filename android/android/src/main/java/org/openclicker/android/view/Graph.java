package org.openclicker.android.view;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Graph extends View {
  private Vector<String> choiceVec = new Vector<String>(); // Vector contain the
  // choices
  // Contain the number of student picked the particular choice
  // Note the choiceLabel[0] correspond to the choiceRespoundCount[0]
  private Vector<Integer> countVec = new Vector<Integer>();
  private Vector<String> answerVec = new Vector<String>();
  
  private int screenX; // The screen's width
  private int screenY; // The screen's height
  private int numOfChoice; // Total number of choices
  private Paint textPaint;
  private Paint correctPaint;
  private Paint incorrectPaint;
  private Paint titlePaint;
  
  public Graph(Context context, int myScreenX, int myScreenY,
      Vector<String> myChoiceVec, Vector<Integer> myCountVec,
      Vector<String> myAnswerVec, int myNumOfChoice) {
    super(context);
    screenY = myScreenY; // Width
    screenX = myScreenX; // Height
    choiceVec = myChoiceVec; // List of choices
    countVec = myCountVec; // List of the count corresponds to choices
    answerVec = myAnswerVec; // List of answer
    numOfChoice = myNumOfChoice; // Number of choices
    setup();
  }
  
  public void setup() {
    textPaint = new Paint();
    textPaint.setStyle(Paint.Style.FILL);
    textPaint.setColor(Color.BLACK);
    textPaint.setAntiAlias(true);
    textPaint.setTextSize(15);
    
    titlePaint = new Paint();
    titlePaint.setStyle(Paint.Style.FILL);
    titlePaint.setColor(Color.BLACK);
    titlePaint.setAntiAlias(true);
    titlePaint.setTextSize(20);
    
    correctPaint = new Paint();
    correctPaint.setStyle(Paint.Style.FILL);
    correctPaint.setColor(Color.GREEN);
    
    incorrectPaint = new Paint();
    incorrectPaint.setStyle(Paint.Style.FILL);
    incorrectPaint.setColor(Color.BLACK);
  }
  
  public void onDraw(Canvas canvas) {
    canvas.drawColor(Color.WHITE);
    
    // // Temp paint to switch between correct and wrong ones during the loop
    // Paint tempPaint = new Paint();
    
    /*
     * To display the height and width of the display
     * canvas.drawText("Screen Width = ", 5, screenY - 50, textPaint);
     * canvas.drawText(Integer.toString(screenY), 120, screenY - 50, textPaint);
     * canvas.drawText("Screen Height = ", 5, screenY - 30, textPaint);
     * canvas.drawText(Integer.toString(screenX), 120, screenY - 30, textPaint);
     */

    int lineY = (screenY - 60);
    
    // Calculate the measurements
    int labelX = (screenX / numOfChoice); // Distance between each label
    int labelY = (screenY - 35); // Height of the label
    int tempLabelX = 35; // The buffer
    int maxValue = findMaxCount();
    
    // /////////////////////////////////////////////////////////////////////
    // Setting it to 4 for now, since we don't have a working server yet!
    // Remember to change the function in the drawing to actual values
    // /////////////////////////////////////////////////////////////////////
    int scale = (lineY - 50) / maxValue;
    int graphTop;
    
    for (int i = 0; i < numOfChoice; i++) {
      // Calculate how tall this bar is going to be base on the number
      // of the student picked this choice
      graphTop = lineY - (countVec.get(i) * scale);
      
      // Draw the label on the correct spot
      canvas.drawText(choiceVec.get(i), tempLabelX, labelY, textPaint);
      
      // If the choice is correct, we draw the rectangle with green
      if (answerVec.contains((choiceVec.get(i)))) {
        canvas.drawRect(tempLabelX, graphTop, tempLabelX + 35, lineY,
            correctPaint);
      }
      // Else we draw it with black or (incorrectPaint)
      else {
        canvas.drawRect(tempLabelX, graphTop, tempLabelX + 35, lineY,
            incorrectPaint);
      }
      
      // // canvas.drawText(countVec.get(i).toString(), tempLabelX + 10,
      // graphTop - 5, textPaint);
      canvas.drawText(Integer.toString(countVec.get(i)), tempLabelX + 10,
          graphTop - 5, textPaint);
      tempLabelX = tempLabelX + labelX;
    }
    
    // Draw the Graph base line
    canvas.drawLine(0, lineY, screenX, lineY, textPaint);
    
    // Draw the title here
    canvas.drawText("Class Statistic", 10, 25, titlePaint);
  }
  
  private Integer findMaxCount() {
    Integer highestRespond = 0;
    for (int i = 0; i < countVec.size(); i++) {
      if (highestRespond < countVec.get(i)) highestRespond = countVec.get(i);
    }
    // Prevent dividing by 0 error
    if (highestRespond == 0) return 1;
    else return highestRespond;
  }
}
