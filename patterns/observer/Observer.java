/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patterns.observer;

import models.Notification;

/**
 * @author Aruna Indika
  */
public interface Observer {
    void update(Notification notification);
}