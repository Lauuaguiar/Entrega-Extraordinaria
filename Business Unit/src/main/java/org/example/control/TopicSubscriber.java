package org.example.control;

import jakarta.jms.Message;

import java.util.List;
public interface TopicSubscriber {
    List<Message> subscribe(List<String> topics) ;
}
