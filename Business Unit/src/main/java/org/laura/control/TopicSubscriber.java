package org.laura.control;

import jakarta.jms.Message;

import java.util.List;
public interface TopicSubscriber {
    List<Message> subscriber(List<String> topics) ;
}
