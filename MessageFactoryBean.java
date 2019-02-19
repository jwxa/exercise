package com.github.jwxa;

/**
 * 类描述
 * <p>
 * 方法描述列表
 * </p>
 * User: Jwxa Date: 2019/2/19 ProjectName: coding-myself Version: 1.0
 */
public class MessageFactoryBean implements MessageFactory {

  public static volatile MessageFactory instance;

  public static MessageFactory getInstance() {
    if (instance == null) {
      synchronized (MessageFactory.class) {
        if (instance == null) {
          instance = new MessageFactoryBean();
        }
      }
    }
    return instance;
  }

  @Override
  public Message newMessage(String countryCode) {
    Message message = (Message) () -> System.out.println(countryCode);
    return message;
  }
}
